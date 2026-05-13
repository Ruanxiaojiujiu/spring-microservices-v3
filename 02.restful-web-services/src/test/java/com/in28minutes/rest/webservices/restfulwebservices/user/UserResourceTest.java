package com.in28minutes.rest.webservices.restfulwebservices.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
		controllers = UserResource.class,
		excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class}
)
class UserResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserDaoService userDaoService;

	private User user1;
	private User user2;

	@BeforeEach
	void setUp() {
		user1 = new User(1, "Adam", LocalDate.now().minusYears(30));
		user2 = new User(2, "Eve", LocalDate.now().minusYears(25));
	}

	@Test
	void retrieveAllUsers_returnsListOfUsers() throws Exception {
		when(userDaoService.findAll()).thenReturn(List.of(user1, user2));

		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(2))
				.andExpect(jsonPath("$[0].name").value("Adam"))
				.andExpect(jsonPath("$[1].name").value("Eve"));
	}

	@Test
	void retrieveUser_validId_returnsUserWithHateoasLink() throws Exception {
		when(userDaoService.findOne(1)).thenReturn(user1);

		mockMvc.perform(get("/users/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Adam"))
				.andExpect(jsonPath("$._links.all-users").exists());
	}

	@Test
	void retrieveUser_invalidId_returns404() throws Exception {
		when(userDaoService.findOne(999)).thenReturn(null);

		mockMvc.perform(get("/users/999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteUser_validId_returns200() throws Exception {
		doNothing().when(userDaoService).deleteById(1);

		mockMvc.perform(delete("/users/1"))
				.andExpect(status().isOk());

		verify(userDaoService, times(1)).deleteById(1);
	}

	@Test
	void createUser_validUser_returns201WithLocation() throws Exception {
		User newUser = new User(null, "Alice", LocalDate.now().minusYears(28));
		User savedUser = new User(4, "Alice", LocalDate.now().minusYears(28));
		when(userDaoService.save(any(User.class))).thenReturn(savedUser);

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newUser)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", org.hamcrest.Matchers.containsString("/users/4")));
	}

	@Test
	void createUser_nameTooShort_returns400() throws Exception {
		User invalidUser = new User(null, "A", LocalDate.now().minusYears(28));

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidUser)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createUser_futureBirthDate_returns400() throws Exception {
		User invalidUser = new User(null, "Alice", LocalDate.now().plusYears(1));

		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidUser)))
				.andExpect(status().isBadRequest());
	}
}
