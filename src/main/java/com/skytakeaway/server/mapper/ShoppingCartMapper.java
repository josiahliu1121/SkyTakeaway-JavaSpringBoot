package com.skytakeaway.server.mapper;

import com.skytakeaway.pojo.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Update("update shopping_cart_table set number = #{number}, price = #{price} where id = #{id}")
    void addNumber(ShoppingCart cart);

    @Insert("insert into shopping_cart_table (name, image, user_id, dish_id, setmeal_id, dish_flavor, price, number, amount, create_time) " +
            "VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{price}, #{number}, #{amount}, #{createTime})")
    void addShoppingCart(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart_table where user_id = #{userId}")
    void deleteByUserId(Long userId);

    @Delete("delete from shopping_cart_table where id = #{id}")
    void deleteById(Long id);
}
