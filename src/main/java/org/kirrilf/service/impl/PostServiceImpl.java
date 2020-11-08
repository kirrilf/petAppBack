package org.kirrilf.service.impl;

import org.kirrilf.dto.PostDto;
import org.kirrilf.model.Image;
import org.kirrilf.model.Post;
import org.kirrilf.model.Status;
import org.kirrilf.model.User;
import org.kirrilf.repository.ImageRepository;
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
    private final ImageRepository imageRepository;

    public PostServiceImpl(JwtAccessTokenProvider jwtAccessTokenProvider,
                           PostRepository postRepository,
                           UserService userService,
                           ImageRepository imageRepository) {
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.postRepository = postRepository;
        this.userService = userService;
        this.imageRepository = imageRepository;

    }

    @Override
    public Post add(Post post, List<Image> images, HttpServletRequest request) {

        post.setStatus(Status.ACTIVE);
        post.setCreated(new Date());
        post.setUpdated(new Date());
        post.setAuthor(userService.findByUsername(
                jwtAccessTokenProvider.getUsername(
                        jwtAccessTokenProvider.resolveToken(request))));
        Post createPost = postRepository.save(post);
        for (Image i : images) {
            i.setCreated(new Date());
            i.setUpdated(new Date());
            i.setPost(createPost);
            imageRepository.save(i);
        }
        return createPost;
    }

    @Override
    public List<Image> getAllFileNamesByPost(Post post) {
        return imageRepository.findByPost(post);
    }

    @Override
    public Post getOnePost(Long postID) {
        return postRepository.findById(postID).orElse(null);
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
        deleteAllImagesByPost(postRepository.findById(id).orElse(null));
        postRepository.deleteById(id);
    }

    @Override
    public User getUserByRequest(HttpServletRequest request){
        return userService.findByUsername(
                jwtAccessTokenProvider.getUsername(
                        jwtAccessTokenProvider.resolveToken(request)
                )
        );
    }


    @Override
    public Post update(String text, Long postId, HttpServletRequest request) {
        Long userId =getUserByRequest(request).getId();

        Post postFromBb = postRepository.findById(postId).orElse(null);
        if (postFromBb == null || !postFromBb.getAuthor().getId().equals(userId)) {
            return null;
        }

        postFromBb.setText(text);
        postFromBb.setUpdated(new Date());
        return postRepository.save(postFromBb);
    }

    @Override
    public void deleteAllImagesByPost(Post post) {
        //TODO delete images in storage
        List<Image> images = imageRepository.findByPost(post);
        for (Image i : images) {
            imageRepository.delete(i);
        }
    }

    @Override
    public PostDto like(Long id, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            User user = userService.findById(getUserByRequest(request).getId());
            PostDto postDto = PostDto.fromPost(post, imageRepository.findByPost(post), user);
            Set<User> likes = post.getLikes();
            if (likes.contains(user)) {
                likes.remove(user);
                postDto.setMeLiked(false);
            } else {
                likes.add(user);
                postDto.setMeLiked(true);
            }
            post.setLikes(likes);
            postDto.setCount(likes.size());
            postRepository.save(post);
            return postDto;
        }
        return null;
    }




}
