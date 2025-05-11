package com.skytakeaway.server.controller.user;

import com.skytakeaway.common.constant.MessageConstant;
import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.dto.AddressBookDTO;
import com.skytakeaway.pojo.entity.AddressBook;
import com.skytakeaway.server.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Address Book Interface")
@Slf4j
@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping("/insert")
    @Operation(summary = "add new address")
    public Result<Void> insertAddress (@RequestBody AddressBookDTO addressBookDTO){
        addressBookService.insertAddress(addressBookDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "get address by user id")
    public Result<List<AddressBook>> getAddressByUserId (){
        List<AddressBook> addressBookList = addressBookService.getAddressByUserId();
        return Result.success(addressBookList);
    }

    @GetMapping("/default")
    @Operation(summary = "get user default address")
    public Result<AddressBook> getUserDefaultAddress (){
        List<AddressBook> addressBookList = addressBookService.getUserDefaultAddress();

        if(addressBookList != null && addressBookList.size() == 1){
            return Result.success(addressBookList.get(0));
        }

        return Result.error(MessageConstant.DEFAULT_ADDRESS_NOT_FOUNT);
    }

    @PostMapping("/update")
    @Operation(summary = "update user address")
    public Result<Void> updateUserAddress (@RequestBody AddressBook addressBook){
        addressBookService.updateUserAddress(addressBook);
        return Result.success();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "delete address by id")
    public Result<Void> deleteAddress(@RequestParam Long id){
        addressBookService.deleteAddress(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get address by id")
    public Result<AddressBook> getAddressById (@PathVariable Long id){
        AddressBook addressBook = addressBookService.getAddressById(id);
        return Result.success(addressBook);
    }

    @PutMapping("/setDefault")
    @Operation(summary = "set user default address")
    public Result<Void> setDefaultAddress (@RequestParam Long id){
        addressBookService.setDefaultAddress(id);
        return Result.success();
    }
}
