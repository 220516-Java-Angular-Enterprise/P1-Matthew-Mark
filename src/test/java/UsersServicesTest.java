import com.revature.shoe.daos.UsersDAO;
import com.revature.shoe.dtos.requests.LoginRequest;
import com.revature.shoe.dtos.requests.NewUserRequest;
import com.revature.shoe.models.Users;

import static org.mockito.Mockito.*;

import com.revature.shoe.models.UsersRole;
import com.revature.shoe.services.UsersServices;
import com.revature.shoe.util.custom_exceptions.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

import com.revature.shoe.util.custom_exceptions.InvalidRequestException;
import com.revature.shoe.util.custom_exceptions.ResourceConflictException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
@ExtendWith(MockitoExtension.class)
public class UsersServicesTest {
    @Mock
    private UsersDAO daoMock;

    @InjectMocks
    private UsersServices userservice;

    @Spy
    Users testuser = new Users();

    @Spy
    UsersRole testrole = new UsersRole();

    @Spy
    LoginRequest testLoginReq = new LoginRequest();

    @Spy
    NewUserRequest testUserReq = new NewUserRequest();

    @Test
    void login() {
        // inactive user
        testLoginReq.setUsername("tester123");
        testLoginReq.setPassword("P@ssw0rd");
        testrole.setRoleID("1");
        testuser = new Users("99", testLoginReq.getUsername(), "", testLoginReq.getPassword(), "", "", false,testrole);

        doReturn(testuser).when(daoMock).getUsersByNamePassword("tester123", "P@ssw0rd");
        assertThrows(AuthenticationException.class, () -> userservice.login(testLoginReq));

        // invalid credentials
        doReturn(null).when(daoMock).getUsersByNamePassword("tester123", "P@ssw0rd");
        assertThrows(AuthenticationException.class, () -> userservice.login(testLoginReq));

        // login
        testrole.setRoleID("1");
        testuser = new Users("99", testLoginReq.getUsername(), "", testLoginReq.getPassword(), "", "", true,testrole);

        doReturn(testuser).when(daoMock).getUsersByNamePassword("tester123", "P@ssw0rd");
        assertEquals(testuser, userservice.login(testLoginReq));

    }

    @Test
    void register() {
        // username is null
        assertThrows(InvalidRequestException.class, ()-> userservice.register(testUserReq));

        // duplicate username
        List<Users> testUserList = new ArrayList<>();
        testuser.setUsername("tester123");
        testUserReq.setUsername("tester123");
        testUserList.add(testuser);
        doReturn(testUserList).when(daoMock).getAll();
        assertThrows(ResourceConflictException.class, ()-> userservice.register(testUserReq));

        // invalid username
        testUserReq.setUsername("tester");
        assertThrows(InvalidRequestException.class, ()-> userservice.register(testUserReq));

    }

    @Test
    public void updateUserPassword() {
        testLoginReq.setUsername("tester123");
        testLoginReq.setPassword("P@ssw0rd");
        testrole.setRoleID("1");
        testuser = new Users("99", testLoginReq.getUsername(), "", testLoginReq.getPassword(), "", "", false,testrole);

        doReturn(testuser).when(daoMock).getUserByUsername("tester123");

        // invalid password
        assertThrows(InvalidRequestException.class, () -> userservice.updateUserPassword("tester123", "P"));
        // invalid username
        assertThrows(InvalidRequestException.class, () -> userservice.updateUserPassword("",""));
        // username/pw null
        assertThrows(InvalidRequestException.class, () -> userservice.updateUserPassword(null,null));

    }
}