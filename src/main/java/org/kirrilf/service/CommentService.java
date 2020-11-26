package org.kirrilf.service;

import org.kirrilf.model.Comment;
import org.kirrilf.model.User;


public interface CommentService {
    Comment create(Comment comment, User user);
}
