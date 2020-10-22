package org.kirrilf.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.kirrilf.model.Post;
import org.kirrilf.model.User;
import org.kirrilf.repository.UserRepository;
import org.kirrilf.service.UserService;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {

    private Long id;
    private String text;
    private Long authorId;



    public Post toPost(){
        Post post = new Post();
        post.setId(id);
        post.setText(text);
        return post;
    }

    public static PostDto fromPost(Post post){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setText(post.getText());
        postDto.setAuthorId(post.getAuthor().getId());
        return postDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}