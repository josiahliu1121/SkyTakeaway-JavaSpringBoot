package com.skytakeaway.server.controller.user;

import com.skytakeaway.common.result.Result;
import com.skytakeaway.pojo.entity.User;
import com.skytakeaway.pojo.vo.UserVO;
import com.skytakeaway.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "user interface")
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "get user profile")
    public Result<UserVO> userProfile() {
        return Result.success(userService.getUserProfile());
    }

    @PostMapping("/editProfile")
    @Operation(summary = "edit profile")
    public Result<Void> editProfile(@RequestBody User user) {
        userService.editProfile(user);
        return Result.success();
    }
}
