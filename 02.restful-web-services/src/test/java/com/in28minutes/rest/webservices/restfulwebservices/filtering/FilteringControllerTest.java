package com.in28minutes.rest.webservices.restfulwebservices.filtering;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
		controllers = FilteringController.class,
		excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class}
)
class FilteringControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void filtering_returnsField1AndField3_excludesField2() throws Exception {
		mockMvc.perform(get("/filtering"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.field1").value("value1"))
				.andExpect(jsonPath("$.field3").value("value3"))
				.andExpect(jsonPath("$.field2").doesNotExist());
	}

	@Test
	void filteringList_returnsField2AndField3_excludesField1() throws Exception {
		mockMvc.perform(get("/filtering-list"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].field2").value("value2"))
				.andExpect(jsonPath("$[0].field3").value("value3"))
				.andExpect(jsonPath("$[0].field1").doesNotExist())
				.andExpect(jsonPath("$[1].field2").value("value5"))
				.andExpect(jsonPath("$[1].field3").value("value6"))
				.andExpect(jsonPath("$[1].field1").doesNotExist());
	}
}
