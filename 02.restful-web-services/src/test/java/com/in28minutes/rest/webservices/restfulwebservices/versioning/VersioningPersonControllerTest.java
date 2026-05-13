package com.in28minutes.rest.webservices.restfulwebservices.versioning;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
		controllers = VersioningPersonController.class,
		excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class}
)
class VersioningPersonControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void v1Person_urlVersioning_returnsSingleNameField() throws Exception {
		mockMvc.perform(get("/v1/person"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Bob Charlie"));
	}

	@Test
	void v2Person_urlVersioning_returnsStructuredName() throws Exception {
		mockMvc.perform(get("/v2/person"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name.firstName").value("Bob"))
				.andExpect(jsonPath("$.name.lastName").value("Charlie"));
	}

	@Test
	void v1Person_requestParamVersioning_returnsSingleNameField() throws Exception {
		mockMvc.perform(get("/person").param("version", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Bob Charlie"));
	}

	@Test
	void v2Person_requestParamVersioning_returnsStructuredName() throws Exception {
		mockMvc.perform(get("/person").param("version", "2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name.firstName").value("Bob"))
				.andExpect(jsonPath("$.name.lastName").value("Charlie"));
	}

	@Test
	void v1Person_headerVersioning_returnsSingleNameField() throws Exception {
		mockMvc.perform(get("/person/header").header("X-API-VERSION", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Bob Charlie"));
	}

	@Test
	void v2Person_headerVersioning_returnsStructuredName() throws Exception {
		mockMvc.perform(get("/person/header").header("X-API-VERSION", "2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name.firstName").value("Bob"))
				.andExpect(jsonPath("$.name.lastName").value("Charlie"));
	}

	@Test
	void v1Person_acceptHeaderVersioning_returnsSingleNameField() throws Exception {
		mockMvc.perform(get("/person/accept")
						.accept("application/vnd.company.app-v1+json"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Bob Charlie"));
	}

	@Test
	void v2Person_acceptHeaderVersioning_returnsStructuredName() throws Exception {
		mockMvc.perform(get("/person/accept")
						.accept("application/vnd.company.app-v2+json"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name.firstName").value("Bob"))
				.andExpect(jsonPath("$.name.lastName").value("Charlie"));
	}
}
