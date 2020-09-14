package org.kirrilf.service;


import org.kirrilf.model.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {

    Post add(Post post, HttpServletRequest request);

    List<Post> getAll();

    List<Post> getAllUserPosts(Long userId);

    void delete(Post post);

    Post update(String text, Long postId, HttpServletRequest request);


}
