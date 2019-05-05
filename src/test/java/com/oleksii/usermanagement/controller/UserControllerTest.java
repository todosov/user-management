package com.oleksii.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.oleksii.usermanagement.domain.UserEntity;
import com.oleksii.usermanagement.exception.UserNotFoundException;
import com.oleksii.usermanagement.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private XmlMapper xmlMapper = new XmlMapper();

    private UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .name("test1")
            .description("desc")
            .key("CHANGE TO PROPER KEY")
            .build();

    @Test
    public void testCreateUserWithJsonPayload() throws Exception {
        when(userService.createUser(any())).thenReturn(userEntity);

        MvcResult mvcResult = mockMvc.perform(post("/service/user/realm")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"test1\",\"description\": \"desc\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        UserEntity userResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserEntity.class);
        assertEquals(userEntity, userResult);
    }

    @Test
    public void testCreateUserWithXMLPayload() throws Exception {
        when(userService.createUser(any())).thenReturn(userEntity);

        MvcResult mvcResult = mockMvc.perform(post("/service/user/realm")
                .contentType(MediaType.APPLICATION_XML)
                .accept(MediaType.APPLICATION_XML)
                .content("<realm><name>test1</name><description>desc</description></realm>"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andReturn();

        UserEntity userResult = xmlMapper.readValue(mvcResult.getResponse().getContentAsString(), UserEntity.class);
        assertEquals(userEntity, userResult);
    }

    @Test
    public void testCreateUserWithEmptyNameShouldFail() throws Exception {
        mockMvc.perform(post("/service/user/realm")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"description\": \"desc\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\": \"InvalidRealmName\"}"));
    }

    @Test
    public void testCreateUserWithExistingNameShouldFail() throws Exception {
        when(userService.createUser(any())).thenThrow(new DataIntegrityViolationException(""));

        mockMvc.perform(post("/service/user/realm")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"test1\",\"description\": \"desc\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\": \"DuplicateRealmName\"}"));
    }

    @Test
    public void testRetrievingUserWithJsonPayload() throws Exception {
        when(userService.getUser(any())).thenReturn(userEntity);

        MvcResult mvcResult = mockMvc.perform(get("/service/user/realm/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        UserEntity userResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserEntity.class);
        assertEquals(userEntity, userResult);
    }

    @Test
    public void testRetrievingUserWithXMLJsonPayload() throws Exception {
        when(userService.getUser(any())).thenReturn(userEntity);

        MvcResult mvcResult = mockMvc.perform(get("/service/user/realm/1")
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml;charset=UTF-8"))
                .andReturn();

        UserEntity userResult = xmlMapper.readValue(mvcResult.getResponse().getContentAsString(), UserEntity.class);
        assertEquals(userEntity, userResult);
    }

    @Test
    public void testRetrievingUserWithInvalidId() throws Exception {
        mockMvc.perform(get("/service/user/realm/1s")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\": \"InvalidArgument\"}"));
    }

    @Test
    public void testRetrievingNonexistentUser() throws Exception {
        when(userService.getUser(any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/service/user/realm/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"code\": \"RealmNotFound\"}"));
    }

    @Test
    public void testExceptionResponseInXmlFormat() throws Exception {
        mockMvc.perform(get("/service/user/realm/1s")
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest())
                .andExpect(content().xml("<error><code>InvalidArgument</code></error>"));
    }
}