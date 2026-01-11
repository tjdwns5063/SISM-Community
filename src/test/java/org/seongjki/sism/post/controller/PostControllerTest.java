package org.seongjki.sism.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seongjki.sism.domain.auth.dto.UserDetailDto;
import org.seongjki.sism.domain.post.controller.PostController;
import org.seongjki.sism.domain.post.dto.*;
import org.seongjki.sism.domain.post.entity.Post;
import org.seongjki.sism.domain.post.service.PostService;
import org.seongjki.sism.domain.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 게시글_생성() throws Exception {
        //given
        CreatePostRequest request = new CreatePostRequest(
                "test",
                "test"
        );
        UserDetailDto mockUser = UserDetailDto.builder()
                .id(1L)
                .email("user@example.com")
                .role(UserRole.ROLE_USER)
                .password("password")
                .build();
        BDDMockito.given(postService.create(BDDMockito.any(), BDDMockito.any())).willReturn(
                new PostDto(1L, "test", "test", LocalDateTime.now())
        );
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                mockUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/post")
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getPostPostHandler());

        //then
        BDDMockito.then(postService).should().create(BDDMockito.any(), BDDMockito.any());
    }

    RestDocumentationResultHandler getPostPostHandler() {
        return document("post/post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
                )
        );
    }

    @Test
    void 게시글_업데이트() throws Exception {
        //given
        UpdatePostRequest request = new UpdatePostRequest(
                "test",
                "test"
        );
        UserDetailDto mockUser = UserDetailDto.builder()
                .id(1L)
                .email("user@example.com")
                .role(UserRole.ROLE_USER)
                .password("password")
                .build();
        BDDMockito.given(postService.update(BDDMockito.anyLong(), BDDMockito.any(), BDDMockito.any())).willReturn(
                new UpdatePostResponse(1L, LocalDateTime.now())
        );
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                mockUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/post/{postId}", 1L)
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getPostPatchHandler());

        //then
        BDDMockito.then(postService).should().update(BDDMockito.anyLong(), BDDMockito.any(), BDDMockito.any());

    }

    RestDocumentationResultHandler getPostPatchHandler() {
        return document("post/patch",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("postId").description("게시글 아이디")),
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
                )
        );
    }

    @Test
    void 게시글_삭제() throws Exception {
        //given
        UserDetailDto mockUser = UserDetailDto.builder()
                .id(1L)
                .email("user@example.com")
                .role(UserRole.ROLE_USER)
                .password("password")
                .build();
        BDDMockito.given(postService.delete(BDDMockito.anyLong(), BDDMockito.any())).willReturn(
                new DeletePostResponse(1L, LocalDateTime.now())
        );
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                mockUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/post/{postId}", 1L)
                        .with(authentication(auth)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getPostDeleteHandler());

        //then
        BDDMockito.then(postService).should().delete(BDDMockito.anyLong(), BDDMockito.any());
    }

    RestDocumentationResultHandler getPostDeleteHandler() {
        return document("post/delete",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("postId").description("게시글 아이디"))
        );
    }

    @Test
    void 게시글_전체조회() throws Exception {
        //given
        BDDMockito.given(postService.getAllActivePosts(BDDMockito.any())).willReturn(
                new PageImpl<>(List.of(
                        new PostDto(1L, "test 0", "test", LocalDateTime.now()),
                        new PostDto(2L, "test 1", "test", LocalDateTime.now()),
                        new PostDto(3L, "test 2", "test", LocalDateTime.now()),
                        new PostDto(4L, "test 3", "test", LocalDateTime.now()),
                        new PostDto(5L, "test 4", "test", LocalDateTime.now())
                ), PageRequest.of(0, 20, Sort.Direction.DESC, "createdAt"), 5L)
        );

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/post")
                        .queryParam("page", "0")
                        .queryParam("size", "20")
                        .queryParam("sort", "createdAt,desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(getAllActivePostGetHandler());

        //then
        BDDMockito.then(postService).should().getAllActivePosts(BDDMockito.any());
    }

    RestDocumentationResultHandler getAllActivePostGetHandler() {
        return document("get-all-active-post/get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                        parameterWithName("size").description("페이지당 게시글 수"),
                        parameterWithName("sort").description("정렬 기준 (예: id,desc)")
                ));
    }

}
