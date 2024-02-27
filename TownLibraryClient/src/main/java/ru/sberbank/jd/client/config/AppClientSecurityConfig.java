package ru.sberbank.jd.client.config;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.sberbank.jd.client.controller.reader.dto.ReaderDto;
import ru.sberbank.jd.client.proxy.ReadersProxy;
import ru.sberbank.jd.client.service.LoggedUserManagementService;

@Configuration
@EnableWebSecurity
public class AppClientSecurityConfig {
    private final LoggedUserManagementService loggedUserManagementService;

    public AppClientSecurityConfig(LoggedUserManagementService loggedUserManagementService) {
        this.loggedUserManagementService = loggedUserManagementService;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(ReadersProxy readersProxy) {
        return username -> {
            String requestId = UUID.randomUUID().toString();
            ResponseEntity<ReaderDto> responseEntity = readersProxy.getReaderById(requestId, username);

            if (!responseEntity.hasBody() && (!responseEntity.hasBody() || responseEntity.getBody() == null)) {
                throw new UsernameNotFoundException("User ‘" + username + "’ not found");
            }

            this.loggedUserManagementService.setReaderDto(responseEntity.getBody());

            return new UserDetails() {
                private final String username = String.valueOf(Objects.requireNonNull(responseEntity.getBody()).getId());
                private final String password = Objects.requireNonNull(responseEntity.getBody()).getPassword();

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return null;
                }

                @Override
                public String getPassword() {
                    return this.password;
                }

                @Override
                public String getUsername() {
                    return this.username;
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        };
    }
    @Bean
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                                .anyRequest().authenticated()
                )
                .formLogin(loginConfigurer -> loginConfigurer
                        .defaultSuccessUrl("/", true))
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"));

        return http.build();
    }
}
