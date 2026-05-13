package com.in28minutes.microservices.currencyexchangeservice;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangeTest {

	@Test
	void constructor_setsAllFields() {
		CurrencyExchange exchange = new CurrencyExchange(10001L, "USD", "INR", BigDecimal.valueOf(65));

		assertEquals(10001L, exchange.getId());
		assertEquals("USD", exchange.getFrom());
		assertEquals("INR", exchange.getTo());
		assertEquals(BigDecimal.valueOf(65), exchange.getConversionMultiple());
	}

	@Test
	void defaultConstructor_createsInstanceWithNullFields() {
		CurrencyExchange exchange = new CurrencyExchange();

		assertNull(exchange.getId());
		assertNull(exchange.getFrom());
		assertNull(exchange.getTo());
		assertNull(exchange.getConversionMultiple());
		assertNull(exchange.getEnvironment());
	}

	@Test
	void setters_updateFields() {
		CurrencyExchange exchange = new CurrencyExchange();
		exchange.setId(10002L);
		exchange.setFrom("EUR");
		exchange.setTo("USD");
		exchange.setConversionMultiple(BigDecimal.valueOf(1.08));
		exchange.setEnvironment("8000");

		assertEquals(10002L, exchange.getId());
		assertEquals("EUR", exchange.getFrom());
		assertEquals("USD", exchange.getTo());
		assertEquals(BigDecimal.valueOf(1.08), exchange.getConversionMultiple());
		assertEquals("8000", exchange.getEnvironment());
	}

	@Test
	void setEnvironment_updatesEnvironmentField() {
		CurrencyExchange exchange = new CurrencyExchange(10001L, "USD", "INR", BigDecimal.valueOf(65));
		exchange.setEnvironment("9000");

		assertEquals("9000", exchange.getEnvironment());
	}
}
