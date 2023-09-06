package kr.leedox.service;

import kr.leedox.entity.Role;
import kr.leedox.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();

        roles = roleRepository.findAll();

        if(roles.size() < 4) {
            initRoles("ROLE_ADMIN");
            initRoles("ROLE_GOLD");
            initRoles("ROLE_SILVER");
            initRoles("ROLE_USER");
        }

        return roleRepository.findAll();
    }

    private void initRoles(String name) {
        List<Role> roles = roleRepository.findByName(name);
        if(roles.isEmpty()) {
            Role role = Role.builder().name(name).build();
            roleRepository.save(role);
        }
    }
}
