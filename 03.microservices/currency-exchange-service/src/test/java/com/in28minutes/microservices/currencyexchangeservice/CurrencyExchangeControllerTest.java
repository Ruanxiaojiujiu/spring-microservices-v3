package com.in28minutes.microservices.currencyexchangeservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyExchangeController.class)
class CurrencyExchangeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CurrencyExchangeRepository repository;

	@Test
	void retrieveExchangeValue_validCurrencyPair_returnsExchangeData() throws Exception {
		CurrencyExchange exchange = new CurrencyExchange(10001L, "USD", "INR", BigDecimal.valueOf(65));
		exchange.setEnvironment("8000");
		when(repository.findByFromAndTo("USD", "INR")).thenReturn(exchange);

		mockMvc.perform(get("/currency-exchange/from/USD/to/INR"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(10001))
				.andExpect(jsonPath("$.from").value("USD"))
				.andExpect(jsonPath("$.to").value("INR"))
				.andExpect(jsonPath("$.conversionMultiple").value(65));
	}

	@Test
	void retrieveExchangeValue_anotherValidPair_returnsExchangeData() throws Exception {
		CurrencyExchange exchange = new CurrencyExchange(10002L, "EUR", "INR", BigDecimal.valueOf(75));
		exchange.setEnvironment("8000");
		when(repository.findByFromAndTo("EUR", "INR")).thenReturn(exchange);

		mockMvc.perform(get("/currency-exchange/from/EUR/to/INR"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.from").value("EUR"))
				.andExpect(jsonPath("$.to").value("INR"))
				.andExpect(jsonPath("$.conversionMultiple").value(75));
	}

	@Test
	void retrieveExchangeValue_unknownCurrencyPair_throwsRuntimeException() {
		when(repository.findByFromAndTo("UNKNOWN", "INR")).thenReturn(null);

		org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () ->
				mockMvc.perform(get("/currency-exchange/from/UNKNOWN/to/INR")));
	}
}
