package service;

import entity.User;
import org.springframework.stereotype.Service;

/**
 * @author songbin
 * @date 2020/10/9
 */
@Service
public interface UserService {

    User findUserByUsernameAndPassword(String username, String password);

    User findUserByUsername(String username);
}
