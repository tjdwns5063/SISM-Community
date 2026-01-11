package org.seongjki.sism.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seongjki.sism.domain.auth.controller.AuthController;
import org.seongjki.sism.domain.auth.dto.SignInRequest;
import org.seongjki.sism.domain.auth.dto.SignInResponse;
import org.seongjki.sism.domain.auth.service.AuthService;
import org.seongjki.sism.domain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriPort = 80)
@ExtendWith(RestDocumentationExtension.class)
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void signIn() throws Exception {
        //given
        BDDMockito.given(authService.signIn(BDDMockito.any(), BDDMockito.any(), BDDMockito.any()))
                .willReturn(new SignInResponse(
                        new UserDto(1L, "n1", "abc@cde.com", "n1", "01012345678", LocalDateTime.now()),
                        "sessionKey"
                ));

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignInRequest("abc@cde.com", "1234")))
                ).andDo(print())
                .andExpect(status().isOk())
                .andDo(getAuthGetHandler());

        //then
        BDDMockito.then(authService).should().signIn(BDDMockito.any(), BDDMockito.any(), BDDMockito.any());
    }

    RestDocumentationResultHandler getAuthGetHandler() {
        return document("auth/get",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                )
        );
    }

}

