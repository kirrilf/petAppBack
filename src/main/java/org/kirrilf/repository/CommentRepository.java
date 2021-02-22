package org.kirrilf.repository;

import org.jetbrains.annotations.NotNull;
import org.kirrilf.model.Comment;
import org.kirrilf.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    @NotNull
    Page<Comment> findCommentsByPostId(@NotNull Pageable pageable, Long id);

    List<Comment> findTop3ByPostOrderById(Post post);

    @NotNull
    Page<Comment> findAll(@NotNull Pageable pageable);


}
