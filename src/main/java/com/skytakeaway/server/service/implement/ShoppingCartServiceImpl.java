package com.skytakeaway.server.service.implement;

import com.skytakeaway.common.context.BaseContext;
import com.skytakeaway.pojo.dto.ShoppingCartDTO;
import com.skytakeaway.pojo.entity.Dish;
import com.skytakeaway.pojo.entity.Setmeal;
import com.skytakeaway.pojo.entity.ShoppingCart;
import com.skytakeaway.server.mapper.DishMapper;
import com.skytakeaway.server.mapper.SetmealMapper;
import com.skytakeaway.server.mapper.ShoppingCartMapper;
import com.skytakeaway.server.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //check if data exists in shopping_cart_table
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //if exists, set new number
        if(list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            cart.setNumber(shoppingCartDTO.getNumber() + cart.getNumber());

            //find item price and update amount
            cart.setAmount(cart.getPrice().multiply(new BigDecimal(cart.getNumber())));

            shoppingCartMapper.addNumber(cart);
            return;
        }

        //insert date into table
        //determine it is dish or setmeal
        Long dishId = shoppingCartDTO.getDishId();
        if(dishId != null){
            Dish dish = dishMapper.getDishById(dishId);

            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setPrice(dish.getPrice());
            shoppingCart.setAmount(dish.getPrice().multiply(BigDecimal.valueOf(shoppingCartDTO.getNumber())));
        } else {
            Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());

            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setPrice(setmeal.getPrice());
            shoppingCart.setAmount(setmeal.getPrice().multiply(BigDecimal.valueOf(shoppingCartDTO.getNumber())));
        }

        shoppingCart.setCreateTime(LocalDateTime.now());

        shoppingCartMapper.addShoppingCart(shoppingCart);
    }

    @Override
    public List<ShoppingCart> getShoppingCartList() {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();

        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void cleanShoppingCart() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }

    @Override
    public void deleteById(Long id) {
        shoppingCartMapper.deleteById(id);
    }
}
