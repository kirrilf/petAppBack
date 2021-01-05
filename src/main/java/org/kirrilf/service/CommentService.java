package org.kirrilf.service;

import org.kirrilf.model.Comment;
import org.kirrilf.model.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface CommentService {

    Comment create(Comment comment,Long postId, HttpServletRequest request);

    Comment change(Comment comment, HttpServletRequest request);

    List<Comment> getCommentsByPostId(Long postId);

    List<Comment> getTop3CommentByPost(Post post);

}
