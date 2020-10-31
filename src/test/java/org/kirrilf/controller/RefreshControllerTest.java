package org.kirrilf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirrilf.model.User;
import org.kirrilf.repository.PostRepository;
import org.kirrilf.repository.UserRepository;
import org.kirrilf.security.jwt.JwtAccessTokenProvider;
import org.kirrilf.security.jwt.JwtRefreshTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource("/application-test.properties")
public class RefreshControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private JwtRefreshTokenProvider jwtRefreshTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private String refreshToken;

    @Before
    public void getAccessToken() throws Exception {
        User user = userRepository.findByUsername("test");
        refreshToken = jwtRefreshTokenProvider.createToken(user.getUsername(), "test");
    }

    @Test
    public void testGetRefreshTokenGood() throws Exception {
        this.mockMvc.perform(get("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer_"+refreshToken)
                .header("Fingerprint", "test"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.access_token", is(notNullValue())));
    }

    @Test
    public void testGetRefreshTokenBadWithoutFingerprint() throws Exception {
        this.mockMvc.perform(get("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer_"+refreshToken))
                //.andDo(print())
                .andExpect(status().isForbidden());
    }
    @Test
    public void testGetRefreshTokenBadWithExpireToken() throws Exception {
        this.mockMvc.perform(get("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer_"+"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraXIiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjAzODc1MjQ0LCJleHAiOjE2MDM4NzUzMDR9.mQ6JNCpyoxG9UC0ae4qO04uXcnryI3szq6EiBY4libY"))
                //.andDo(print())
                .andExpect(status().isForbidden());
    }


}