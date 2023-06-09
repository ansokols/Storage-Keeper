package DAO;

import java.util.List;

public interface ShipmentMaterialDao<T> {

    T get(int id);

    List<T> getAllByShipment(int id);

    int save(T t);

    void update(T t);

    void delete(T t);
}
