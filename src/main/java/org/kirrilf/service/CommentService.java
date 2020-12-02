package org.kirrilf.service;

import org.kirrilf.model.Comment;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface CommentService {
    void create(Comment comment, HttpServletRequest request);

    List<Comment> getCommentsByPostId(Long postId);

}
