package org.kirrilf.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();

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

    public Set<User> getLikes() {
        return likes;
    }

    public void setLikes(Set<User> likes) {
        this.likes = likes;
    }


    @Override
    public String toString() {
        return "Post{" +
                "text='" + text + '\'' +
                ", author=" + author +
                "} " + super.toString();
    }
}
