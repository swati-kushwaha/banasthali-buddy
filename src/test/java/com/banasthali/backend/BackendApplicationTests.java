package com.banasthali.backend;

import com.banasthali.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

	// Mock the repository so Spring doesn't try to connect to MongoDB
	@MockBean
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		// Context loads with mocked repository
	}

}
