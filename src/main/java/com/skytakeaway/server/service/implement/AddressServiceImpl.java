package com.skytakeaway.server.service.implement;

import com.skytakeaway.common.context.BaseContext;
import com.skytakeaway.pojo.dto.AddressBookDTO;
import com.skytakeaway.pojo.entity.AddressBook;
import com.skytakeaway.server.mapper.AddressBookMapper;
import com.skytakeaway.server.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AddressServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    @Transactional
    public void insertAddress(AddressBookDTO addressBookDTO) {
        AddressBook addressBook = new AddressBook();
        BeanUtils.copyProperties(addressBookDTO, addressBook);

        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);

        // Check if this is the user's first address
        if (addressBookMapper.selectAddressCountByUserId(userId) == 0) {
            addressBook.setIsDefault(1);
        } else if (addressBook.getIsDefault() != null && addressBook.getIsDefault() == 1) {
            // If explicitly marked as default, reset other addresses first
            addressBookMapper.updateIsDefaultByUserId(userId);
        }

        addressBookMapper.insertAddress(addressBook);
    }

    @Override
    public List<AddressBook> getAddressByUserId() {
        return addressBookMapper.selectAddressByUserId(BaseContext.getCurrentId());
    }

    @Override
    public List<AddressBook> getUserDefaultAddress() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);

        return addressBookMapper.list(addressBook);
    }

    @Override
    @Transactional
    public void updateUserAddress(AddressBook addressBook) {
        //determine new address is set to default or not
        if(addressBook.getIsDefault() == 1){
            //set all user address is_default = 0
            addressBookMapper.updateIsDefaultByUserId(BaseContext.getCurrentId());
        }

        addressBookMapper.updateUserAddress(addressBook);
    }

    @Override
    public void deleteAddress(Long id) {
        addressBookMapper.deleteAddressById(id);
    }

    @Override
    public AddressBook getAddressById(Long id) {
        return addressBookMapper.selectAddressById(id);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long id) {
        //set all user address is_default = 0
        addressBookMapper.updateIsDefaultByUserId(BaseContext.getCurrentId());

        //set default address
        addressBookMapper.setDefaultAddress(id);
    }
}
