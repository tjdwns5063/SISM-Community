package org.seongjki.sism.domain.post.persist;

import org.seongjki.sism.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUser_IdAndDeletedAtIsNull(Long id, Long userId);

}
