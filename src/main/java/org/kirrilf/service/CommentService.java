package org.kirrilf.service;

import org.kirrilf.dto.CommentPageDto;
import org.kirrilf.model.Comment;
import org.kirrilf.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface CommentService {

    Comment create(Comment comment,Long postId, HttpServletRequest request);

    Comment change(Comment comment, HttpServletRequest request);

    CommentPageDto getCommentsByPostId(Pageable pageable, Long postId);

    List<Comment> getTop3CommentByPost(Post post);

}
