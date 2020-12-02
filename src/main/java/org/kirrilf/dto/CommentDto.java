package org.kirrilf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.kirrilf.model.Comment;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {

    private Long id;
    private String text;
    private Long authorId;
    private Long updateDate;


    public static CommentDto fromComment(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorId(comment.getAuthor().getId());
        commentDto.setUpdateDate(comment.getUpdated().getTime()/1000);
        return commentDto;
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

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }
}
