package org.kirrilf.controller;

import org.kirrilf.dto.CommentDto;
import org.kirrilf.model.Comment;
import org.kirrilf.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping
    public ResponseEntity<CommentDto> create(@RequestBody Comment comment, HttpServletRequest request) {
        commentService.create(comment, request);
        return new ResponseEntity<>(CommentDto.fromComment(comment), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<CommentDto>> getPostComments(@PathVariable(name = "id") Long postId) {
        List<CommentDto> commentsDto = new ArrayList<>();
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        comments.forEach(comment -> commentsDto.add(CommentDto.fromComment(comment)));
        return new ResponseEntity<>(commentsDto, HttpStatus.OK);
    }
}
