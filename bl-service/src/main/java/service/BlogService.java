package service;

import entity.Blog;
import entityUtil.BlogsForm;

import java.util.List;
import java.util.Map;

/**
 * @author songbin
 * @date 2020/10/9
 */
public interface BlogService {

    Blog getBlog(int id);

    List<Blog> listBlog(BlogsForm blog);

    List<Blog> listBlog(String search);

    List<Blog> getAllBlogs();

    int saveBlog(Blog blog);

    int updateBlog(int id,Blog blog);

    int deleteBlog(int id);

    List<Blog> getBlogIsRecommend();

    Blog getBlogAndConvert(int id);

    List<Blog> listBlogByTypeId(int id);

    List<Blog> listBlogByTagId(int id);

    Map<String, List<Blog>> listBlogByTime();

    int getBlogCount();
}
