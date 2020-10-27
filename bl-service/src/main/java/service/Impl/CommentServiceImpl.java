package service.Impl;

import entity.Comment;
import mapper.CommentMapper;
import service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author songbin
 * @date 2020/10/17
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<Comment> listComment(int blogId) {
        List<Comment> comments = commentMapper.selectAllCommentsByBlogIdParNull(blogId);
        for (Comment comment : comments) {
            List<Comment> list = getReplyComment(blogId, comment);
            comment.setChildComment(list);
        }
        return comments;
    }

    //获取回复评论
    private List<Comment> getReplyComment(int blogId,Comment comment){
        List<Comment> allList = null;

        List<Comment> comments = commentMapper.selectAllCommentsByBlogIdAndParId(blogId, comment.getId());
        if (comments.size() > 0){
            allList = new ArrayList<>();
            getAllReply(blogId,comment,allList,comments);
        }

        return allList;
    }

    private void getAllReply(int blogId,Comment parComment,List<Comment> allList,List<Comment> comments){
        if (comments.size() > 0){
            for (Comment comment : comments) {
                allList.add(comment);
                Comment c = new Comment();
                BeanUtils.copyProperties(parComment,c);
                c.setChildComment(null);
                comment.setParentComment(c);
                List<Comment> list1 = commentMapper.selectAllCommentsByBlogIdAndParId(blogId, comment.getId());
                getAllReply(blogId,comment,allList,list1);
            }
        }else {
            return;
        }
    }

    @Override
    public int saveComment(Comment comment) {
        int parentCommentId = comment.getParentComment().getId();
        if (parentCommentId == -1){
            comment.setParentComment(null);
        }else {
            comment.setParentComment(commentMapper.selectCommentById(parentCommentId));
        }
        comment.setCreateTime(new Date());
        return commentMapper.insertNewComment(comment);
    }
}
