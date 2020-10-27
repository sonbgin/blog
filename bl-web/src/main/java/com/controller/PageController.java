package com.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.BlogService;
import service.CommentService;
import service.TagService;
import service.TypeService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songbin
 * @date 2020/9/29
 */
@Controller
@RequestMapping("/ordinary")
public class PageController {

    @Resource
    private BlogService blogService;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;

    /**
     * 跳转到主页面
     * @return
     */
    @RequestMapping({"/toIndex","/"})
    public String toIndex(@RequestParam(defaultValue = "1") int pageNum,
                          @RequestParam(defaultValue = "5") int pageSize, Model model){
        model.addAttribute("tags",tagService.listTag());
        List<Type> types = typeService.listType();
        model.addAttribute("types",types);
        //blog进行分页操作
        PageHelper.startPage(pageNum,pageSize);
        List<Blog> blogs = blogService.getAllBlogs();
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("blogs",pageInfo);

        model.addAttribute("isRecommend",blogService.getBlogIsRecommend());
        return "block/index";
    }

    @PostMapping("/toSearch")
    public String toSearch(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "5") int pageSize,@RequestParam String search, Model model){
        PageHelper.startPage(pageNum,pageSize);
        List<Blog> blogs = blogService.listBlog(search);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("blogs",pageInfo);
        return "block/search";
    }

    /**
     * 跳转到博客详情页面
     * @return
     */
    @RequestMapping("/toBlog/{id}")
    public String toBlog(@PathVariable int id, Model model){
        model.addAttribute("blog",blogService.getBlogAndConvert(id));
        return "block/blog";
    }

    /**
     * 跳转到标签页面
     * @return
     */
    @RequestMapping("/toTag")
    public String toTag(@RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "5") int pageSize, Model model){

        model.addAttribute("tags",tagService.listTag());
        //blog进行分页操作
        PageHelper.startPage(pageNum,pageSize);
        List<Blog> blogs = blogService.getAllBlogs();
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("blogs",pageInfo);
        return "block/tag";
    }

    /**
     * 跳转到分类页面
     * @return
     */
    @RequestMapping("/toType")
    public String toType(@RequestParam(defaultValue = "1") int pageNum,
                         @RequestParam(defaultValue = "5") int pageSize, Model model){

        model.addAttribute("types",typeService.listType());
        //blog进行分页操作
        PageHelper.startPage(pageNum,pageSize);
        List<Blog> blogs = blogService.getAllBlogs();
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("blogs",pageInfo);
        return "block/type";
    }

    @GetMapping("/type")
    public String type(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize, Model model,HttpSession session){
        List<Type> types = typeService.listType();
        model.addAttribute("types",types);
        PageHelper.startPage(pageNum,pageSize);
        List<Blog> blogs = new ArrayList<>();
        for (Type type : types) {
            if (type.getId() == (int)session.getAttribute("id")){
                blogs = type.getBlogs();
                model.addAttribute("curtype",type);
            }
        }
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("blogs",pageInfo);

        return "block/type";
    }

    @GetMapping("/type/{id}")
    public String type2(@PathVariable int id,HttpSession session){
        session.setAttribute("id",id);
        return "redirect:/ordinary/type";
    }

    @GetMapping("/tag")
    public String tag(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "5") int pageSize, Model model,HttpSession session){
        List<Tag> tags = tagService.listTag();
        model.addAttribute("tags",tags);
        PageHelper.startPage(pageNum,pageSize);
        List<Blog> blogs = new ArrayList<>();
        for (Tag tag : tags) {
            if (tag.getId() == (int)session.getAttribute("id")){
                blogs = tag.getBlogs();
            }
        }
        PageInfo<Blog> pageInfo = new PageInfo<>(blogs);
        model.addAttribute("blogs",pageInfo);

        return "block/tag";
    }

    @GetMapping("/tag/{id}")
    public String tag2(@PathVariable int id,HttpSession session){
        session.setAttribute("id",id);
        return "redirect:/ordinary/tag";
    }

    /**
     * 跳转到关于我页面
     * @return
     */
    @RequestMapping("/toAbout")
    public String toAboutMe(){
        return "block/about";
    }

    /**
     * 跳转到归档页面
     * @return
     */
    @RequestMapping("/toArchive")
    public String toArchive(Model model){
        model.addAttribute("map",blogService.listBlogByTime());
        model.addAttribute("blogCount",blogService.getBlogCount());
        return "block/archive";
    }

    //Comment 处理
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable int blogId,Model model){
        Blog blog = blogService.getBlogAndConvert(blogId);
        model.addAttribute("blog",blog);
        return "block/blog";
    }

    /**
     * 评论处理
     * @param comment
     * @param session
     * @return
     */
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        if (user != null){
            comment.setFlag(true);
            comment.setAvatar(user.getAvatar());
        }else {
            comment.setAvatar("/img/per.jpg");
        }
        commentService.saveComment(comment);
        model.addAttribute("comments",commentService.listComment(comment.getBlog().getId()));
        return "block/blog::comment-user";
    }

    @RequestMapping("/footer")
    public String toFoot(Model model){
        List<Blog> blogs = blogService.getBlogIsRecommend();

        model.addAttribute("recommendBlog",blogs);
        return "_fragments::footer_blog";
    }
}
