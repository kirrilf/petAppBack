package org.kirrilf.repository;

import org.kirrilf.model.Post;
import org.kirrilf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User author);

}
