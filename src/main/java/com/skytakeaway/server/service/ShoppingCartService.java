package com.skytakeaway.server.service;

import com.skytakeaway.pojo.dto.ShoppingCartDTO;
import com.skytakeaway.pojo.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> getShoppingCartList();

    void cleanShoppingCart();

    void deleteById(Long id);
}
