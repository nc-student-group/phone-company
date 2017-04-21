package com.phonecompany.dao;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional //will be replaced (it is just for 2 days)
@Rollback
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void shouldPersistUser() {
        //given
        User userToBeSaved = new User("test@mail.com", "pass1",
                UserRole.CLIENT, Status.INACTIVE);

        //when
        User persistedUser = userDao.save(userToBeSaved);

        //then
        User user = userDao.getById(persistedUser.getId());

        assertThat(user, is(notNullValue()));
        assertThat(user.getEmail(), is(userToBeSaved.getEmail()));
        assertThat(user.getPassword(), is(userToBeSaved.getPassword()));
        assertThat(user.getRole(), is(userToBeSaved.getRole()));
        assertThat(user.getStatus(), is(userToBeSaved.getStatus()));
    }

    @Test
    public void shouldGetById() {
        //given


        //when

        //then
    }
}
