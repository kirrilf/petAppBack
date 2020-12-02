package org.kirrilf.service.impl;

import org.kirrilf.model.Comment;
import org.kirrilf.model.Status;
import org.kirrilf.repository.CommentRepository;
import org.kirrilf.service.CommentService;
import org.kirrilf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Override
    public void create(Comment comment, HttpServletRequest request) {


        comment.setAuthor(userService.getUserByRequest(request));
        comment.setStatus(Status.ACTIVE);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.getCommentsByPostId(postId);
    }
}
