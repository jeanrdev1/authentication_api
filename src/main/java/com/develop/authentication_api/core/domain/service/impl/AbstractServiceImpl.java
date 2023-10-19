package com.develop.authentication_api.core.domain.service.impl;

import com.develop.authentication_api.core.domain.entity.AbstractEntity;
import com.develop.authentication_api.core.domain.repository.AbstractRepository;
import com.develop.authentication_api.core.domain.service.AbstractService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AbstractServiceImpl<T extends AbstractEntity> implements AbstractService<T> {

    protected AbstractRepository<T> repository;

    public AbstractServiceImpl(final AbstractRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(Long id) {
        Optional<T> entity = repository.findById(id);
        return entity.orElse(null);
    }

    @Override
    public List<T> findAllEnabled() {
        return repository.findAllEnabled();
    }

    @Override
    public T save(T entity) {
        entity.setEnabled(true);
        entity.setCreation(LocalDateTime.now());
        return repository.save(entity);
    }

    @Override
    public T update(T entity) {
        return repository.save(entity);
    }

    @Override
    public T updateStatus(Long id) {
        T entity = repository.getById(id);
        entity.updateStatus();
        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
