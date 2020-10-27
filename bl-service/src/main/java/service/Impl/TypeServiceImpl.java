package service.Impl;

import entity.Type;
import exceoption.NotFoundException;
import mapper.TypeMapper;
import service.BlogService;
import service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author songbin
 * @date 2020/10/9
 */
@Service
public class TypeServiceImpl implements TypeService {

    @Resource
    private TypeMapper typeMapper;
    @Resource
    private BlogService blogService;

    @Transactional
    @Override
    public int saveType(Type type) {
        return typeMapper.insertType(type);
    }

    @Transactional
    @Override
    public Type getType(int id) {
        return typeMapper.selectType(id);
    }

    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeMapper.selectTypeByName(name);
    }

    @Transactional
    @Override
    public List<entity.Type> listType() {
        List<Type> list = typeMapper.selectAllTypes();
        for (Type type : list) {
            type.setBlogs(blogService.listBlogByTypeId(type.getId()));
        }
        list.sort((t1,t2)->t2.getBlogs().size()-t1.getBlogs().size());
        return list;
    }

    @Transactional
    @Override
    public int update(int id, Type type) {
        Type t = typeMapper.selectType(id);
        if (t==null){
            throw new NotFoundException("类型不存在");
        }

        BeanUtils.copyProperties(type,t);
        return typeMapper.updateType(t);
    }

    @Transactional
    @Override
    public void deleteType(int id) {
        typeMapper.deleteType(id);
    }
}
