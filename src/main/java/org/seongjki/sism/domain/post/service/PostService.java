package org.seongjki.sism.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.seongjki.sism.common.HttpException;
import org.seongjki.sism.domain.auth.dto.UserDetailDto;
import org.seongjki.sism.domain.post.dto.CreatePostRequest;
import org.seongjki.sism.domain.post.dto.PostDto;
import org.seongjki.sism.domain.post.dto.UpdatePostRequest;
import org.seongjki.sism.domain.post.dto.UpdatePostResponse;
import org.seongjki.sism.domain.post.entity.Post;
import org.seongjki.sism.domain.post.persist.PostRepository;
import org.seongjki.sism.domain.user.entity.User;
import org.seongjki.sism.domain.user.persist.UserRepository;
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

}
