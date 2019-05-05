package com.oleksii.usermanagement.service;

import com.oleksii.usermanagement.domain.UserCreateResource;
import com.oleksii.usermanagement.domain.UserEntity;
import com.oleksii.usermanagement.exception.UserNotFoundException;
import com.oleksii.usermanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private EncryptionService encryptionService;

    public UserEntity createUser(UserCreateResource user) {
        return userRepository.save(UserEntity.builder()
                .name(user.getName())
                .description(user.getDescription())
                .key(encryptionService.generateEncryptionKey())
                .build());
    }

    public UserEntity getUser(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
