package org.kirrilf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirrilf.dto.AuthenticationUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
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
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource("/application-test.properties")
public class AuthenticationControllerTest {


    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testOkLogin() throws Exception{
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setUsername("test");
        authenticationUserDto.setPassword("test");
        this.mockMvc.perform(post("/api/auth/login")
                .content(mapper.writeValueAsString(authenticationUserDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Fingerprint", "test"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("test")))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())));
    }

    @Test
    public void testBadLoginWithBadUsername() throws Exception{
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setUsername("test1");
        authenticationUserDto.setPassword("test");
        this.mockMvc.perform(post("/api/auth/login")
                .content(mapper.writeValueAsString(authenticationUserDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Fingerprint", "test"))
                //.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testBadLoginWithBadPassword() throws Exception{
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setUsername("test");
        authenticationUserDto.setPassword("test1");
        this.mockMvc.perform(post("/api/auth/login")
                .content(mapper.writeValueAsString(authenticationUserDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Fingerprint", "test"))
                //.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testBadLoginWithoutFingerprint() throws Exception{
        AuthenticationUserDto authenticationUserDto = new AuthenticationUserDto();
        authenticationUserDto.setUsername("test");
        authenticationUserDto.setPassword("test");
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