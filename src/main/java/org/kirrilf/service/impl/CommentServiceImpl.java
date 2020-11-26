package org.kirrilf.service.impl;

import org.kirrilf.model.Comment;
import org.kirrilf.model.Status;
import org.kirrilf.model.User;
import org.kirrilf.repository.CommentRepository;
import org.kirrilf.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment create(Comment comment, User user) {
        comment.setAuthor(user);
        comment.setStatus(Status.ACTIVE);
        comment.setCreated(new Date());
        comment.setUpdated(new Date());
        commentRepository.save(comment);
        return comment;
    }
}
