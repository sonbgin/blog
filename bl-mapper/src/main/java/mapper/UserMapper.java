package mapper;

import entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author songbin
 * @date 2020/10/9
 */
@Mapper
@Repository
public interface UserMapper {

    /**
     * 根据用户名和密码查询用户信息
     * @param username
     * @param password
     * @return
     */
    User selectUserByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    User findUserByUsername(String username);

    User selectUserbyId(int id);
}
