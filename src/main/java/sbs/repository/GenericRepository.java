package sbs.repository;
// http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
import java.util.List;

public interface GenericRepository<E, K>  {
    public void save(E entity) ;
    public void update(E entity);
    public void saveOrUpdate(E entity);
    public void remove(E entity);
    public E findById(K key);
    public List<E> findAll() ;
}
