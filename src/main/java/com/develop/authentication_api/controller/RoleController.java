package com.develop.authentication_api.controller;

import com.develop.authentication_api.domain.entity.Role;
import com.develop.authentication_api.domain.model.RoleModel;
import com.develop.authentication_api.domain.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(RoleController.PATH)
public class RoleController {

    public static final String PATH = "role";

    private final RoleService roleService;

    @GetMapping
    public List<Role> findAll() {
        return roleService.findAll();
    }

    @PostMapping
    public Role save(@RequestBody RoleModel roleModel) {
        return roleService.save(roleModel);
    }
}
