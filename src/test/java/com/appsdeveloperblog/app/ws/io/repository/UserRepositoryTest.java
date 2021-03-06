package com.appsdeveloperblog.app.ws.io.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;

	static boolean recordsCreated = false;

	@BeforeEach
	void setUp() throws Exception {
		if(!recordsCreated) {
			createRecords();
		}
	}

	@Test
	void testGetVerifiedUsers() {
		Pageable pageableRequest = PageRequest.of(1, 1);
		Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
		assertNotNull(pages);

		List<UserEntity> userEntities = pages.getContent();
		assertNotNull(userEntities);
		assertTrue(userEntities.size() == 1);
	}

	@Test
	void testFindUserByFirstName() {
		String firstName="Test";
		List<UserEntity> users = userRepository.findUserbyFirstName(firstName);
		assertNotNull(users);
		assertTrue(users.size() == 2);

		UserEntity user = users.get(0);
		assertTrue(user.getFirstName().equals(firstName));
	}

	@Test
	void testFindUserByLastName() {
		String lastName="User";
		List<UserEntity> users = userRepository.findUserbyLastName(lastName);
		assertNotNull(users);
		assertTrue(users.size() == 2);

		UserEntity user = users.get(0);
		assertTrue(user.getLastName().equals(lastName));
	}

	@Test
	void testFindUsersByKeyword() {
		String keyword="Us";
		List<UserEntity> users = userRepository.findUsersByKeyword(keyword);
		assertNotNull(users);
		assertTrue(users.size() == 2);

		UserEntity user = users.get(0);
		assertTrue(user.getLastName().contains(keyword) ||
				   user.getFirstName().contains(keyword));
	}

	@Test
	void testFindUserFirstNameLastNameByKeyword() {
		String keyword="Us";
		List<Object[]> users = userRepository.findUserFirstNameLastNameByKeyword(keyword);
		assertNotNull(users);
		assertTrue(users.size() == 2);

		Object[] user = users.get(0);

		String userFirstName = String.valueOf(user[0]);
		String userLastName = String.valueOf(user[1]);

		assertNotNull(userFirstName);
		assertNotNull(userLastName);

		System.out.println(userFirstName);
		System.out.println(userLastName);
	}

	@Test
	void testFindUserEntityByUserId() {
		String userId = "abc123";
		UserEntity userEntity =  userRepository.findUserEntityByUserId(userId);

		assertNotNull(userEntity);
		assertTrue(userEntity.getUserId().equals(userId));
	}

	@Test
	void testGetUserEntityFullNameById() {
		String userId = "abc123";
		List<Object[]> records = userRepository.getUserEntityFullNameById(userId);

		assertNotNull(records);
		assertTrue(records.size() == 1);

		Object[] userDetails = records.get(0);

		String firstName = String.valueOf(userDetails[0]);
		String lastName = String.valueOf(userDetails[1]);

		assertNotNull(firstName);
		assertNotNull(lastName);

	}

	@Test
	void testUpdateUserEntityEmailVerificationStatus() {
		boolean newEmailVerificationStatus = true;
		userRepository.updateUserEntityVerificationStatus(newEmailVerificationStatus, "abc123");

		UserEntity storedUserDetails = userRepository.findByUserId("abc123");

		boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

		assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
	}

	private void createRecords() {
		// Prepare UserEntity
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("Test");
		userEntity.setLastName("User");
		userEntity.setUserId("abc123");
		userEntity.setEncryptedPassword("456def");
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationStatus(true);

		// Prepare UserAddresses
		AddressEntity addressEntity = new AddressEntity();
		addressEntity.setType("shipping");
		addressEntity.setAddressId("abc1234");
		addressEntity.setCity("Washington DC");
		addressEntity.setCountry("USA");
		addressEntity.setPostalCode("12345");
		addressEntity.setStreetName("1600 Pennsylvania Ave");

		List<AddressEntity> addresses = new ArrayList<>();
		addresses.add(addressEntity);
		userEntity.setAddresses(addresses);

		userRepository.save(userEntity);

		// Prepare UserEntity
		UserEntity userEntity2 = new UserEntity();
		userEntity2.setFirstName("Test");
		userEntity2.setLastName("User");
		userEntity2.setUserId("abc1234");
		userEntity2.setEncryptedPassword("456defg");
		userEntity2.setEmail("test1@test.com");
		userEntity2.setEmailVerificationStatus(true);

		// Prepare UserAddresses
		AddressEntity addressEntity2 = new AddressEntity();
		addressEntity2.setType("shipping");
		addressEntity2.setAddressId("abc12345");
		addressEntity2.setCity("Washington DC");
		addressEntity2.setCountry("USA");
		addressEntity2.setPostalCode("12345");
		addressEntity2.setStreetName("1600 Pennsylvania Ave");

		List<AddressEntity> addresses2 = new ArrayList<>();
		addresses2.add(addressEntity2);
		userEntity2.setAddresses(addresses2);

		userRepository.save(userEntity2);

		recordsCreated = true;
	}
}
