package com.in28minutes.rest.webservices.restfulwebservices.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoServiceTest {

	private final UserDaoService service = new UserDaoService();

	@Test
	void findAll_returnsAtLeastThreeUsers() {
		List<User> users = service.findAll();
		assertNotNull(users);
		assertTrue(users.size() >= 3, "Expected at least 3 seeded users");
	}

	@Test
	void findOne_knownId_returnsCorrectUser() {
		User user = service.findOne(1);
		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("Adam", user.getName());
	}

	@Test
	void findOne_unknownId_returnsNull() {
		User user = service.findOne(99999);
		assertNull(user);
	}

	@Test
	void save_assignsIdAndAddsUserToList() {
		int sizeBefore = service.findAll().size();
		User newUser = new User(null, "TestUser", LocalDate.now().minusYears(22));

		User savedUser = service.save(newUser);

		assertNotNull(savedUser.getId());
		assertTrue(savedUser.getId() > 0);
		assertEquals("TestUser", savedUser.getName());
		assertEquals(sizeBefore + 1, service.findAll().size());
	}

	@Test
	void deleteById_removesExistingUser() {
		// Save a new user so we have a known ID to delete without disturbing seeded data
		User toDelete = new User(null, "ToDelete", LocalDate.now().minusYears(30));
		User saved = service.save(toDelete);
		int idToDelete = saved.getId();

		int sizeBefore = service.findAll().size();
		service.deleteById(idToDelete);

		assertEquals(sizeBefore - 1, service.findAll().size());
		assertNull(service.findOne(idToDelete));
	}

	@Test
	void deleteById_nonExistentId_doesNotThrowAndSizeUnchanged() {
		int sizeBefore = service.findAll().size();
		service.deleteById(99999);
		assertEquals(sizeBefore, service.findAll().size());
	}
}
