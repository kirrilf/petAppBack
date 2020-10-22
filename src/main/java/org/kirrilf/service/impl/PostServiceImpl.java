package org.kirrilf.service.impl;

import org.kirrilf.model.Post;
import org.kirrilf.model.Status;
import org.kirrilf.repository.PostRepository;
import org.kirrilf.security.jwt.access.JwtAccessTokenProvider;
import org.kirrilf.service.PostService;
import org.kirrilf.service.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final JwtAccessTokenProvider jwtAccessTokenProvider;
    private final PostRepository postRepository;
    private final UserService userService;

    public PostServiceImpl(JwtAccessTokenProvider jwtAccessTokenProvider, PostRepository postRepository, UserService userService) {
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.postRepository = postRepository;
        this.userService = userService;

    }

    @Override
    public Post add(Post post, HttpServletRequest request) {
        post.setStatus(Status.ACTIVE);
        post.setCreated(new Date());
        post.setUpdated(new Date());
        post.setAuthor(userService.findByUsername(
                jwtAccessTokenProvider.getUsername(
                    jwtAccessTokenProvider.resolveToken(request))));
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getAllUserPosts(Long userId) {
        return postRepository.findByAuthor(userService.findById(userId));
    }

    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post update(String text, Long postId, HttpServletRequest request) {
        Long userId = userService.findByUsername(
                jwtAccessTokenProvider.getUsername(
                        jwtAccessTokenProvider.resolveToken(request)
                )
        ).getId();
        Post postFromBb = postRepository.findById(postId).orElse(null);
        if(postFromBb == null || !postFromBb.getAuthor().getId().equals(userId)){
            return null;
        }
        postFromBb.setText(text);
        postFromBb.setUpdated(new Date());
        return postRepository.save(postFromBb);
    }
}
