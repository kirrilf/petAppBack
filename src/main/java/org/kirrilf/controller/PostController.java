package org.kirrilf.controller;


import org.apache.log4j.Logger;
import org.kirrilf.dto.PostDto;
import org.kirrilf.model.Post;
import org.kirrilf.service.PostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    private static final Logger logger = Logger.getLogger(PostController.class);

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Value("${upload.path}")
    private String uploadPath;


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

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDto> getOnePost(@PathVariable(name = "id") Long id){
        Post post = postService.getOnePost(id);
        return new ResponseEntity<>(PostDto.fromPost(post), HttpStatus.OK);
    }


    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PostDto> create(@RequestParam String text,
                                          @RequestParam(value = "file", required = false) MultipartFile file,
                                          HttpServletRequest request) throws IOException {
        Post post = new Post();
        post.setText(text);
        if (file != null && file.getOriginalFilename() != null) {
            File uploadDir = new File(uploadPath);
            String uuidFile = UUID.randomUUID().toString();
            String resultNameFile = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultNameFile));
            post.setFileName(resultNameFile);
        }


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



    @PutMapping(value = "/{id}",consumes = "multipart/form-data")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "id") Long id,
                                              @RequestParam String text,
                                              @RequestParam(value = "file", required = false) MultipartFile file,
                                              HttpServletRequest request) throws IOException {

        String resultNameFile = null;
        if (file != null && file.getOriginalFilename() != null) {
            File uploadDir = new File(uploadPath);
            String uuidFile = UUID.randomUUID().toString();
            resultNameFile = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultNameFile));
        }

        PostDto postDto = PostDto.fromPost(postService.update(text,resultNameFile,id, request));
        logger.debug("Update post with id: " + id + " and text " + text);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }



    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(name = "id") Long id) {
        postService.delete(id);
        logger.debug("Delete post with id: " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/like")
    public ResponseEntity<PostDto> like(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        PostDto postDto = postService.like(id, request);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

}
