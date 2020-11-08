package org.kirrilf.repository;

import org.kirrilf.model.Image;
import org.kirrilf.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPost(Post post);
}
