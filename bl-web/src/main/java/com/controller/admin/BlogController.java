package com.controller.admin;

import entity.Blog;
import entity.User;
import entityUtil.BlogsForm;
import service.BlogService;
import service.TagService;
import service.TypeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author songbin
 * @date 2020/10/12
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Resource
    private BlogService blogService;
    @Resource
    private TypeService typeService;
    @Resource
    private TagService tagService;

    /**
     * blog显示
     * @param pageNum
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/toBlogs")
    public String toBlogs(@RequestParam(defaultValue = "1") int pageNum,
                          @RequestParam(defaultValue = "5") int pageSize,Model model,HttpSession session){
        PageHelper.startPage(pageNum,pageSize);
        if (session.getAttribute("blogs")==null){   //非查询显示
            model.addAttribute("blogs",blogService.getAllBlogs());
            if (model.getAttribute("findNothing")!=null){
                model.addAttribute("findNothing",null);
            }
        }else if (((List<Blog>)session.getAttribute("blogs")).size()==0){  //查询结果为空处理
            model.addAttribute("blogs",session.getAttribute("blogs"));
            model.addAttribute("findNothing","所查结果为空");
        }else {
            model.addAttribute("blogs",session.getAttribute("blogs"));
            session.removeAttribute("blogs");
        }

        model.addAttribute("types",typeService.listType());
        PageInfo<Blog> pageInfo = new PageInfo<>((List<Blog>) (model.getAttribute("blogs")));
        model.addAttribute("blogs",pageInfo);
        return "block/admin/blogs";
    }

    /**
     * 根据条件查询blog
     * @param title
     * @param typeId
     * @param recommend
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String blogs(@RequestParam("title") String title, String typeId,String recommend, HttpSession session){
        BlogsForm blog = new BlogsForm(title,typeId==""? -1 : Integer.parseInt(typeId),recommend==null?false:true);

        List<Blog> blogs = blogService.listBlog(blog);

        session.setAttribute("blogs",blogs);
        return "redirect:/admin/toBlogs";
    }

    /**
     * blog新增
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String toBlogsIn(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("blog",new Blog());
        return "block/admin/blog-input";
    }

    /**
     * blog编辑
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String editBlog(@PathVariable int id, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("blog",blogService.getBlog(id));
        return "block/admin/blog-input";
    }

    /**
     * 提交blog的修改
     * @param blog
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/blogsSub/{id}")
    public String post(Blog blog, @PathVariable int id, HttpSession session, RedirectAttributes attributes){
        int i = blogService.updateBlog(id,blog);
        if ( i >0 ){
            attributes.addAttribute("message","操作成功");
        }else {
            attributes.addAttribute("message","操作失败");
        }
        return "redirect:/admin/toBlogs";
    }

    /**
     * 提交新blog
     * @param blog
     * @param model
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/blogsSub")
    public String post(Blog blog, Model model, HttpSession session, RedirectAttributes attributes){
        System.out.println(blog.getTagIds());
        blog.setUser((User) session.getAttribute("user"));

        int i = blogService.saveBlog(blog);
        if ( i >0 ){
            attributes.addAttribute("message","操作成功");
        }else {
            attributes.addAttribute("message","操作失败");
        }
        return "redirect:/admin/toBlogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable int id){
        int i = blogService.deleteBlog(id);

        return "redirect:/admin/toBlogs";
    }
}
