package com.develop.authentication_api.core.domain.controller;

import com.develop.authentication_api.core.domain.entity.AbstractEntity;
import com.develop.authentication_api.core.domain.service.AbstractService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class AbstractController<T extends AbstractEntity> {

    private final AbstractService<T> service;

    public AbstractController(final AbstractService<T> service) {
        this.service = service;
    }

    @GetMapping("find-all")
    public List<T> findAll() {
        return service.findAll();
    }

    @GetMapping("find-by-id/{id}")
    public T findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("find-all-enabled")
    public List<T> findAllEnabled() {
        return service.findAllEnabled();
    }

    @PostMapping("save")
    public T save(@RequestBody T entity) {
        return service.save(entity);
    }

    @PutMapping("update")
    public T update(@RequestBody T entity) {
        return service.update(entity);
    }

    @PatchMapping("update-status/{id}")
    public T updateStatus(@PathVariable Long id) {
        return service.updateStatus(id);
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
