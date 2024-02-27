package ru.sberbank.jd.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.sberbank.jd.server.controller.reader.dto.CreateReaderDto;
import ru.sberbank.jd.server.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.server.controller.reader.dto.ReaderNotificationDto;
import ru.sberbank.jd.server.service.dto.CreateUserDto;
import ru.sberbank.jd.server.service.dto.RoleDto;
import ru.sberbank.jd.server.service.dto.UserDto;
import ru.sberbank.jd.server.service.dto.UserNotificationDto;

/**
 * Класс сервис для читателей.
 */
@Service
@Slf4j
public class ReadersService {
    private final UsersService usersService;

    /**
     * Конструктор.
     *
     * @param usersService ссылка на класс сервис для пользователей
     */
    public ReadersService(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Определить список всех читателей.
     *
     * @return список читателей
     */
    public List<ReaderDto> findAll() {
        List<UserDto> userDtos = usersService.findAllByRoleId("READER");
        return userDtos.stream()
                .map(ReadersService::userDtoToReaderDto)
                .collect(Collectors.toList());
    }

    /**
     * Определить читателя по id
     *
     * @param id идентификатор читателя
     * @return ссылка на объект читателя
     */
    public Optional<ReaderDto> findByReaderId(Long id) {
        Optional<UserDto> userDto = usersService.findById(id);
        return userDto.map(ReadersService::userDtoToReaderDto)
                .filter(readerDto -> "READER".equalsIgnoreCase(readerDto.getRole().getId()));
    }

    /**
     * Определить список читателей по имени или фамилии
     *
     * @param keyword ключевое слово поиска
     * @return список читателей
     */
    public List<ReaderDto> findByFirstNameOrLastName(String keyword) {
        List<UserDto> userDtos = new ArrayList<>();

        if (keyword == null) {
            userDtos.addAll(usersService.findAllByRoleId("READER"));
        } else {
            userDtos.addAll(usersService.findByFirstNameLike(keyword));
            userDtos.addAll(usersService.findByLastNameLike(keyword));
        }

        return userDtos.stream()
                .filter(userDto -> userDto.getRoles().stream()
                        .anyMatch(roleDto -> "READER".equalsIgnoreCase(roleDto.getId())))
                .map(ReadersService::userDtoToReaderDto)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Определить список сообщений читателя по идентификатору.
     *
     * @param id идентификатор читателя
     * @return список сообщений читателя
     */
    public List<ReaderNotificationDto> getNotificationByReaderId(Long id) {
        List<ReaderNotificationDto> result = new ArrayList<>();

        Optional<ReaderDto> readerDto = findByReaderId(id);
        if (readerDto.isPresent()) {
            List<UserNotificationDto> userNotificationDtos = this.usersService.getNotificationByUserId(id);
            return userNotificationDtos.stream()
                    .map(value -> {
                        ReaderNotificationDto readerNotificationDto = new ReaderNotificationDto();

                        readerNotificationDto.setRecordId(value.getRecordId());
                        readerNotificationDto.setMessage(value.getMessage());
                        readerNotificationDto.setCreateDate(value.getCreateDate());

                        return readerNotificationDto;
                    })
                    .collect(Collectors.toList());
        }

        return result;
    }

    /**
     * Создание карточки читателя.
     *
     * @param readerDto данные читателя
     * @return ссылка на объект созданного читателя
     */
    public void create(CreateReaderDto readerDto) {
        usersService.create(ReadersService.createReaderDtoToUserDto(readerDto));
    }

    static private ReaderDto userDtoToReaderDto(UserDto userDto) {
        ReaderDto readerDto = new ReaderDto();

        log.info("UserDto: " + userDto.toString());

        readerDto.setId(userDto.getId());
        readerDto.setPassword(userDto.getPassword());
        readerDto.setLastName(userDto.getLastName());
        readerDto.setFirstName(userDto.getFirstName());

        userDto.getRoles().stream()
                .filter(value -> "READER".equalsIgnoreCase(value.getId()))
                .findFirst()
                .ifPresent(readerDto::setRole);

        return readerDto;
    }

    static private CreateUserDto createReaderDtoToUserDto(CreateReaderDto newReaderDto) {
        CreateUserDto newUserDto = new CreateUserDto();

        newUserDto.setId(newReaderDto.getId());
        newUserDto.setPassword(newReaderDto.getPassword());
        newUserDto.setFirstName(newReaderDto.getFirstName());
        newUserDto.setLastName(newReaderDto.getLastName());
        newUserDto.addRole(new RoleDto("READER", ""));

        return newUserDto;
    }
}
