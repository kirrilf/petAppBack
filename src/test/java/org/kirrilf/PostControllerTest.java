package org.kirrilf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirrilf.dto.PostDto;
import org.kirrilf.model.Post;
import org.kirrilf.model.User;
import org.kirrilf.repository.PostRepository;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/create-post-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestPropertySource("/application-test.properties")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;


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
        this.mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer_"+accessToken))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].text", is("test post1")))
                .andExpect(jsonPath("$.[0].authorId", is(1)))
                .andExpect(jsonPath("$.[1].id", is(2)))
                .andExpect(jsonPath("$.[1].text", is("test post2")))
                .andExpect(jsonPath("$.[1].authorId", is(1)));

    }

    @Test
    public void testCreatePostGood() throws Exception {
        this.mockMvc.perform(post("/api/posts")
                .content("{\"text\":\"test post\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer_"+accessToken))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("test post")))
                .andExpect(jsonPath("$.authorId", is(1)));
    }

    @Test
    public void testUpdatePostGood() throws Exception {
        this.mockMvc.perform(put("/api/posts/1")
                .content("{\"text\":\"test post update\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer_"+accessToken))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("test post update")))
                .andExpect(jsonPath("$.authorId", is(1)));
    }

    @Test
    public void testDeletePostGood() throws Exception {
        this.mockMvc.perform(put("/api/posts/1")
                .content("{\"text\":\"test post update\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer_"+accessToken))
                //.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPostsBadWithoutToken() throws Exception {
        this.mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreatePostsBadWithoutToken() throws Exception {
        this.mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdatePostsBadWithoutToken() throws Exception {
        this.mockMvc.perform(put("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeletePostsBadWithoutToken() throws Exception {
        this.mockMvc.perform(delete("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetPostsBadWithoutExpireToken() throws Exception {
        this.mockMvc.perform(delete("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer_"+"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraXIiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNjAzODc1MjQ0LCJleHAiOjE2MDM4NzUzMDR9.mQ6JNCpyoxG9UC0ae4qO04uXcnryI3szq6EiBY4libY"))
                .andExpect(status().isForbidden());
    }


}