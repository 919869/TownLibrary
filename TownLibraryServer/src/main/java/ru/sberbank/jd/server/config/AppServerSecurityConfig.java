package ru.sberbank.jd.server.config;

import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import ru.sberbank.jd.server.service.UsersService;
import ru.sberbank.jd.server.service.dto.UserDto;

@Configuration
@EnableWebSecurity
public class AppServerSecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public UserDetailsService userDetailsService(UsersService usersService) {
        return username -> {
            Optional<UserDto> userDto = usersService.findById(Long.valueOf(username));

            if (userDto.isEmpty()) {
                throw new UsernameNotFoundException("User ‘" + username + "’ not found");
            } else {
                return User.builder()
                        .username(userDto.get().getFirstName())
                        .password(userDto.get().getPassword())
                        .authorities(userDto.get().getRoles().stream()
                                .map((role) -> new SimpleGrantedAuthority("ROLE_" + role.getId()))
                                .collect(Collectors.toList()))
                        .accountExpired(false)
                        .accountLocked(false)
                        .accountLocked(false)
                        .credentialsExpired(false)
                        .build();
            }
        };
    }
    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/").hasRole("ADMIN")
                        .requestMatchers("/bookCards", "/bookCards/**").hasRole("ADMIN")
                        .requestMatchers("/readers", "/readers/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").hasRole("TECH")
                        .anyRequest().permitAll()
                    )
                .formLogin(loginConfigurer -> loginConfigurer
                        .defaultSuccessUrl("/", true))
                .httpBasic(Customizer.withDefaults())
                .logout(logoutConfigurer -> logoutConfigurer
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login"));

        return http.build();
    }
}
