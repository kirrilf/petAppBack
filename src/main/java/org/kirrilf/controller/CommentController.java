package org.kirrilf.controller;

import org.kirrilf.dto.CommentDto;
import org.kirrilf.dto.CommentPageDto;
import org.kirrilf.model.Comment;
import org.kirrilf.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/comment")
public class CommentController {
    private final CommentService commentService;
    private static final int COMMENTS_PER_PAGE = 5;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping(value = "/{id}")
    public ResponseEntity<CommentDto> create(@PathVariable(name = "id") Long postId, @RequestBody Comment comment, HttpServletRequest request) {
        return new ResponseEntity<>(CommentDto.fromComment(commentService.create(comment, postId, request)), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentPageDto> getPostComments(@PageableDefault(size = COMMENTS_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                          @PathVariable(name = "id") Long postId) {
        return new ResponseEntity<>(commentService.getCommentsByPostId(pageable, postId), HttpStatus.OK);
    }




}
