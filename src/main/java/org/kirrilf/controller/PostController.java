package org.kirrilf.controller;


import org.apache.log4j.Logger;
import org.kirrilf.dto.PostDto;
import org.kirrilf.model.Post;
import org.kirrilf.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    private static final Logger logger = Logger.getLogger(PostController.class);

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    public ResponseEntity<List<PostDto>> allPosts() {
        List<Post> posts = postService.getAll();
        logger.debug("Get all posts");
        List<PostDto> postsDto = new ArrayList<>();
        for (Post i : posts) {
            postsDto.add(PostDto.fromPost(i));
        }
        return new ResponseEntity<>(postsDto, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<PostDto> create(@RequestBody PostDto postDto, HttpServletRequest request) {
        Post post = postDto.toPost();
        PostDto result = PostDto.fromPost(postService.add(post, request));
        logger.debug("Create new post: " + post.getText() + "With author " + post.getAuthor());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<PostDto>> allUserPosts(@PathVariable(name = "id") Long id) {
        List<Post> posts = postService.getAllUserPosts(id);
        List<PostDto> postsDto = new ArrayList<>();
        for (Post post : posts) {
            postsDto.add(PostDto.fromPost(post));
        }
        logger.debug("Get all post user with id: " + id);
        return new ResponseEntity<>(postsDto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "id") Long id, @RequestBody PostDto postDtoText, HttpServletRequest request) {
        PostDto postDto = PostDto.fromPost(postService.update(postDtoText.getText(), id, request));
        logger.debug("Update post with id: " + id + " and text " + postDtoText.getText());
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(name = "id") Long id) {
        postService.delete(id);
        logger.debug("Delete post with id: " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
