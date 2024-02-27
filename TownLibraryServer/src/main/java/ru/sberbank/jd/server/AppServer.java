package ru.sberbank.jd.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class AppServer
{
    public static void main( String[] args )
    {
        SpringApplication.run(AppServer.class, args);
    }
}