package service.Impl;

import entity.Blog;
import entity.Tag;
import entityUtil.BlogsForm;
import exceoption.NotFoundException;
import mapper.BlogMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import service.BlogService;
import service.CommentService;
import service.TagService;
import service.TypeService;
import util.MarkdownUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author songbin
 * @date 2020/10/12
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogMapper blogMapper;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;


    @Override
    public Blog getBlog(int id) {
        return blogMapper.selectBlogById(id);
    }

    @Override
    public Blog getBlogAndConvert(int id) {
        Blog blog = blogMapper.selectBlogById(id);
        blog.setTags(tagService.listTag(blog.getTagIds()));
        blog.setViews(blog.getViews()+1);
        blogMapper.updateBlog(blog);
        if (blog == null){
            throw new NotFoundException("资源不存在");
        }
        blog.setComments(commentService.listComment(id));
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String s = MarkdownUtils.markdownToHtmlExtensions(b.getContent());
        blog.setContent(s);
        return blog;
    }

    @Override
    public List<Blog> listBlogByTypeId(int id) {

        return blogMapper.selectBlogByTypeId(id);
    }

    @Override
    public List<Blog> listBlogByTagId(int id) {
        return blogMapper.selectBlogByTagId(id);
    }

    @Override
    public Map<String, List<Blog>> listBlogByTime() {
        Map<String,List<Blog>> map = new HashMap<>();
        List<String> years = blogMapper.selectTimeYList();
        years.forEach((item -> map.put(item,blogMapper.selectBlogByTimeY(item))));
        return map;
    }

    @Override
    public int getBlogCount() {
        return blogMapper.selectBlogCount();
    }

    @Override
    public List<Blog> listBlog(BlogsForm blog) {

        return blogMapper.selectBlogList(blog);
    }

    @Override
    public List<Blog> listBlog(String search) {
        return blogMapper.selectBlogListForSearch(search);
    }

    /**
     * 查询标签
     * @param blog
     */
    private void addTagToBlog(Blog blog){
        String tagIds = blog.getTagIds();
        if (tagIds.contains(",")){
            List<Tag> tag = new ArrayList<>();
            String[] split = tagIds.split(",");
            for (String s : split) {
                tag.add(tagService.getTag(Integer.parseInt(s)));
            }
            blog.setTags(tag);
        }else if (tagIds!=null){
            blog.setTags(new ArrayList<Tag>(){{
                add(tagService.getTag(Integer.parseInt(tagIds)));
            }});
        }
    }

    @Override
    public List<Blog> getAllBlogs() {
        List<Blog> blogs = blogMapper.selectAllBlog();
        //查询标签
        for (Blog blog : blogs) {
            addTagToBlog(blog);
        }
        
        return blogs;
    }

    @Override
    public int saveBlog(Blog blog) {

        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        blog.setViews(0);

        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        return blogMapper.insertBlog(blog);
    }

    @Override
    public int updateBlog(int id,Blog blog) {
        Blog b = blogMapper.selectBlogById(id);
        if (b == null){
            throw new NotFoundException("博客不存在");
        }

        blog.setUser(b.getUser());
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));

        blog.setCreateTime(b.getCreateTime());
        blog.setViews(b.getViews());
        blog.setUpdateTime(new Date());
        BeanUtils.copyProperties(blog,b);
        return blogMapper.updateBlog(b);
    }

    @Override
    public int deleteBlog(int id) {
        return blogMapper.deleteBlog(id);
    }

    @Override
    public List<Blog> getBlogIsRecommend() {
        List<Blog> blogs = blogMapper.selectBlogIsRecommend(true);
        blogs.sort((i1,i2) -> i2.getId() - i1.getId());
        return blogs;
    }
}
