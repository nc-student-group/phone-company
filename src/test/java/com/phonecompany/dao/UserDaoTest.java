package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserDaoTest {

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

        assertThat(user.getEmail(), is("phcompadm@gmail.com"));
        assertTrue(encodedRoot.equals(user.getPassword()));
        assertThat(user.getRole(), is(UserRole.ADMIN));
        assertThat(user.getStatus(), is(Status.ACTIVATED));
    }
}
