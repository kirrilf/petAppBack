package org.kirrilf.controller;


import org.apache.log4j.Logger;
import org.kirrilf.dto.PostDto;
import org.kirrilf.dto.PostPageDto;
import org.kirrilf.model.Image;
import org.kirrilf.model.Post;
import org.kirrilf.service.CommentService;
import org.kirrilf.service.PostService;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private static final int POSTS_PER_PAGE = 1;

    private static final Logger logger = Logger.getLogger(PostController.class);

    public PostController(PostService postService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @Value("${upload.path}")
    private String uploadPath;


    @GetMapping
    public ResponseEntity<PostPageDto> allPosts(@PageableDefault(size = POSTS_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {

        PostPageDto postPageDto = postService.getAll(pageable, request);


        return new ResponseEntity<>(postPageDto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDto> getOnePost(@PathVariable(name = "id") Long id,
                                              HttpServletRequest request) {
        Post post = postService.getOnePost(id);
        return new ResponseEntity<>(PostDto.fromPost(post,
                        postService.getAllFileNamesByPost(post),
                        userService.getUserByRequest(request), commentService.getTop3CommentByPost(post)), HttpStatus.OK);
    }


    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PostDto> create(@RequestParam String text,
                                          @RequestParam(value = "file") MultipartFile[] files,
                                          HttpServletRequest request) throws IOException {
        Post post = new Post();
        post.setText(text);

        List<Image> images = saveImages(files);

        PostDto result = PostDto.fromPost(postService.add(post, images,request), images,  userService.getUserByRequest(request), commentService.getTop3CommentByPost(post));
        logger.debug("Create new post: " + post.getText() + "With author " + post.getAuthor());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private List<Image> saveImages(@RequestParam("file") MultipartFile[] files) throws IOException {
        List<Image> images = new LinkedList<>();
        File uploadDir = new File(uploadPath);
        for (MultipartFile i : files) {
            if (i.getOriginalFilename() != null) {
                String uuidFile = UUID.randomUUID().toString();
                String resultNameFile = uuidFile + "." + i.getOriginalFilename();
                i.transferTo(new File(uploadPath + "/" + resultNameFile));
                Image image = new Image();
                image.setFileName(resultNameFile);
                images.add(image);
            }
        }
        return images;
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<PostDto>> allUserPosts(@PathVariable(name = "id") Long id,
                                                      HttpServletRequest request) {
        List<Post> posts = postService.getAllUserPosts(id);
        List<PostDto> postsDto = new ArrayList<>();
        for (Post post : posts) {
            postsDto.add(PostDto.fromPost(post, postService.getAllFileNamesByPost(post),  userService.getUserByRequest(request), commentService.getTop3CommentByPost(post)));
        }
        logger.debug("Get all post user with id: " + id);
        return new ResponseEntity<>(postsDto, HttpStatus.OK);
    }


    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name = "id") Long id,
                                              @RequestParam String text,
                                              HttpServletRequest request) throws IOException {

        Post postUpdate = postService.update(text, id, request);
        PostDto postDto = PostDto.fromPost(postUpdate,
                postService.getAllFileNamesByPost(postUpdate),
                userService.getUserByRequest(request), commentService.getTop3CommentByPost(postUpdate));
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
