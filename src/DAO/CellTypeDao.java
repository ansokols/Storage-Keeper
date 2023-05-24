package DAO;

import java.util.List;

public interface CellTypeDao<T> {
    T get(int cellId, int typeId);

    List<T> getAllByCell(int cellId);

    List<T> getAllByType(int typeId);

    int save(T t);

    void update(T t);

    void delete(T t);
}
