package org.kirrilf.service.impl;

import org.kirrilf.model.Post;
import org.kirrilf.model.Status;
import org.kirrilf.model.User;
import org.kirrilf.repository.PostRepository;
import org.kirrilf.security.jwt.JwtTokenProvider;
import org.kirrilf.service.PostService;
import org.kirrilf.service.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PostRepository postRepository;
    private final UserService userService;

    public PostServiceImpl(JwtTokenProvider jwtTokenProvider, PostRepository postRepository, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.postRepository = postRepository;
        this.userService = userService;

    }

    @Override
    public Post add(Post post, HttpServletRequest request) {
        post.setStatus(Status.ACTIVE);
        post.setCreated(new Date());
        post.setUpdated(new Date());
        post.setAuthor(userService.findByUsername(
                jwtTokenProvider.getUsername(
                    jwtTokenProvider.resolveToken(request))));
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
                jwtTokenProvider.getUsername(
                        jwtTokenProvider.resolveToken(request)
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
