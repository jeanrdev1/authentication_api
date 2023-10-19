package com.develop.authentication_api.core.domain.service;

import com.develop.authentication_api.core.domain.entity.AbstractEntity;

import java.util.List;

public interface AbstractService<T extends AbstractEntity> {

    List<T> findAll();
    T findById(Long id);
    List<T> findAllEnabled();
    T save(T entity);
    T update(T entity);
    T updateStatus(Long id);
    void delete(Long id);
}
