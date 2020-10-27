package mapper;

import entity.Blog;
import entityUtil.BlogsForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author songbin
 * @date 2020/10/8
 */
@Mapper
public interface BlogMapper {
    /**
     * 根据所给信息查询博客信息
     * @return
     */
    List<Blog> selectBlogList(BlogsForm blog);

    List<Blog> selectBlogListForSearch(String search);

    List<Blog> selectAllBlog();

    Blog selectBlogById(int id);

    int insertBlog(Blog blog);

    int updateBlog(Blog blog);

    int deleteBlog(int id);

    List<Blog> selectBlogByTagId(int id);

    List<Blog> selectBlogByTypeId(int id);

    List<Blog> selectBlogIsRecommend(boolean flag);

    List<String> selectTimeYList();

    List<Blog> selectBlogByTimeY(String year);

    int selectBlogCount();
}
