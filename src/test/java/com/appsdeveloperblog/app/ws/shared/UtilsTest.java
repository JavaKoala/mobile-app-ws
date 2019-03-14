package com.appsdeveloperblog.app.ws.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class UtilsTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGenerateUserId() {
		Utils utils = new Utils();
		String userId = utils.generateUserId(30);
		String userId2 = utils.generateAddressId(30);

		assertNotNull(userId);
		assertNotNull(userId2);

		assertTrue(userId.length() == 30);
		assertTrue(!userId.equalsIgnoreCase(userId2));

	}

}
