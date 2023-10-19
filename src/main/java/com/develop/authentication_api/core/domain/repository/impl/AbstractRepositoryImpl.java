package com.develop.authentication_api.core.domain.repository.impl;

import com.develop.authentication_api.core.domain.repository.AbstractRepository;
import com.develop.authentication_api.core.domain.entity.AbstractEntity;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class AbstractRepositoryImpl<T extends AbstractEntity> extends SimpleJpaRepository<T, Long> implements AbstractRepository<T> {

    public AbstractRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public AbstractRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    @Override
    public List<T> findAllEnabled() {
        return super.findAll().stream().filter(AbstractEntity::getEnabled).collect(Collectors.toList());
    }
}
