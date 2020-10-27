package mapper;

import entity.Type;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author songbin
 * @date 2020/10/8
 */
@Mapper
public interface TypeMapper {
    /**
     * 查询所有类型
     * @return
     */
    List<Type> selectAllTypes();

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    Type selectType(int id);

    /**
     * 根据name查询分类
     * @return
     */
    Type selectTypeByName(String name);

    /**
     * 新增类型
     * @param type
     * @return
     */
    int insertType(Type type);

    /**
     * 更新类型
     * @param type
     * @return
     */
    int updateType(Type type);

    /**
     * 删除类型
     * @param id
     */
    void deleteType(int id);
}
