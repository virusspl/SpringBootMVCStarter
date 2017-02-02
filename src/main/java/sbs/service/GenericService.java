package sbs.service;

import java.util.List;

public interface GenericService<E, K> {
    public void save(E entity);
    public void update(E entity);
    public void saveOrUpdate(E entity);
    public void remove(E entity);
    public E findById(K id);
    public List<E> findAll();

}