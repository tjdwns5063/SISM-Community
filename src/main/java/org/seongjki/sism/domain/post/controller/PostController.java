package org.seongjki.sism.domain.post.controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.seongjki.sism.common.dto.ApiResponse;
import org.seongjki.sism.domain.auth.dto.UserDetailDto;
import org.seongjki.sism.domain.post.dto.CreatePostRequest;
import org.seongjki.sism.domain.post.dto.PostDto;
import org.seongjki.sism.domain.post.dto.UpdatePostRequest;
import org.seongjki.sism.domain.post.dto.UpdatePostResponse;
import org.seongjki.sism.domain.post.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<PostDto> create(@RequestBody @Valid CreatePostRequest request, @AuthenticationPrincipal UserDetailDto principal) {
        return ApiResponse.success(postService.create(request, principal),
                "게시글 생성에 성공했습니다.");
    }

    @PatchMapping("{postId}")
    public ApiResponse<UpdatePostResponse> update(
            @PathVariable Long postId,
            @RequestBody @Valid UpdatePostRequest request,
            @AuthenticationPrincipal UserDetailDto principal) {
        return ApiResponse.success(postService.update(postId, request, principal), "게시글 업데이트에 성공했습니다.");
    }

}
