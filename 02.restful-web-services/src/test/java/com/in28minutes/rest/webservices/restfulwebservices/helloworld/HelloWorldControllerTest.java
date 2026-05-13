package com.in28minutes.rest.webservices.restfulwebservices.helloworld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
		controllers = HelloWorldController.class,
		excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class}
)
class HelloWorldControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void helloWorld_returnsHelloWorldString() throws Exception {
		mockMvc.perform(get("/hello-world"))
				.andExpect(status().isOk())
				.andExpect(content().string("Hello World"));
	}

	@Test
	void helloWorldBean_returnsBeanWithMessage() throws Exception {
		mockMvc.perform(get("/hello-world-bean"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Hello World"));
	}

	@Test
	void helloWorldPathVariable_returnsPersonalizedGreeting() throws Exception {
		mockMvc.perform(get("/hello-world/path-variable/Ranga"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Hello World, Ranga"));
	}

	@Test
	void helloWorldPathVariable_differentName_returnsPersonalizedGreeting() throws Exception {
		mockMvc.perform(get("/hello-world/path-variable/John"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Hello World, John"));
	}

	@Test
	void helloWorldInternationalized_noLocale_returnsDefaultEnglishMessage() throws Exception {
		mockMvc.perform(get("/hello-world-internationalized"))
				.andExpect(status().isOk())
				.andExpect(content().string("Good Morning"));
	}

	@Test
	void helloWorldInternationalized_dutchLocale_returnsDutchMessage() throws Exception {
		mockMvc.perform(get("/hello-world-internationalized")
						.header("Accept-Language", "nl"))
				.andExpect(status().isOk())
				.andExpect(content().string("Goedemorgen"));
	}
}
