package ru.sberbank.jd.server.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import ru.sberbank.jd.server.repository.LibRoleRepository;
import ru.sberbank.jd.server.service.dto.CreateUserDto;
import ru.sberbank.jd.server.service.dto.RoleDto;
import ru.sberbank.jd.server.entity.BookCard;
import ru.sberbank.jd.server.entity.LibRole;
import ru.sberbank.jd.server.repository.BookCardRepository;
import ru.sberbank.jd.server.service.UsersService;

@Configuration
public class AppServerConfig {
    @Bean
    @Order(1)
    public CommandLineRunner loadBookCard(BookCardRepository bookCardRepository,
            LibRoleRepository libRoleRepository, UsersService usersService) {
        return args -> {
            /*
            BookCard bookCard;

            bookCard = BookCard.builder()
                    .article("3200156")
                    .name("Java Core 1")
                    .author("ПАО Сбербанк")
                    .publisher("ПАО Сбербанк")
                    .lbc("11.11.111")
                    .numberOpened(0L)
                    .build();
            bookCardRepository.save(bookCard);

            bookCard = BookCard.builder()
                    .article("3200159")
                    .name("Java Core 2")
                    .author("ПАО СберТех")
                    .publisher("ПАО СберТех")
                    .lbc("99.99.999")
                    .numberOpened(0L)
                    .build();
            bookCardRepository.save(bookCard);
             */
/*
            libRoleRepository.save(new LibRole("ADMIN", "Administrator", null));
            libRoleRepository.save(new LibRole("READER", "Reader", null));
            libRoleRepository.save(new LibRole("TECH", "Technical", null));
*/
            /*
            List<RoleDto> readerRoles = new ArrayList<>();
            readerRoles.add(new RoleDto("READER", ""));
            usersService.create(new CreateUserDto(900000L, "123456", "FirstName1", "LastName1", readerRoles));
            usersService.create(new CreateUserDto(900001L, "123456", "FirstName2", "LastName2", readerRoles));
            usersService.create(new CreateUserDto(900003L, "123456", "FirstName3", "LastName3", readerRoles));
*/
            /*
            List<RoleDto> adminRoles = new ArrayList<>();
            adminRoles.add(new RoleDto("ADMIN", ""));
            usersService.create(new CreateUserDto(800000L, "123456", "FirstName1", "LastName1", adminRoles));
            usersService.create(new CreateUserDto(800001L, "123456", "FirstName2", "LastName2", adminRoles));

            List<RoleDto> techRoles = new ArrayList<>();
            techRoles.add(new RoleDto("TECH", ""));
            usersService.create(new CreateUserDto(100000L, "123456", "Technical", "Technical", techRoles));
             */
            usersService.createAdministrator(800000L, "123456", "Иван", "Ивано");
            usersService.createTechnical(100000L, "123456", "Technical", "Technical");
        };
    }
}
