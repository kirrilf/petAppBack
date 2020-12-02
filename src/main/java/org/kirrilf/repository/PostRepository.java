package org.kirrilf.repository;

import org.jetbrains.annotations.NotNull;
import org.kirrilf.model.Post;
import org.kirrilf.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User author);

    /*@EntityGraph(attributePaths = {"comments"})
    @NotNull
    List<Post> findAll();*/
}

