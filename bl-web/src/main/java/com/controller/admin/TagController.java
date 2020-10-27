package com.controller.admin;

import entity.Tag;
import entity.Type;
import service.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author songbin
 * @date 2020/10/11
 */
@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private TagService tagService;

    /**
     * 跳转到标签管理
     * @return
     */
    @GetMapping("/toTags")
    public String toTags(@RequestParam(defaultValue = "1") int pageNum,
                         @RequestParam(defaultValue = "5") int pageSize, Model model){

        PageHelper.startPage(pageNum,pageSize);
        List<Tag> list = tagService.listTag();
        PageInfo<Tag> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        return "block/admin/tags";
    }

    /**
     * 跳转到标签新增
     * @return
     */
    @GetMapping("/toTagsIn")
    public String toTagsIn(Model model){
        model.addAttribute("tag",new Type());
        return "block/admin/tag-input";
    }

    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Type());
        return "block/admin/tag-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable("id") int id, Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "block/admin/tag-input";
    }

    /**
     * 新增处理
     * @param tag
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){

        Tag t = tagService.getTagByName(tag.getName());
        if (t!=null){
            result.rejectValue("name","nameError","不能添加重复的标签");
        }

        if (result.hasErrors()){
            return "block/admin/tag-input";
        }
        int i = tagService.saveTag(tag);
        if (i > 0){
            attributes.addFlashAttribute("msg","新增成功");
        }else {
            attributes.addFlashAttribute("msg","新增失败");
        }
        return "redirect:/admin/toTags";
    }

    /**
     * 编辑修改处理
     * @param tag
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String post(@Valid Tag tag, BindingResult result,@PathVariable int id, RedirectAttributes attributes) {
        Tag t = tagService.getTagByName(tag.getName());
        if (t != null){
            result.rejectValue("name","nameError","不能添加重复的标签");
        }

        if (result.hasErrors()){
            return "block/admin/tag-input";
        }
        int i = tagService.update(id,tag);
        if (i > 0){
            attributes.addFlashAttribute("msg","更新成功");
        }else {
            attributes.addFlashAttribute("msg","更新失败");
        }
        return "redirect:/admin/toTags";
    }

    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable("id") int id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addAttribute("msg","删除成功");
        return "redirect:/admin/toTags";
    }
}
