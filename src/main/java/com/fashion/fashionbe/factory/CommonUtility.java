package com.fashion.fashionbe.factory;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fashion.fashionbe.dto.Problem;

@Component
public class CommonUtility{

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public Function<String, Problem> createProblem = errorMessage -> {
        Problem problem = new Problem();
        problem.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        problem.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        problem.setDetail(errorMessage);
        return problem;
    };
}
