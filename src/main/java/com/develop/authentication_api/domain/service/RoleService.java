package com.develop.authentication_api.domain.service;

import com.develop.authentication_api.domain.entity.Role;
import com.develop.authentication_api.domain.model.RoleModel;
import com.develop.authentication_api.domain.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public List<Role> addRoleList(List<Long> ids) {
        List<Role> roles = roleRepository.findAllById(ids);
        validateRoleList(roles, ids);
        return roles;
    }

    public Role save(RoleModel roleModel) {
        validateRole(roleModel.getName());
        Role role = new Role();
        role.setName(roleModel.getName());
        return roleRepository.save(role);
    }

    void validateRoleList(List<Role> roles, List<Long> ids) {
        if (!ids.stream().allMatch(id -> roles.stream().anyMatch(role -> role.getId().equals(id)))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some role elements does not exists!");
        }
    }

    private void validateRole(String name) {
        if (roleRepository.findRoleByName(name).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, MessageFormat.format("Role {0} already exists!", name));
        }
    }

    public Role getRole(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role does not exists!");
        }
        return role.get();
    }
}
