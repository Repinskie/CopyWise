package com.repinsky.copywise.services;

import com.repinsky.copywise.models.Role;
import com.repinsky.copywise.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByTitle("ROLE_USER").orElseThrow();
    }
}
