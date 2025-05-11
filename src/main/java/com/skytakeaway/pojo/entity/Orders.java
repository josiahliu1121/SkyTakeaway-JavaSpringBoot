package com.skytakeaway.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {
    //status
    public static final int PENDING_PAYMENT = 1;
    public static final int PENDING_RECEIVE = 2;
    public static final int RECEIVED = 3;
    public static final int UNDER_DELIVERY = 4;
    public static final int COMPLETE = 5;
    public static final int CANCELED = 6;

    //payStatus
    public static final int WAITING_FOR_PAYMENT = 0;
    public static final int PAID = 1;
    public static final int REFUNDED = 2;

    //deliveryStatus
    public static final int DELIVERED = 1;
    public static final int DELIVERING = 0;

    private Long id;
    private String orderNumber;
    private Integer status;
    private Long userId;
    private String userName;
    private Long addressBookId;
    private LocalDateTime orderTime;
    private LocalDateTime checkoutTime;
    private Integer payMethod;
    private Integer payStatus;
    private BigDecimal amount;
    private String remark;
    private String phone;
    private String address;
    private String consignee;
    private String cancelReason;
    private String rejectionReason;
    private LocalDateTime cancelTime;
    private LocalDateTime estimatedDeliveryTime;
    private Integer deliveryStatus; //0 is not yet, 1 is done, this is for user use
    private LocalDateTime deliveryTime;
    private Integer packAmount;
    private Integer tablewareNumber;
    private Integer tablewareStatus;
}
