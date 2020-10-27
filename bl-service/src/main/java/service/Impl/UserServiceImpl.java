package service.Impl;

import entity.User;
import mapper.UserMapper;
import service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author songbin
 * @date 2020/10/9
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        return userMapper.selectUserByUsernameAndPassword(username,password);
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }
}
