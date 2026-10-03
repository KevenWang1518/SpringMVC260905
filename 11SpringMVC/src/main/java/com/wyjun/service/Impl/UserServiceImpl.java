package com.wyjun.service.Impl;

import com.wyjun.entity.User;
import com.wyjun.mapper.UserMapper;
import com.wyjun.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //自动注入了:通过构造方法注入的。
    private final UserMapper userMapper;

    @Override
    public int save(User user) {
        return userMapper.insert(user);
    }
}
