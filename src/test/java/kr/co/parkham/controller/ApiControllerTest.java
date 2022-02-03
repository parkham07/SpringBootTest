package kr.co.parkham.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import kr.co.parkham.ApiDocumentUtils;
import kr.co.parkham.config.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static kr.co.parkham.ApiDocumentUtils.getDocumentRequest;
import static kr.co.parkham.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class ApiControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JwtUtil jwtUtil;

	@Test
	public void authenticate() throws Exception {
		JsonObject param = new JsonObject();

		param.addProperty("userName", "test");
		param.addProperty("password", "test");

		ResultActions result = this.mockMvc.perform(
				post("/authenticate")
						.content(param.toString())
						.contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.107 Safari/537.36")
						.accept(MediaType.APPLICATION_JSON)
		);

		result.andExpect(status().isOk())
				.andDo(document("authenticate", // (4)
						getDocumentRequest(),
						getDocumentResponse(),
						requestHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON_VALUE)
						),
						requestFields(
								fieldWithPath("userName").type(JsonFieldType.STRING).description("ID"),
								fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
						),
						responseFields(
								fieldWithPath("token").type(JsonFieldType.STRING).description("토큰 값")
						)
				));
	}
}
