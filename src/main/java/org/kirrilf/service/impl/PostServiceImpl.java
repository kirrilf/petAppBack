package org.kirrilf.service.impl;

import org.kirrilf.dto.PostDto;
import org.kirrilf.dto.PostPageDto;
import org.kirrilf.model.Image;
import org.kirrilf.model.Post;
import org.kirrilf.model.Status;
import org.kirrilf.model.User;
import org.kirrilf.repository.CommentRepository;
import org.kirrilf.repository.ImageRepository;
import org.kirrilf.repository.PostRepository;
import org.kirrilf.security.jwt.JwtAccessTokenProvider;
import org.kirrilf.service.PostService;
import org.kirrilf.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {

    private final JwtAccessTokenProvider jwtAccessTokenProvider;
    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;


    public PostServiceImpl(JwtAccessTokenProvider jwtAccessTokenProvider,
                           PostRepository postRepository,
                           UserService userService,
                           ImageRepository imageRepository, CommentRepository commentRepository) {
        this.jwtAccessTokenProvider = jwtAccessTokenProvider;
        this.postRepository = postRepository;
        this.userService = userService;
        this.imageRepository = imageRepository;
        this.commentRepository = commentRepository;
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
    public PostPageDto getAll(Pageable pageable, HttpServletRequest request) {


        Page<Post> postsPage = postRepository.findAll(pageable);
        List<Post> posts = postsPage.getContent();
        List<PostDto> postsDto = new ArrayList<>();
        for (Post post : posts) {
            postsDto.add(PostDto.fromPost(post, getAllFileNamesByPost(post),
                    userService.getUserByRequest(request),
                    commentRepository.findTop3ByPostOrderById(post)));
        }

        return new PostPageDto(postsDto, pageable.getPageNumber(), postsPage.getTotalPages());
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
    public Post update(String text, Long postId, HttpServletRequest request) {
        Long userId = userService.getUserByRequest(request).getId();

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
            User user = userService.findById(userService.getUserByRequest(request).getId());
            PostDto postDto = PostDto.fromPost(post, imageRepository.findByPost(post), user, commentRepository.findTop3ByPostOrderById(post));
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
