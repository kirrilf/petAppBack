package org.kirrilf.controller;


import org.kirrilf.dto.PostDto;
import org.kirrilf.model.Post;
import org.kirrilf.model.Status;
import org.kirrilf.model.User;
import org.kirrilf.repository.PostRepository;
import org.kirrilf.security.jwt.JwtTokenProvider;
import org.kirrilf.service.PostService;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> allPosts(){
        List<Post> posts  = postService.getAll();
        List<PostDto> postsDto = new ArrayList<>();
        for(Post i : posts){
            postsDto.add(PostDto.fromPost(i));
        }
        return new ResponseEntity<>(postsDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PostDto> create(@RequestBody PostDto postDto, HttpServletRequest request) {
        Post post = postDto.toPost();
        PostDto result = PostDto.fromPost(postService.add(post, request));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<PostDto>> allUserPosts(@PathVariable(name = "id") Long id){
        List<Post> posts = postService.getAllUserPosts(id);
        List<PostDto> postsDto = new ArrayList<>();
        for(Post post : posts){
            postsDto.add(PostDto.fromPost(post));
        }
        return new ResponseEntity<>(postsDto, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "id") Long id, @RequestBody PostDto postDtoText, HttpServletRequest request){
        System.out.println(postDtoText.getText());
        PostDto postDto = PostDto.fromPost(postService.update(postDtoText.getText(), id, request));
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

}
