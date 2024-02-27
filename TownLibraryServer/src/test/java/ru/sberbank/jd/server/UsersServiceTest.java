package ru.sberbank.jd.server;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import junit.framework.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.sberbank.jd.server.entity.LibRole;
import ru.sberbank.jd.server.entity.LibUser;
import ru.sberbank.jd.server.entity.LibUserName;
import ru.sberbank.jd.server.entity.LibUserNotification;
import ru.sberbank.jd.server.repository.LibRoleRepository;
import ru.sberbank.jd.server.repository.LibUserNameRepository;
import ru.sberbank.jd.server.repository.LibUserNotificationRepository;
import ru.sberbank.jd.server.repository.LibUserRepository;
import ru.sberbank.jd.server.service.UsersService;
import ru.sberbank.jd.server.service.dto.CreateUserDto;
import ru.sberbank.jd.server.service.dto.RoleDto;
import ru.sberbank.jd.server.service.dto.UserDto;
import ru.sberbank.jd.server.service.dto.UserNotificationDto;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    @Mock
    private LibUserRepository libUserRepository;
    @Mock
    private LibRoleRepository libRoleRepository;
    @Mock
    private LibUserNameRepository libUserNameRepository;
    @Mock
    private LibUserNotificationRepository libUserNotificationRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UsersService usersService;

    @Test
    @DisplayName("Find by role id")
    public void findByRoleIdSuccessful() {
        List<LibUser> expectUsers = new ArrayList<>();
        LibUser user;

        user = LibUser.builder()
                .id(800000L)
                .password("123456")
                .names(List.of(new LibUserName("1", 800000L, "FirstName1", "LastName1", null, null)))
                .roles(List.of(LibRole.builder().id("ADMIN").name("Administrator").build()))
                .build();
        expectUsers.add(user);

        user = LibUser.builder()
                .id(800001L)
                .password("123456")
                .names(List.of(new LibUserName("1", 800001L, "FirstName2", "LastName2", null, null)))
                .roles(List.of(LibRole.builder().id("ADMIN").name("Administrator").build()))
                .build();
        expectUsers.add(user);

        LibRole libAdminRole = new LibRole("ADMIN", "Administrator", expectUsers);

        when(libRoleRepository.findById("ADMIN"))
                .thenReturn(Optional.of(libAdminRole));

        List<UserDto> actualUser = usersService.findAllByRoleId("ADMIN");
        verify(libRoleRepository)
                .findById("ADMIN");
    }

    @Test
    @DisplayName("Find by id")
    public void findByIdSuccessful() {
        LibUser expectUser = LibUser.builder()
                .id(800000L)
                .password("123456")
                .names(List.of(new LibUserName("1", 800000L, "FirstName1", "LastName1", null, null)))
                .roles(List.of(LibRole.builder().id("ADMIN").name("Administrator").build()))
                .build();

        when(libUserRepository.findById(800000L))
                .thenReturn(Optional.of(expectUser));

        Optional<UserDto> actualUser = usersService.findById(800000L);
        verify(libUserRepository).findById(800000L);
    }

    @Test
    @DisplayName("Find by first name like")
    public void findByFirstNameLikeSuccessful() {
        List<LibUser> expectUsers = new ArrayList<>();
        LibUser user;

        user = LibUser.builder()
                .id(800000L)
                .password("123456")
                .names(List.of(new LibUserName("1", 800000L, "FirstName1", "LastName1", null, null)))
                .roles(List.of(LibRole.builder().id("ADMIN").name("Administrator").build()))
                .build();
        expectUsers.add(user);

        user = LibUser.builder()
                .id(800001L)
                .password("123456")
                .names(List.of(new LibUserName("1", 800001L, "FirstName2", "LastName2", null, null)))
                .roles(List.of(LibRole.builder().id("ADMIN").name("Administrator").build()))
                .build();
        expectUsers.add(user);

        when(libUserRepository.findAllById(List.of(800000L, 800001L)))
                .thenReturn(expectUsers);

        List<LibUserName> expectUserNames = new ArrayList<>();
        expectUserNames.add(LibUserName.builder()
                .recordId("1").userId(800000L).firstName("FirstName1").lastName("LastName1").build());
        expectUserNames.add(LibUserName.builder()
                .recordId("2").userId(800001L).firstName("FirstName2").lastName("LastName2").build());

        when(libUserNameRepository.findByFirstNameLike("%First%"))
                .thenReturn(expectUserNames);

        List<UserDto> actualUsers = usersService.findByFirstNameLike("First");

        verify(libUserNameRepository).findByFirstNameLike("%First%");
        verify(libUserRepository).findAllById(List.of(800000L, 800001L));
    }

    @Test
    @DisplayName("Find by last name like")
    public void findByLastNameLikeSuccessful() {
        List<LibUser> expectUsers = new ArrayList<>();
        LibUser user;

        user = LibUser.builder()
                .id(800000L)
                .password("123456")
                .names(List.of(new LibUserName("1", 800000L, "FirstName1", "LastName1", null, null)))
                .roles(List.of(LibRole.builder().id("ADMIN").name("Administrator").build()))
                .build();
        expectUsers.add(user);

        user = LibUser.builder()
                .id(800001L)
                .password("123456")
                .names(List.of(new LibUserName("1", 800001L, "FirstName2", "LastName2", null, null)))
                .roles(List.of(LibRole.builder().id("ADMIN").name("Administrator").build()))
                .build();
        expectUsers.add(user);

        when(libUserRepository.findAllById(List.of(800000L, 800001L)))
                .thenReturn(expectUsers);

        List<LibUserName> expectUserNames = new ArrayList<>();
        expectUserNames.add(LibUserName.builder()
                .recordId("1").userId(800000L).firstName("FirstName1").lastName("LastName1").build());
        expectUserNames.add(LibUserName.builder()
                .recordId("2").userId(800001L).firstName("FirstName2").lastName("LastName2").build());

        when(libUserNameRepository.findByFirstNameLike("%Last%"))
                .thenReturn(expectUserNames);

        List<UserDto> actualUsers = usersService.findByFirstNameLike("Last");

        verify(libUserNameRepository).findByFirstNameLike("%Last%");
        verify(libUserRepository).findAllById(List.of(800000L, 800001L));
    }

    @Test
    @DisplayName("Create user successful")
    public void createUserSuccessful() {
        LibUser actualUser = LibUser.builder()
                .id(800000L)
                .password("123456")
                .roles(List.of(LibRole.builder().id("ADMIN").name("").users(null).build()))
                .notifications(new ArrayList<>())
                .build();

        LibUserName actualUserName = LibUserName.builder()
                .userId(800000L)
                .firstName("FirstName")
                .lastName("LastName")
                .from_date(null)
                .to_date(null)
                .build();

        when(passwordEncoder.encode("123456"))
                .thenReturn("123456");

        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setId(800000L);
        createUserDto.setPassword("123456");
        createUserDto.setFirstName("FirstName");
        createUserDto.setLastName("LastName");
        createUserDto.addRole(new RoleDto("ADMIN", "Administrator"));

        usersService.create(createUserDto);

        verify(libUserRepository).save(actualUser);
        verify(libUserNameRepository).save(actualUserName);
    }

    @Test
    @DisplayName("Create notification successful")
    public void createNotificationSuccessful() {
        Instant instant = Instant.now(Clock.fixed(Instant.parse("2024-02-26T10:15:30Z"), ZoneId.of("UTC")));

        try(MockedStatic<Instant> mockedStaticInstant = mockStatic(Instant.class);) {
            mockedStaticInstant.when(Instant::now).thenReturn(instant);

            LibUser actualUser = LibUser.builder()
                    .id(800000L)
                    .password("123456")
                    .names(List.of(new LibUserName("1", 800000L, "FirstName1", "LastName1", null, null)))
                    .roles(List.of(LibRole.builder().id("ADMIN").name("Administrator").build()))
                    .build();
            when(libUserRepository.findById(800000L))
                    .thenReturn(Optional.of(actualUser));

            LibUserNotification actualLibUserNotification = LibUserNotification.builder()
                    .userId(800000L)
                    .message("Message1")
                    .createDate(Date.from(Instant.now()))
                    .build();

            usersService.createNotification(800000L, "Message1");

            verify(libUserNotificationRepository).save(actualLibUserNotification);
        }
    }

    @Test
    @DisplayName("Get notification by user id")
    public void getNotificationByUserId() {
        LibUser actualUser = LibUser.builder()
                .id(800000L)
                .password("123456")
                .notifications(
                        List.of(new LibUserNotification("1", 800000L, "Message1", Date.from(Instant.now())),
                                new LibUserNotification("2", 800000L, "Message2", Date.from(Instant.now())))
                )
                .build();
        when(libUserRepository.findById(800000L))
                .thenReturn(Optional.of(actualUser));

        List<UserNotificationDto> userNotificationDtos = usersService.getNotificationByUserId(800000L);

        verify(libUserRepository).findById(800000L);
        Assert.assertEquals(2, userNotificationDtos.size());
    }
}
