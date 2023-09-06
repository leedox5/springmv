package kr.leedox.service;

import kr.leedox.entity.Role;
import kr.leedox.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @InjectMocks
    RoleService roleService;

    @Mock
    RoleRepository roleRepository;

    @Test
    void getAllRolesTest() {
        given(roleRepository.findAll()).willReturn(getStubRoles());

        List<Role> roles = roleService.getAllRoles();

        assertEquals(2, roles.size());
    }
    private List<Role> getStubRoles() {
        Role role1 = Role.builder().name("ROLE_ADMIN").build();
        Role role2 = Role.builder().name("ROLE_USER").build();
        return List.of(role1, role2);
    }

}
