package org.kirrilf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirrilf.dto.AuthenticationUserDto;
import org.kirrilf.model.Role;
import org.kirrilf.model.User;
import org.kirrilf.repository.UserRepository;
import org.kirrilf.security.jwt.JwtAccessTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource("/application-test.properties")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PostController postController;

    @Autowired
    private JwtAccessTokenProvider jwtAccessTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private String accessToken;

    @Before
    public void getAccessToken() throws Exception {
        User user = userRepository.findByUsername("test");
        accessToken = jwtAccessTokenProvider.createToken(user.getUsername(), user.getRoles());
    }

    @Test
    public void testGetPostsGood() throws Exception {
        this.mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer_"+accessToken))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("kir")))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())));
    }

}