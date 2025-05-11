package com.skytakeaway.server.mapper;

import com.skytakeaway.pojo.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    @Insert("insert into address_book_table (user_id, consignee, sex, phone, postcode, province, address, label, is_default) " +
            "values (#{userId}, #{consignee}, #{sex}, #{phone}, #{postcode}, #{province}, #{address}, #{label}, #{isDefault})")
    void insertAddress(AddressBook addressBook);

    @Select("select * from address_book_table where user_id = #{userId}")
    List<AddressBook> selectAddressByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM address_book_table WHERE user_id = #{userId}")
    int selectAddressCountByUserId(Long userId);

    List<AddressBook> list(AddressBook addressBook);

    void updateUserAddress(AddressBook addressBook);

    @Delete("delete from address_book_table where id = #{id}")
    void deleteAddressById(Long id);

    @Select("select * from address_book_table where id = #{id}")
    AddressBook selectAddressById(Long id);

    @Update("update address_book_table set is_default = 0 where user_id = #{userId}")
    void updateIsDefaultByUserId(Long userId);

    @Update("update address_book_table set is_default = 1 where id = #{id}")
    void setDefaultAddress(Long id);
}
