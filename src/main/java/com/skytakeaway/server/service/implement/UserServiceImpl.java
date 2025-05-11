package com.skytakeaway.server.service.implement;

import com.skytakeaway.common.context.BaseContext;
import com.skytakeaway.pojo.entity.AddressBook;
import com.skytakeaway.pojo.entity.User;
import com.skytakeaway.pojo.vo.UserVO;
import com.skytakeaway.server.mapper.AddressBookMapper;
import com.skytakeaway.server.mapper.UserMapper;
import com.skytakeaway.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public UserVO getUserProfile() {
        User user = userMapper.getById(BaseContext.getCurrentId());
        List<AddressBook> addressBooks = addressBookMapper.selectAddressByUserId(BaseContext.getCurrentId());

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setAddressBooks(addressBooks);

        return userVO;
    }

    @Override
    public void editProfile(User user) {
        userMapper.updateUser(user);
    }
}
