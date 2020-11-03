package org.kirrilf.service.impl;

import org.kirrilf.dto.PostDto;
import org.kirrilf.model.Post;
import org.kirrilf.model.Status;
import org.kirrilf.model.User;
import org.kirrilf.repository.PostRepository;
import org.kirrilf.security.jwt.JwtAccessTokenProvider;
import org.kirrilf.service.PostService;
import org.kirrilf.service.UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    public Post update(String text,String fileName, Long postId, HttpServletRequest request) {
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
        if(fileName != null){
         postFromBb.setFileName(fileName);
        }
        return postRepository.save(postFromBb);
    }

    @Override
    public PostDto like(Long id, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElse(null);
        if(post != null) {
            PostDto postDto = PostDto.fromPost(post);
            Set<User> likes = post.getLikes();
            User user = userService.findById(getUserIdByRequest(request));
            if(likes.contains(user)){
                likes.remove(user);
                postDto.setMeLiked(false);
            }else {
                likes.add(user);
                postDto.setMeLiked(true);
            }
            post.setLikes(likes);
            postDto.setCount(likes.size());
            postRepository.save(post);
            return postDto;
        }return null;
    }

    public Long getUserIdByRequest(HttpServletRequest request){
        return  userService.findByUsername(
                jwtAccessTokenProvider.getUsername(
                        jwtAccessTokenProvider.resolveToken(request)
                )
        ).getId();
    }


}
