package com.oleksii.usermanagement.service;

import com.oleksii.usermanagement.domain.UserCreateResource;
import com.oleksii.usermanagement.domain.UserEntity;
import com.oleksii.usermanagement.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    private UserEntity userEntity = UserEntity.builder()
            .name("test")
            .description("test description")
            .key("CHANGE TO PROPER KEY")
            .build();

    private UserCreateResource userCreateResource = UserCreateResource.builder().name("test").description("test description").build();

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userService = new UserService(userRepository, encryptionService);
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(encryptionService.generateEncryptionKey()).thenReturn("CHANGE TO PROPER KEY");
        userService.createUser(userCreateResource);

        verify(encryptionService, only()).generateEncryptionKey();
        verify(userRepository, only()).save(userEntity);
    }

    @Test
    public void testGetUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        userService.getUser(1L);

        verify(userRepository, only()).findById(1L);
    }
}