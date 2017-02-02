package sbs.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericRepositoryAdapter<E, K extends Serializable> implements GenericRepository<E, K> {

	@Autowired
    private SessionFactory sessionFactory;
     
    protected Class<? extends E> daoType;
     
    /**
    * By defining this class as abstract, we prevent Spring from creating 
    * instance of this class If not defined as abstract, 
    * getClass().getGenericSuperClass() would return Object. There would be 
    * exception because Object class does not hava constructor with parameters.
    */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericRepositoryAdapter() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        daoType = (Class) pt.getActualTypeArguments()[0];
    }
     
    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
     
    @Override
    public void save(E entity) {
        currentSession().save(entity);
    }
    @Override
    public void update(E entity) {
    	currentSession().update(entity);
    }
     
    @Override
    public void saveOrUpdate(E entity) {
        currentSession().saveOrUpdate(entity);
    }
     
    @Override
    public void remove(E entity) {
        currentSession().delete(entity);
    }
     
    @Override
    public E findById(K key) {
        return (E) currentSession().get(daoType, key);
    }
     
    @SuppressWarnings("unchecked")
	@Override
    public List<E> findAll() {
        return currentSession().createCriteria(daoType).list();
    }
	
}
