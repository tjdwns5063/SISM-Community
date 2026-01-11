package org.seongjki.sism.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.seongjki.sism.common.HttpException;
import org.seongjki.sism.domain.auth.dto.UserDetailDto;
import org.seongjki.sism.domain.post.dto.*;
import org.seongjki.sism.domain.post.entity.Post;
import org.seongjki.sism.domain.post.persist.PostRepository;
import org.seongjki.sism.domain.user.entity.User;
import org.seongjki.sism.domain.user.persist.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final Clock clock;

    @Transactional
    public PostDto create(CreatePostRequest request, UserDetailDto userDetailDto) {
        User user = userRepository.findById(userDetailDto.getId())
                .orElseThrow(() -> new HttpException(HttpStatusCode.valueOf(404), "해당 유저가 존재하지 않습니다."));

        Post post = postRepository.save(Post.builder()
                .title(request.title())
                .content(request.content())
                .viewCount(0)
                .createdAt(LocalDateTime.now(clock))
                .updatedAt(LocalDateTime.now(clock))
                .user(user)
                .build());

        return new PostDto(post.getId(), post.getTitle(), user.getNickname(), post.getCreatedAt());
    }

    @Transactional
    public UpdatePostResponse update(long postId, UpdatePostRequest request, UserDetailDto userDetailDto) {
        User user = userRepository.findById(userDetailDto.getId())
                .orElseThrow(() -> new HttpException(HttpStatusCode.valueOf(404), "해당 유저가 존재하지 않습니다."));

        Post post = postRepository.findByIdAndUser_IdAndDeletedAtIsNull(postId, user.getId())
                .orElseThrow(() -> new HttpException(HttpStatusCode.valueOf(404), "해당 게시글이 존재하지 않습니다."));

        post.update(request.title(), request.content(), clock);

        return new UpdatePostResponse(post.getId(), post.getUpdatedAt());
    }

    @Transactional
    public DeletePostResponse delete(Long postId, UserDetailDto userDetailDto) {
        User user = userRepository.findById(userDetailDto.getId())
                .orElseThrow(() -> new HttpException(HttpStatusCode.valueOf(404), "해당 유저가 존재하지 않습니다."));

        Post post = postRepository.findByIdAndUser_IdAndDeletedAtIsNull(postId, user.getId())
                .orElseThrow(() -> new HttpException(HttpStatusCode.valueOf(404), "해당 게시글이 존재하지 않습니다."));

        post.delete(clock);

        return new DeletePostResponse(post.getId(), post.getDeletedAt());
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getAllActivePosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByDeletedAtIsNull(pageable);

        return posts.map(p -> new PostDto(p.getId(), p.getTitle(), p.getUser().getNickname(), p.getCreatedAt()));
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPostDetailById(Long postId) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new HttpException(HttpStatusCode.valueOf(404), "해당 게시글을 찾을 수 없습니다."));
        return new PostDetailDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }

}
