package com.skytakeaway.server.service;

import com.skytakeaway.pojo.entity.User;
import com.skytakeaway.pojo.vo.UserVO;

public interface UserService {
    UserVO getUserProfile();

    void editProfile(User user);
}
