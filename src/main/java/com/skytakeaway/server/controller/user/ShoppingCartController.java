package com.skytakeaway.server.controller.user;

import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.dto.ShoppingCartDTO;
import com.skytakeaway.pojo.entity.ShoppingCart;
import com.skytakeaway.server.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "user shopping cart interface")
@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @Operation(summary = "add item into shopping cart")
    public Result<Void> addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "get shopping cart list")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> list = shoppingCartService.getShoppingCartList();
        return Result.success(list);
    }

    @DeleteMapping("/clean")
    @Operation(summary = "clean shopping cart")
    public Result<Void> cleanShoppingCart(){
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "delete cart item by id")
    public Result<Void> deleteShoppingCart(@PathVariable Long id){
        shoppingCartService.deleteById(id);
        return Result.success();
    }
}
