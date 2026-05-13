package com.in28minutes.microservices.limitsservice.controller;

import com.in28minutes.microservices.limitsservice.configuration.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LimitsController.class)
class LimitsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private Configuration configuration;

	@Test
	void retrieveLimits_returnsConfiguredMinimumAndMaximum() throws Exception {
		when(configuration.getMinimum()).thenReturn(3);
		when(configuration.getMaximum()).thenReturn(997);

		mockMvc.perform(get("/limits"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.minimum").value(3))
				.andExpect(jsonPath("$.maximum").value(997));
	}

	@Test
	void retrieveLimits_differentConfiguration_returnsUpdatedValues() throws Exception {
		when(configuration.getMinimum()).thenReturn(1);
		when(configuration.getMaximum()).thenReturn(1000);

		mockMvc.perform(get("/limits"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.minimum").value(1))
				.andExpect(jsonPath("$.maximum").value(1000));
	}
}
