package com.phonecompany.service;

import com.phonecompany.dao.interfaces.UserDao;
import com.phonecompany.exception.KeyAlreadyPresentException;
import com.phonecompany.model.User;
import com.phonecompany.model.enums.UserRole;
import com.phonecompany.model.events.OnUserCreationEvent;
import com.phonecompany.service.email.PasswordAssignmentEmailCreator;
import com.phonecompany.service.email.ResetPasswordEmailCreator;
import com.phonecompany.service.interfaces.EmailService;
import com.phonecompany.service.interfaces.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static com.phonecompany.model.enums.Status.ACTIVATED;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceImpl.class)
public class UserServiceImplTest {

    public static final String EXAMPLE_EMAIL = "email@gmail.com";
    public static final String EXAMPLE_PASSWORD = "$root$";
    public static final String ENCODED_PASSWORD = "encoded_pass";
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @MockBean
    private UserDao userDao;
    @MockBean
    private ShaPasswordEncoder shaPasswordEncoder;
    @MockBean
    private ResetPasswordEmailCreator resetPassMessageCreator;
    @MockBean
    private PasswordAssignmentEmailCreator passwordAssignmentCreator;
    @MockBean
    private EmailService<User> emailService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Autowired
    private UserService userService;

    private User user;
    private OnUserCreationEvent userCreationEvent;

    @Before
    public void setUp() {
        user = new User(EXAMPLE_EMAIL, EXAMPLE_PASSWORD, UserRole.CLIENT, null);
        userCreationEvent = new OnUserCreationEvent(user);
    }

    @Test
    public void shouldThrowExceptionOnExistingEmail() {
        //given
        when(userDao.getCountByEmail(anyString()))
                .thenReturn(1);
        thrown.expect(KeyAlreadyPresentException.class);
        thrown.expectMessage("User associated with " + EXAMPLE_EMAIL
                + " already exists");

        //when
        userService.save(user);

        //then
        verify(userDao, never()).save(any());
    }

    @Test
    public void shouldSaveUser() {
        //given
        when(userDao.getCountByEmail(anyString()))
                .thenReturn(0);
        when(shaPasswordEncoder.encodePassword(user.getPassword(), null))
                .thenReturn(ENCODED_PASSWORD);
        when(userDao.save(user))
                .then(returnsFirstArg());

        //when
        String initialPassword = user.getPassword();
        User savedUser = userService.save(user);

        //then
        verify(shaPasswordEncoder, times(1))
                .encodePassword(any(), any());

        assertThat(savedUser.getStatus(), is(ACTIVATED));
        assertThat(initialPassword, is(not(savedUser.getPassword())));
        assertThat(savedUser.getPassword(), is("encoded_pass"));
    }

    @Test
    public void shouldSendConfirmationEmail() {
        //given
        SimpleMailMessage confirmationMessage = new SimpleMailMessage();
        when(passwordAssignmentCreator.constructMessage(user))
                .thenReturn(confirmationMessage);

        //when
        userService.sendConfirmationEmail(userCreationEvent);

        //then
        verify(passwordAssignmentCreator, times(1))
                .constructMessage(userCaptor.capture());
        verify(emailService, times(1))
                .sendMail(confirmationMessage, user);

        assertThat(userCaptor.getValue().getEmail(), is(EXAMPLE_EMAIL));
        assertThat(userCaptor.getValue().getPassword(), is(EXAMPLE_PASSWORD));
    }

    @Test
    public void shouldFindByEmail() {
        //given
        when(userDao.findByEmail(EXAMPLE_EMAIL))
                .thenReturn(user);
        //when
        User userFoundByEmail = userService.findByEmail(EXAMPLE_EMAIL);

        //then
        verify(userDao, times(1)).findByEmail(EXAMPLE_EMAIL);

        assertThat(userFoundByEmail.getRole(), is(UserRole.CLIENT));
        assertThat(userFoundByEmail.getPassword(), is(EXAMPLE_PASSWORD));
        assertThat(userFoundByEmail.getEmail(), is(EXAMPLE_EMAIL));
    }
}