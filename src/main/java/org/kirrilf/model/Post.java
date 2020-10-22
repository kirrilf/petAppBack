package org.kirrilf.model;


import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Post extends BaseEntity {

    @Column(name = "message")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

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
}
