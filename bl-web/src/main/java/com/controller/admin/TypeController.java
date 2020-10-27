package com.controller.admin;

import entity.Type;
import service.TypeService;
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
 * @date 2020/10/9
 */
@Controller
@RequestMapping("/admin")
public class TypeController {
    @Resource
    private TypeService typeService;
    /**
     * 跳转到分类管理
     * @return
     */
    @GetMapping("/toTypes")
    public String toTypes(@RequestParam(defaultValue = "1") int pageNum,
                          @RequestParam(defaultValue = "5") int pageSize,Model model){

        PageHelper.startPage(pageNum,pageSize);
        List<Type> list = typeService.listType();
        PageInfo<Type> pageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);

        return "block/admin/types";
    }

    /**
     * 跳转到分类新增
     * @return
     */
    @GetMapping("/toTypesIn")
    public String toTypesIn(Model model){
        model.addAttribute("type",new Type());
        return "block/admin/type-input";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "block/admin/type-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable("id") int id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "block/admin/type-input";
    }

    /**
     * 新增处理
     * @param type
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        Type t = typeService.getTypeByName(type.getName());
        if (t != null){
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        if (result.hasErrors()){
            return "block/admin/type-input";
        }
        int i = typeService.saveType(type);
        if (i > 0){
            attributes.addFlashAttribute("msg","新增成功");
        }else {
            attributes.addFlashAttribute("msg","新增失败");
        }
        return "redirect:/admin/toTypes";
    }

    /**
     * 编辑修改处理
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,@PathVariable("id") int id, RedirectAttributes attributes){
        Type t = typeService.getTypeByName(type.getName());
        if (t != null){
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        if (result.hasErrors()){
            return "block/admin/type-input";
        }
        int i = typeService.update(id,type);
        if (i > 0){
            attributes.addFlashAttribute("msg","更新成功");
        }else {
            attributes.addFlashAttribute("msg","更新失败");
        }
        return "redirect:/admin/toTypes";
    }

    /**
     * 删除分类
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable("id") int id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addAttribute("msg","删除成功");
        return "redirect:/admin/toTypes";
    }
}
