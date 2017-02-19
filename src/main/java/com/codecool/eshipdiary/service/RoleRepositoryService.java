package com.codecool.eshipdiary.service;

import com.codecool.eshipdiary.model.Role;
import com.codecool.eshipdiary.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleRepositoryService {

    @Autowired
    RoleRepository roleRepository;

    public void save(Role role) {
        roleRepository.save(role);
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
