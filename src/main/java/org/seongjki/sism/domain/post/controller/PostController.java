package org.seongjki.sism.domain.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.seongjki.sism.common.dto.ApiResponse;
import org.seongjki.sism.domain.auth.dto.UserDetailDto;
import org.seongjki.sism.domain.post.dto.*;
import org.seongjki.sism.domain.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @DeleteMapping("{postId}")
    public ApiResponse<DeletePostResponse> delete(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailDto principal
    ) {
        return ApiResponse.success(postService.delete(postId, principal), "게시글 삭제에 성공했습니다.");
    }

    @GetMapping
    public ApiResponse<Page<PostDto>> getAllPosts(
            @PageableDefault(size = 20, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(postService.getAllActivePosts(pageable), "게시글 조회에 성공했습니다.");
    }

}
