package service;

import entity.Tag;

import java.util.List;

/**
 * @author songbin
 * @date 2020/10/9
 */
public interface TagService {

    int saveTag(Tag tag);

    Tag getTag(int id);

    Tag getTagByName(String name);

    List<Tag> listTag();

    List<Tag> listTag(String tagIds);

    int update(int id,Tag tag);

    void deleteTag(int id);
}
