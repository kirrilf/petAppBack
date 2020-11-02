package org.kirrilf.service;


import org.kirrilf.dto.PostDto;
import org.kirrilf.model.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {

    Post add(Post post, HttpServletRequest request);

    List<Post> getAll();

    List<Post> getAllUserPosts(Long userId);

    void delete(Long id);

    Post update(String text, Long postId, HttpServletRequest request);

    PostDto like(Long id, HttpServletRequest request);

}
