package com.orange.rouber.service;

import com.orange.rouber.model.User;
import com.orange.rouber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public void registerUser(User user) {
        userRepository.save(user);
    }
}
