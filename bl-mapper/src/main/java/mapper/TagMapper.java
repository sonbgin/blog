package mapper;

import entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author songbin
 * @date 2020/10/8
 */
@Mapper
public interface TagMapper {
    /**
     * 查询所有类型
     * @return
     */
    List<Tag> selectAllTags();

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    Tag selectTag(int id);

    /**
     * 根据name查询分类
     * @return
     */
    Tag selectTagByName(String name);

    /**
     * 新增类型
     * @param tag
     * @return
     */
    int insertTag(Tag tag);

    /**
     * 更新类型
     * @param tag
     * @return
     */
    int updateTag(Tag tag);

    /**
     * 删除类型
     * @param id
     */
    void deleteTag(int id);
}
