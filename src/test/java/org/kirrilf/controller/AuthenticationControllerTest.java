package org.kirrilf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirrilf.dto.AuthenticationUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AuthenticationController authenticationController;

    @Test
    public void testOkLogin() throws Exception{
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setUsername("kir");
        authenticationUserDto.setPassword("kir");
        this.mockMvc.perform(post("/api/auth/login")
                            .content(mapper.writeValueAsString(authenticationUserDto))
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .header("Fingerprint", 2))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("kir")))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())));;
    }

    @Test
    public void testBadLoginWithBadUsername() throws Exception{
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setUsername("kir1");
        authenticationUserDto.setPassword("kir");
        this.mockMvc.perform(post("/api/auth/login")
                .content(mapper.writeValueAsString(authenticationUserDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Fingerprint", 2))
                //.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testBadLoginWithBadPassword() throws Exception{
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setUsername("kir");
        authenticationUserDto.setPassword("kir2");
        this.mockMvc.perform(post("/api/auth/login")
                .content(mapper.writeValueAsString(authenticationUserDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Fingerprint", 2))
                //.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testBadLoginWithoutFingerprint() throws Exception{
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setUsername("kir");
        authenticationUserDto.setPassword("kir");
        this.mockMvc.perform(post("/api/auth/login")
                .content(mapper.writeValueAsString(authenticationUserDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                //.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testBadLoginWithoutContent() throws Exception{
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        this.mockMvc.perform(post("/api/auth/login")
                .content(mapper.writeValueAsString(authenticationUserDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                //.andDo(print())
                .andExpect(status().isForbidden());
    }



}