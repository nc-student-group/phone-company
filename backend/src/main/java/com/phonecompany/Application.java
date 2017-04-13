package com.phonecompany;

import com.phonecompany.dao.UserDaoImpl;
import com.phonecompany.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner testData(final UserDaoImpl userDao) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
//                User user = new User();
//                user.setEmail("shpilak.yura1@gmail.com");
//                user.setPassword("123456");
//                userDao.save(user);
                User user = userDao.getById(1l);
                System.out.println(user.getEmail());
            }
        };
    }

}
