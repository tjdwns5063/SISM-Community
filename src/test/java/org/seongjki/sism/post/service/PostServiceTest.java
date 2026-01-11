package org.seongjki.sism.post.service;

import org.junit.jupiter.api.Test;
import org.seongjki.sism.domain.auth.dto.UserDetailDto;
import org.seongjki.sism.domain.post.dto.CreatePostRequest;
import org.seongjki.sism.domain.post.dto.PostDto;
import org.seongjki.sism.domain.post.persist.PostRepository;
import org.seongjki.sism.domain.post.service.PostService;
import org.seongjki.sism.domain.user.UserRole;
import org.seongjki.sism.domain.user.entity.User;
import org.seongjki.sism.domain.user.persist.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

}
