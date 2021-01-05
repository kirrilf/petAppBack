package org.kirrilf.repository;

import org.jetbrains.annotations.NotNull;
import org.kirrilf.model.Post;
import org.kirrilf.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthor(User author);

    @NotNull
    Page<Post> findAll(@NotNull Pageable pageable);

    /*@EntityGraph(attributePaths = {"comments"})
    @NotNull
    List<Post> findAll();*/
}

