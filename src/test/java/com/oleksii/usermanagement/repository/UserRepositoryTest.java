package com.oleksii.usermanagement.repository;

import com.oleksii.usermanagement.domain.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private Long testId;

    @Before
    public void setUp() throws Exception {
        //add test records to db
        testId = entityManager
                .persistAndGetId(UserEntity.builder().name("test1").description("test user 1").key("key").build(),
                        Long.class);
        entityManager.persist(UserEntity.builder().name("test2").description("test user 2").key("key").build());
        entityManager.persist(UserEntity.builder().name("test3").description("test user 3").key("key").build());
    }

    @Test
    public void testGetUserById() {
        UserEntity userEntity = userRepository.findById(testId).get();

        assertEquals("test1", userEntity.getName());
        assertEquals("test user 1", userEntity.getDescription());
        assertEquals("key", userEntity.getKey());
    }

    @Test
    public void testSaveUser() {
        UserEntity entity = userRepository.save(UserEntity.builder().name("test").description("test user").key("key").build());

        assertTrue(userRepository.existsById(entity.getId()));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNameNotNullConstraint() {
        userRepository.save(UserEntity.builder().description("test user").key("key").build());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testSaveUserWithExistingName() {
        userRepository.save(UserEntity.builder().name("test1").description("test user 1").key("key").build());
    }
}