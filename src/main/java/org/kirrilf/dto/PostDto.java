package org.kirrilf.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.kirrilf.model.Image;
import org.kirrilf.model.Post;
import org.kirrilf.model.User;
import org.kirrilf.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {

    private Long id;
    private String text;
    private Long authorId;
    private int count;
    private Boolean meLiked;
    private List<String> fileNames;


    public Post toPost(){
        Post post = new Post();
        post.setId(id);
        post.setText(text);
        return post;
    }

    public static PostDto fromPost(Post post, List<Image> images, User me){
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setText(post.getText());
        postDto.setAuthorId(post.getAuthor().getId());
        postDto.setCount(post.getLikes().size());
        boolean meLiked = false;
        if(post.getLikes().contains(me)){
            meLiked = true;
        }
        postDto.setMeLiked(meLiked);
        List<String> fileNames = new LinkedList<>();
        for(Image i : images){
            fileNames.add(i.getFileName());
        }
        postDto.setFileNames(fileNames);
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


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getMeLiked() {
        return meLiked;
    }

    public void setMeLiked(Boolean meLiked) {
        this.meLiked = meLiked;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
