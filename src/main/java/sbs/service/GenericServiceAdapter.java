package sbs.service;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sbs.repository.GenericRepository;

@Service
public class GenericServiceAdapter<E, K> implements GenericService<E, K> {

    private GenericRepository<E, K> genericRepository;
    
    public GenericServiceAdapter(GenericRepository<E,K> genericRepository) {
        this.genericRepository=genericRepository;
    }
 
    public GenericServiceAdapter() {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(E entity) {
        genericRepository.save(entity);
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(E entity) {
        genericRepository.update(entity);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdate(E entity) {
        genericRepository.saveOrUpdate(entity);
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<E> findAll() {
        return genericRepository.findAll();
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public E findById(K id) {
        return genericRepository.findById(id);
    }
 
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void remove(E entity) {
        genericRepository.remove(entity);
    }
    

}