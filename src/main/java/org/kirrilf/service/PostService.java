package org.kirrilf.service;


import org.kirrilf.dto.PostDto;
import org.kirrilf.dto.PostPageDto;
import org.kirrilf.model.Image;
import org.kirrilf.model.Post;
import org.kirrilf.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface PostService {

    Post add(Post post, List<Image> images, HttpServletRequest request);

    PostPageDto getAll(Pageable pageable, HttpServletRequest request);

    List<Post> getAllUserPosts(Long userId);

    Post getOnePost(Long postID);

    void delete(Long id);

    void deleteAllImagesByPost(Post post);

    Post update(String text, Long postId, HttpServletRequest request);

    PostDto like(Long id, HttpServletRequest request);

    List<Image> getAllFileNamesByPost(Post post);


}
