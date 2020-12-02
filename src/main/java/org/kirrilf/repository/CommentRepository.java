package org.kirrilf.repository;

import org.jetbrains.annotations.NotNull;
import org.kirrilf.model.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @NotNull List<Comment> findAll();
    List<Comment> getCommentsByPostId(Long id);
}
