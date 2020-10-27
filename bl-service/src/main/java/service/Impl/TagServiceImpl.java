package service.Impl;

import entity.Tag;
import exceoption.NotFoundException;
import mapper.TagMapper;
import service.BlogService;
import service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songbin
 * @date 2020/10/11
 */
@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;
    @Resource
    private BlogService blogService;

    @Transactional
    @Override
    public int saveTag(Tag tag) {
        return tagMapper.insertTag(tag);
    }

    @Transactional
    @Override
    public Tag getTag(int id) {
        return tagMapper.selectTag(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagMapper.selectTagByName(name);
    }

    @Transactional
    @Override
    public List<Tag> listTag() {
        List<Tag> tags = tagMapper.selectAllTags();
        for (Tag tag : tags) {
            tag.setBlogs(blogService.listBlogByTagId(tag.getId()));
        }
        return tags;
    }

    @Override
    public List<Tag> listTag(String tagIds) {
        List<Tag> list = new ArrayList<>();
        if (tagIds.length() > 1){
            String[] tags = tagIds.split(",");
            for (String tag : tags) {
                list.add(tagMapper.selectTag(Integer.parseInt(tag)));
            }
        }else {
            list.add(tagMapper.selectTag(Integer.parseInt(tagIds)));
        }

        //搜索博客
        for (Tag tag : list) {
            blogService.listBlogByTagId(tag.getId());
        }

        return list;
    }

    @Transactional
    @Override
    public int update(int id, Tag tag) {
        Tag t = tagMapper.selectTag(id);

        if (t ==null){
            throw new NotFoundException("标签不存在");
        }
        BeanUtils.copyProperties(tag,t);
        return tagMapper.updateTag(t);
    }

    @Transactional
    @Override
    public void deleteTag(int id) {
        tagMapper.deleteTag(id);
    }
}
