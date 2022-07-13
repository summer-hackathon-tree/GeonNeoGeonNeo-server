package com.tlea.geonneo.domain.user.facade;

import com.tlea.geonneo.domain.user.domain.User;
import com.tlea.geonneo.domain.user.domain.repository.UserRepository;
import com.tlea.geonneo.domain.user.exception.PasswordMismatchException;
import com.tlea.geonneo.domain.user.exception.UserAlreadyExistsException;
import com.tlea.geonneo.domain.user.exception.UserNotFoundException;
import com.tlea.geonneo.global.security.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findUserByDongho(String dongho) {
        return userRepository.findByDongho(dongho)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    public User getCurrentUser() {
        AuthDetails auth = (AuthDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return auth.getUser();
    }

    public void checkUserByDongho(String dongho) {
        userRepository.findByDongho(dongho)
                .ifPresent(user -> { throw UserAlreadyExistsException.EXCEPTION; });
    }

    public void checkUserPassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw PasswordMismatchException.EXCEPTION;
        }
    }
}
