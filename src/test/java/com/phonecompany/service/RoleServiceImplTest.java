package com.phonecompany.service;

import com.phonecompany.dao.interfaces.RoleDao;
import com.phonecompany.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleServiceImplTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnRoleById() {
        String name = "Admin";
        long id = 1L;
        Role role = createRole(id, name);

        when(roleDao.getById(id)).thenReturn(role);

        role = roleService.getById(id);

        assertThat(id, equalTo(role.getId()));
        assertThat(name, equalTo(role.getName()));
    }

    @Test
    public void shouldUpdateRole() {
        String name = "Admin";
        long id = 1L;
        Role role = createRole(id, name);

        roleService.update(role);

        verify(roleDao, atLeastOnce()).update(role);
    }

    @Test
    public void shouldDeleteRole() {
        String name = "Admin";
        long id = 1L;
        Role role = createRole(id, name);

        roleService.delete(role.getId());

        verify(roleDao, atLeastOnce()).delete(role.getId());
    }

    @Test
    public void shouldSaveRole() {
        String name = "Admin";
        long id = 1L;
        Role role = createRole(id, name);

        roleService.save(role);

        verify(roleDao, atLeastOnce()).save(role);
    }

    @Test
    public void shouldGetAllRoles() {
        String name1 = "Admin";
        long id1 = 1L;
        Role role1 = createRole(id1, name1);

        String name2 = "CSR";
        long id2 = 2L;
        Role role2 = createRole(id2, name2);

        when(roleDao.getAll()).thenReturn(Arrays.asList(role1, role2));

        roleService.getAll();

        assertThat(id1, equalTo(role1.getId()));
        assertThat(name1, equalTo(role1.getName()));

        assertThat(id2, equalTo(role2.getId()));
        assertThat(name2, equalTo(role2.getName()));

    }


    private Role createRole(Long id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }

}
