package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void add(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findAll().stream()
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}