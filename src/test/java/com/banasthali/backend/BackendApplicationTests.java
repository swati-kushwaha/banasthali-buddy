package com.banasthali.backend;

import com.banasthali.backend.repository.ItemRepository;
import com.banasthali.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

	// Mock repositories so Spring doesn't try to connect to MongoDB
	@MockBean
	private UserRepository userRepository;

	@MockBean
	private ItemRepository itemRepository;

	@Test
	void contextLoads() {
		// Context loads with mocked repositories
	}

}
