package com.skytakeaway.server.service.implement;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.skytakeaway.common.constant.MessageConstant;
import com.skytakeaway.common.context.BaseContext;
import com.skytakeaway.common.exception.AddressIsEmptyException;
import com.skytakeaway.common.exception.OrderBusinessException;
import com.skytakeaway.common.exception.ShoppingCartEmpty;
import com.skytakeaway.pojo.dto.OrdersPageQueryDTO;
import com.skytakeaway.pojo.dto.OrdersSubmitDTO;
import com.skytakeaway.pojo.entity.AddressBook;
import com.skytakeaway.pojo.entity.OrderDetail;
import com.skytakeaway.pojo.entity.Orders;
import com.skytakeaway.pojo.entity.ShoppingCart;
import com.skytakeaway.pojo.vo.OrdersSubmitVO;
import com.skytakeaway.pojo.vo.OrdersVo;
import com.skytakeaway.pojo.vo.UserOrdersVo;
import com.skytakeaway.server.mapper.*;
import com.skytakeaway.server.service.OrdersService;
import com.skytakeaway.server.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    AddressBookMapper addressBookMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    WebSocketServer webSocketServer;
    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional
    public OrdersSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //check address book exists or not
        AddressBook addressBook = addressBookMapper.selectAddressById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressIsEmptyException(MessageConstant.ADDRESS_IS_EMPTY);
        }

        //check shopping cart is empty or not
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if(shoppingCartList == null || shoppingCartList.size() == 0){
            throw new ShoppingCartEmpty(MessageConstant.SHOPPING_CART_IS_EMPTY);
        }

        //insert one new order
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);

        orders.setAddress(addressBook.getAddress());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());

        orders.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        orders.setUserId(userId);
        orders.setUserName(userMapper.getById(userId).getUserName());
        orders.setPayStatus(Orders.WAITING_FOR_PAYMENT);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setOrderTime(LocalDateTime.now());

        ordersMapper.insert(orders);

        //insert n order detail
        List<OrderDetail> orderDetails = new ArrayList<>();
        shoppingCartList.forEach(shoppingCart1 -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            BeanUtils.copyProperties(shoppingCart1, orderDetail);
            orderDetails.add(orderDetail);
        });

        orderDetailMapper.insertBatch(orderDetails);

        //clean user shopping cart
        shoppingCartMapper.deleteByUserId(userId);

        //return result in VO
        OrdersSubmitVO ordersSubmitVO = OrdersSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getOrderNumber())
                .orderAmount(orders.getAmount())
                .build();

        return ordersSubmitVO;
    }

    @Override
    public void reminder(Long id) {
        Orders orderDB = ordersMapper.getById(id);

        if(orderDB == null || !orderDB.getStatus().equals(Orders.RECEIVED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //send message to admin client through websocket
        Map map = new HashMap();
        map.put("type", 2); // 1 = receive customer order, 2 = customer urging
        map.put("orderId", orderDB.getId());
        map.put("content","orderNumber: " + orderDB.getOrderNumber());

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

    //admin use
    @Override
    public OrdersVo pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        //prepare page query result
        PageHelper.startPage(ordersPageQueryDTO.getPageNumber(), ordersPageQueryDTO.getPageSize());
        Page<Orders> result = ordersMapper.pageQuery(ordersPageQueryDTO);

        //prepare other data
        Map<String, Object> map = new HashMap<>();
        map.put("begin", ordersPageQueryDTO.getBegin());
        map.put("end", ordersPageQueryDTO.getEnd());
        map.put("status", Orders.PENDING_RECEIVE);
        Integer pendingCount = ordersMapper.countByMap(map);

        map.put("status", Orders.UNDER_DELIVERY);
        Integer deliveryCount = ordersMapper.countByMap(map);

        map.put("status", Orders.RECEIVED);
        Integer receiveCount = ordersMapper.countByMap(map);

        return OrdersVo.builder()
                .records(result.getResult())
                .total(result.getTotal())
                .deliveryCount(deliveryCount)
                .pendingCount(pendingCount)
                .receiveCount(receiveCount)
                .build();
    }

    @Override
    public void updateStatusById(Integer status, Long id) {
        Orders order = ordersMapper.getById(id);
        //when admin cancel user order
        if(status == Orders.CANCELED){
            order.setDeliveryStatus(Orders.DELIVERED); //so it will show as history for user

            if(order.getPayStatus() == Orders.PAID){
                //refund to customer
                //TODO

                order.setPayStatus(Orders.REFUNDED);
            }
        }

        //when complete (user receive)
        if(status == Orders.COMPLETE){
            order.setDeliveryStatus(Orders.DELIVERED);
            order.setDeliveryTime(LocalDateTime.now());
        }

        order.setStatus(status);
        ordersMapper.update(order);
        //update status in order table
    }

    @Override
    public List<OrderDetail> getOrderDetail(Long id) {
        return orderDetailMapper.getById(id);
    }

    @Override
    public List<UserOrdersVo> getOrderForUser(Integer deliveryStatus) {
        List<Orders> orders = ordersMapper.getByUserIdAndDeliveryStatus(BaseContext.getCurrentId(), deliveryStatus);

        //return null if no order found
        if(orders == null || orders.isEmpty()){
            return null;
        }

        List<Long> ordersIds = new ArrayList<>();
        orders.forEach(order -> ordersIds.add(order.getId()));

        //get order items
        List<OrderDetail> orderItems = orderDetailMapper.getByIds(ordersIds);

        //convert to VO
        List<UserOrdersVo> result = new ArrayList<>();

        Map<Long, List<OrderDetail>> detailsMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderDetail::getOrderId));

        orders.forEach(order -> {
            UserOrdersVo userOrdersVo = new UserOrdersVo();
            BeanUtils.copyProperties(order, userOrdersVo);

            userOrdersVo.setOrderItems(detailsMap.getOrDefault(order.getId(), new ArrayList<>()));

            result.add(userOrdersVo);
        });

        return result;
    }

    @Override
    public void userReceiveOrder(Long id) {
        updateStatusById(Orders.COMPLETE, id);
    }

    @Override
    public void paySuccess(String orderNumber){
        Long userId= BaseContext.getCurrentId();
        System.out.println(orderNumber);

        Orders orderDB = ordersMapper.getByOrderNumberAndUserId(orderNumber, userId);

        Orders orders = Orders.builder()
                .id(orderDB.getId())
                .status(Orders.PENDING_RECEIVE)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        ordersMapper.update(orders);

        //send message to admin client through websocket
        Map map = new HashMap();
        map.put("type", 1); // 1 = receive customer order, 2 = customer urging
        map.put("orderId", orderDB.getId());
        map.put("content","orderNumber: " + orderNumber);

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }
}
