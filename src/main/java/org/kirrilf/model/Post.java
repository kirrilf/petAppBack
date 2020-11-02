package org.kirrilf.model;


import javax.persistence.*;


@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String fileName;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Post{" +
                "text='" + text + '\'' +
                ", author=" + author +
                "} " + super.toString();
    }
}
