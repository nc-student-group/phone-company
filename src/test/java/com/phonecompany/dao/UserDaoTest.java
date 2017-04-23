package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class UserDaoTest extends AbstractTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void shouldGetById() {
        //given
        ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();
        String encodedRoot = shaPasswordEncoder
                .encodePassword("root", null);

        //then
        User user = userDao.getById(1L);

        assertThat(user.getEmail(), is("admin@gmail.com"));
        assertTrue(encodedRoot.equals(user.getPassword()));
        assertThat(user.getRole(), is(UserRole.ADMIN));
        assertThat(user.getStatus(), is(Status.ACTIVATED));
    }
}
