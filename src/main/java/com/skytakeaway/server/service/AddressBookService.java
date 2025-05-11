package com.skytakeaway.server.service;

import com.skytakeaway.pojo.dto.AddressBookDTO;
import com.skytakeaway.pojo.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void insertAddress(AddressBookDTO addressBookDTO);

    List<AddressBook> getAddressByUserId();

    List<AddressBook> getUserDefaultAddress();

    void updateUserAddress(AddressBook addressBook);

    void deleteAddress(Long id);

    AddressBook getAddressById(Long id);

    void setDefaultAddress(Long id);
}
