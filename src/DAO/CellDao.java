package DAO;

import java.util.List;

public interface CellDao<T> {

    T get(int id);

    List<T> getAllByArea(int areaId);

    int save(T t);

    void update(T t);

    void delete(T t);
}
