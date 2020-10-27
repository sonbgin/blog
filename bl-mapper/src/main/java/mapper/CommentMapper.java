package mapper;

import entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author songbin
 * @date 2020/10/17
 */
@Mapper
public interface CommentMapper {
    /**
     * 查询所有评论信息
     * @return
     */
    List<Comment> selectAllCommentsByBlogIdParNull(int blogId);

    List<Comment> selectAllCommentsByBlogIdAndParId(@Param("blogId") int blogId,@Param("parentId") int parentId);

    int insertNewComment(Comment comment);

    Comment selectCommentById(int id);
}
