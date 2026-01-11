package org.seongjki.sism.post.service;

import org.junit.jupiter.api.Test;
import org.seongjki.sism.domain.auth.dto.UserDetailDto;
import org.seongjki.sism.domain.post.dto.*;
import org.seongjki.sism.domain.post.entity.Post;
import org.seongjki.sism.domain.post.persist.PostRepository;
import org.seongjki.sism.domain.post.service.PostService;
import org.seongjki.sism.domain.user.UserRole;
import org.seongjki.sism.domain.user.entity.User;
import org.seongjki.sism.domain.user.persist.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class PostServiceTest {

    private PostService postService;

    private PostRepository postRepository;

    private UserRepository userRepository;

    private Clock clock;

    @Autowired
    public PostServiceTest(PostRepository postRepository, UserRepository userRepository) {
        LocalDateTime time = LocalDateTime.of(2026, 1, 1, 12, 30);
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.clock = Clock.fixed(time.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        this.postService = new PostService(postRepository, userRepository, clock);
    }

    @Test
    void 게시글_생성_성공() {
        //given
        User user = userRepository.save(User.builder()
                .nickname("test")
                .build());
        CreatePostRequest request = new CreatePostRequest("test", "test");

        //when
        PostDto res = postService.create(request,
                new UserDetailDto(user.getId(), "test@abc.com", "test", UserRole.ROLE_USER));

        //then
        assertThat(res).isEqualTo(new PostDto(
                res.id(),
                "test",
                "test",
                LocalDateTime.of(2026, 1, 1, 12, 30)
        ));
    }

    @Test
    void 게시글_업데이트_성공() {
        //given
        User user = userRepository.save(User.builder()
                .nickname("test")
                .build());
        Post post = postRepository.save(Post.builder()
                .title("test")
                .content("test")
                .viewCount(0)
                .user(user)
                .build());
        UpdatePostRequest request = new UpdatePostRequest("test", "test");

        //when
        UpdatePostResponse res = postService.update(post.getId(), request,
                new UserDetailDto(user.getId(), user.getEmail(), user.getPassword(), UserRole.ROLE_USER));

        //then
        assertThat(res).isEqualTo(new UpdatePostResponse(
                post.getId(),
                LocalDateTime.of(2026, 1, 1, 12, 30)
        ));
    }

    @Test
    void 게시글_삭제_성공() {
        //given
        User user = userRepository.save(User.builder()
                .nickname("test")
                .build());
        Post post = postRepository.save(Post.builder()
                .title("test")
                .content("test")
                .viewCount(0)
                .user(user)
                .build());

        //when
        DeletePostResponse res = postService.delete(post.getId(),
                new UserDetailDto(user.getId(), user.getEmail(), user.getPassword(), UserRole.ROLE_USER));

        //then
        assertThat(res).isEqualTo(new DeletePostResponse(
                post.getId(),
                LocalDateTime.of(2026, 1, 1, 12, 30)
        ));
    }

    @Test
    void 게시글_전체조회_성공() {
        //given
        User user = userRepository.save(User.builder()
                .nickname("test")
                .build());
        List<Post> postList = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            Post p = postRepository.save(Post.builder()
                    .title(String.format("test %s", i))
                    .content(String.format("test %s", i))
                    .user(user)
                    .createdAt(LocalDateTime.now(clock))
                    .viewCount(0)
                    .build());
            postList.add(p);
        }

        //when
        Page<PostDto> res = postService.getAllActivePosts(PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt"));

        //then
        assertThat(res.getContent()).isEqualTo(List.of(
                new PostDto(postList.get(0).getId(), "test 0", "test", LocalDateTime.of(2026, 1, 1, 12, 30)),
                new PostDto(postList.get(1).getId(), "test 1", "test", LocalDateTime.of(2026, 1, 1, 12, 30)),
                new PostDto(postList.get(2).getId(), "test 2", "test", LocalDateTime.of(2026, 1, 1, 12, 30)),
                new PostDto(postList.get(3).getId(), "test 3", "test", LocalDateTime.of(2026, 1, 1, 12, 30)),
                new PostDto(postList.get(4).getId(), "test 4", "test", LocalDateTime.of(2026, 1, 1, 12, 30))
                ));
    }

}
