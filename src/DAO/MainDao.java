package DAO;

import java.util.List;

public interface MainDao<T> {

    T get(int id);

    List<T> getAll();

    int save(T t);

    void update(T t);

    void delete(T t);
}
