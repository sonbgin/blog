package service;

import entity.Type;

import java.util.List;

/**
 * @author songbin
 * @date 2020/10/9
 */
public interface TypeService {

    int saveType(Type type);

    Type getType(int id);

    Type getTypeByName(String name);

    List<entity.Type> listType();

    int update(int id,Type type);

    void deleteType(int id);
}
