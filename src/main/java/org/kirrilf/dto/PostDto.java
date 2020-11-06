package org.kirrilf.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.kirrilf.model.Post;
import org.kirrilf.service.UserService;
import org.springframework.web.multipart.MultipartFile;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {

    private Long id;
    private String text;
    private Long authorId;
    private String fileName;
    private int count;
    private Boolean meLiked;

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
        postDto.setFileName(post.getFileName());
        postDto.setCount(post.getLikes().size());
        boolean meLiked = false;
        if(post.getLikes().contains(post.getAuthor())){
            meLiked = true;
        }
        postDto.setMeLiked(meLiked);
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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


    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
