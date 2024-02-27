package ru.sberbank.jd.server.service;

import java.time.Instant;
import java.util.ArrayList;
import java.time.Clock;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.server.entity.LibUserNotification;
import ru.sberbank.jd.server.repository.LibRoleRepository;
import ru.sberbank.jd.server.repository.LibUserNameRepository;
import ru.sberbank.jd.server.repository.LibUserNotificationRepository;
import ru.sberbank.jd.server.repository.LibUserRepository;
import ru.sberbank.jd.server.service.dto.RoleDto;
import ru.sberbank.jd.server.service.dto.CreateUserDto;
import ru.sberbank.jd.server.entity.LibRole;
import ru.sberbank.jd.server.entity.LibUser;
import ru.sberbank.jd.server.entity.LibUserName;
import ru.sberbank.jd.server.service.dto.UserDto;
import ru.sberbank.jd.server.service.dto.UserNotificationDto;

/**
 * Класс сервис для обработки данных пользователя.
 */
@Service
@Slf4j
public class UsersService {
    private final LibUserRepository libUserRepository;
    private final LibRoleRepository libRoleRepository;
    private final LibUserNameRepository libUserNameRepository;
    private final LibUserNotificationRepository libUserNotificationRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор
     *
     * @param libUserRepository репозиторий для таблицы User
     * @param libUserNameRepository репозиторий для таблицы UserName
     * @param libRoleRepository репозиторий для таблицы Role
     * @param passwordEncoder шифрование паролей
     */
    public UsersService(LibUserRepository libUserRepository,
                        LibUserNameRepository libUserNameRepository,
                        LibRoleRepository libRoleRepository,
                        LibUserNotificationRepository libUserNotificationRepository,
                        PasswordEncoder passwordEncoder) {
        this.libUserRepository = libUserRepository;
        this.libUserNameRepository = libUserNameRepository;
        this.libRoleRepository = libRoleRepository;
        this.libUserNotificationRepository = libUserNotificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Определить список всех пользователей.
     *
     * @return список пользователей
     */
    public List<UserDto> findAll() {
        List<LibUser> users = libUserRepository.findAll();
        return users.stream()
                    .map(UsersService::userToUserDto)
                    .collect(Collectors.toList());
    }

    /**
     * Определить список пользователей по идентификатору роли.
     *
     * @param roleId идентификатор роли
     * @return список пользователей
     */
    public List<UserDto> findAllByRoleId(String roleId) {
        List<UserDto> results = new ArrayList<>();

        Optional<LibRole> libRole = libRoleRepository.findById(roleId);
        return libRole.map(value -> value.getUsers().stream()
                .map(UsersService::userToUserDto)
                .collect(Collectors.toList())).orElse(results);
    }

    /**
     * Определить пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return данные пользователя
     */
    public Optional<UserDto> findById(Long id) {
        Optional<LibUser> libUser = libUserRepository.findById(id);
        return libUser.map(UsersService::userToUserDto);
    }

    /**
     * Определить список пользователей по имени.
     *
     * @param firstName имя пользователя
     * @return список пользователей
     */
    public List<UserDto> findByFirstNameLike(String firstName) {
        List<LibUserName> libUserNames = libUserNameRepository.findByFirstNameLike("%" + firstName + "%");

        Iterable<Long> ids = libUserNames.stream()
                .map(LibUserName::getUserId)
                .distinct()
                .collect(Collectors.toList());

        return libUserRepository.findAllById(ids).stream()
                    .map(UsersService::userToUserDto)
                    .collect(Collectors.toList());
    }

    /**
     * Определить список пользователей по фамилии.
     *
     * @param lastName фамилия
     * @return список пользователей
     */
    public List<UserDto> findByLastNameLike(String lastName) {
        List<LibUserName> libUserNames = libUserNameRepository.findByLastNameLike("%" + lastName + "%");

        Iterable<Long> ids = libUserNames.stream()
                .map(LibUserName::getUserId)
                .distinct()
                .collect(Collectors.toList());

        return libUserRepository.findAllById(ids).stream()
                .map(UsersService::userToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Создать пользователя.
     *
     * @param userDto данные пользователя
     */
    public void create(CreateUserDto userDto) {
        LibUser libUser = new LibUser();
        LibUserName libUserName = new LibUserName();

        libUser.setId(userDto.getId());
        libUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        libUser.setRoles(userDto.getRoles().stream()
                    .map(roleDto -> new LibRole(roleDto.getId(), "", null))
                    .collect(Collectors.toList()));

        libUserName.setUserId(userDto.getId());
        libUserName.setFirstName(userDto.getFirstName());
        libUserName.setLastName(userDto.getLastName());

        libUserRepository.save(libUser);
        libUserNameRepository.save(libUserName);
    }

    /**
     * Создание сообщение для пользователя.
     *
     * @param id идентификатор пользователя
     * @param message сообщение
     */
    public void createNotification(Long id, String message) {
        Optional<LibUser> libUser = this.libUserRepository.findById(id);
        if (libUser.isEmpty()) {
            throw new RuntimeException("User with id = " + id + " not found!");
        }

        LibUserNotification libUserNotification = new LibUserNotification();

        libUserNotification.setUserId(id);
        libUserNotification.setMessage(message);
        libUserNotification.setCreateDate(Date.from(Instant.now()));

        this.libUserNotificationRepository.save(libUserNotification);
    }

    /**
     * Определить список сообщений по идентификатору пользователя.
     *
     * @param id идентификатор пользователя
     * @return список сообщений
     */
    public List<UserNotificationDto> getNotificationByUserId(Long id) {
        Optional<LibUser> libUser = this.libUserRepository.findById(id);
        return libUser.map(user -> user.getNotifications().stream()
                .map(value -> {
                    UserNotificationDto userNotificationDto = new UserNotificationDto();

                    userNotificationDto.setRecordId(value.getRecordId());
                    userNotificationDto.setMessage(value.getMessage());
                    userNotificationDto.setCreateDate(value.getCreateDate());

                    return userNotificationDto;
                })
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    /**
     * Создание учетной записи администратора.
     *
     * @param id идентификатор
     * @param password пароль
     * @param firstName имя
     * @param lastName фамилия
     * @return созданный пользователь
     */
    public Optional<UserDto> createAdministrator(Long id, String password, String firstName, String lastName) {
        List<RoleDto> roles = new ArrayList<>();
        roles.add(new RoleDto("ADMIN", ""));

        create(new CreateUserDto(id, password, firstName, lastName, roles));
        return findById(id);
    }

    /**
     * Создание учетной записи технического пользователя.
     *
     * @param id идентификатор
     * @param password пароль
     * @param firstName имя
     * @param lastName фамилия
     * @return созданный пользователь
     */
    public Optional<UserDto> createTechnical(Long id, String password, String firstName, String lastName) {
        List<RoleDto> roles = new ArrayList<>();
        roles.add(new RoleDto("TECH", ""));

        create(new CreateUserDto(id, password, firstName, lastName, roles));
        return findById(id);
    }

    static private UserDto userToUserDto(LibUser user) {
        UserDto userDto = new UserDto();

        List<RoleDto> roles = user.getRoles().stream()
                .map(role -> {
                    RoleDto roleDto = new RoleDto();
                    roleDto.setId(role.getId());
                    roleDto.setName(role.getName());
                    return roleDto;
                })
                .toList();

        userDto.setId(user.getId());
        userDto.setPassword(user.getPassword());
        userDto.setLastName(user.getNames().get(0).getLastName());
        userDto.setFirstName(user.getNames().get(0).getFirstName());
        userDto.getRoles().addAll(roles);

        return userDto;
    }
}
