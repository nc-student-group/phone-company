package com.phonecompany.service;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnUserById() {
        String email = "example@domain.com";
        long id = 1L;
        String firstName = "John";
        String lastName = "Smith";
        User user = createUser(id, email, firstName, lastName);

        when(userDao.getById(id)).thenReturn(user);

        user = userService.getById(id);

        assertThat(id, equalTo(user.getId()));
        assertThat(email, equalTo(user.getEmail()));
        assertThat(firstName, equalTo(user.getFirstName()));
        assertThat(lastName, equalTo(user.getLastName()));
    }

    @Test
    public void shouldReturnUserByUsername() {
        String email = "example@domain.com";
        long id = 1L;
        String firstName = "John";
        String lastName = "Smith";
        User user = createUser(id, email, firstName, lastName);

        when(userDao.findByUsername(email)).thenReturn(user);

        user = userService.findByUsername(email);

        assertThat(id, equalTo(user.getId()));
        assertThat(email, equalTo(user.getEmail()));
        assertThat(firstName, equalTo(user.getFirstName()));
        assertThat(lastName, equalTo(user.getLastName()));
    }

    @Test
    public void shouldUpdateUser() {
        String email = "example@domain.com";
        long id = 1L;
        String firstName = "John";
        String lastName = "Smith";
        User user = createUser(id, email, firstName, lastName);

        userService.update(user);

        verify(userDao, atLeastOnce()).update(user);
    }

    @Test
    public void shouldDeleteUser() {
        String email = "example@domain.com";
        long id = 1L;
        String firstName = "John";
        String lastName = "Smith";
        User user = createUser(id, email, firstName, lastName);

        userService.delete(user.getId());

        verify(userDao, atLeastOnce()).delete(user.getId());
    }

    @Test
    public void shouldSaveUser() {
        String email = "example@domain.com";
        long id = 1L;
        String firstName = "John";
        String lastName = "Smith";
        User user = createUser(id, email, firstName, lastName);

        userService.save(user);

        verify(userDao, atLeastOnce()).save(user);
    }

    @Test
    public void shouldGetAllUsers() {
        String email1 = "example1@domain.com";
        long id1 = 1L;
        String firstName1 = "John";
        String lastName1 = "Smith";
        User user1 = createUser(id1, email1, firstName1, lastName1);

        String email2 = "example2@domain.com";
        long id2 = 2L;
        String firstName2 = "Tom";
        String lastName2 = "Black";
        User user2 = createUser(id2, email2, firstName2, lastName2);

        when(userDao.getAll()).thenReturn(Arrays.asList(user1, user2));

        userService.getAll();

        assertThat(id1, equalTo(user1.getId()));
        assertThat(email1, equalTo(user1.getEmail()));
        assertThat(firstName1, equalTo(user1.getFirstName()));
        assertThat(lastName1, equalTo(user1.getLastName()));

        assertThat(id2, equalTo(user2.getId()));
        assertThat(email2, equalTo(user2.getEmail()));
        assertThat(firstName2, equalTo(user2.getFirstName()));
        assertThat(lastName2, equalTo(user2.getLastName()));

    }

    @Test
    public void shouldResetPassword() {
        String email = "example@domain.com";
        long id = 1L;
        String firstName = "John";
        String lastName = "Smith";
        String password = "password";
        User user = createUser(id, email, firstName, lastName);
        user.setPassword(password);

        userService.resetPassword(user);

        verify(userDao, atLeastOnce()).update(user);
        assertThat(user.getPassword(), notNullValue());
        assertNotEquals(password, user.getPassword());

        //TODO: verify for email sending when email service will be added

    }

    private User createUser(Long id, String email, String firstName, String lastName) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

}