package org.kirrilf.service.impl;

import org.kirrilf.dto.CommentDto;
import org.kirrilf.dto.CommentPageDto;
import org.kirrilf.model.Comment;
import org.kirrilf.model.Post;
import org.kirrilf.model.Status;
import org.kirrilf.model.User;
import org.kirrilf.repository.CommentRepository;
import org.kirrilf.service.CommentService;
import org.kirrilf.service.PostService;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }

    @Override
    public Comment create(Comment comment, Long postId, HttpServletRequest request) {
        comment.setPost(postService.getOnePost(postId));
        comment.setAuthor(userService.getUserByRequest(request));
        comment.setStatus(Status.ACTIVE);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());
        return commentRepository.save(comment);
    }



    @Override
    public List<Comment> getTop3CommentByPost(Post post) {
        return commentRepository.findTop3ByPostOrderById(post);
    }

    @Override
    public Comment change(Comment comment, HttpServletRequest request) {
        if(comment.getAuthor() == userService.getUserByRequest(request)){
            comment.setUpdated(new Date());
            return commentRepository.save(comment);
        }
        return null;
    }

    @Override
    public CommentPageDto getCommentsByPostId(Pageable pageable, Long postId) {
        Page<Comment> commentPage = commentRepository.findCommentsByPostId(pageable, postId);
        List<CommentDto> commentsDto = new ArrayList<>();
        List<Comment> comments = commentPage.getContent();
        comments.forEach(comment -> commentsDto.add(CommentDto.fromComment(comment)));
        return new CommentPageDto(commentsDto, pageable.getPageNumber(), commentPage.getTotalPages());
    }
}
