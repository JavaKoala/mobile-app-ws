package com.appsdeveloperblog.app.ws.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.PasswordResetTokenRepository;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.EmailService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptpasswordEncoder;

	@Mock
	EmailService emailService;

	@Mock
	PasswordResetTokenRepository passwordResetTokenRepository;

	String userId = "abracadabra1234";
	String encryptedPassword = "4321abcd";
	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userEntity = new UserEntity();
		userEntity.setId(1);
		userEntity.setFirstName("Test");
		userEntity.setLastName("User");
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(encryptedPassword);
		userEntity.setEmail("test@test.com");
		userEntity.setEmailVerificationToken("abracadabra1234");
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	final void testGetUser() {

		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		UserDto userDto = userService.getUser("test@test.com");

		assertNotNull(userDto);
		assertEquals("Test", userDto.getFirstName());
	}

	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		
		assertThrows(UsernameNotFoundException.class, 
				()->{
					userService.getUser("test@test.com");
				}
				);
	}

	@Test
	final void testCreateUser_CreateUserServiceException() {
		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Test");
		userDto.setLastName("User");
		userDto.setPassword("password");
		userDto.setEmail("test@test.com");
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		assertThrows(UserServiceException.class,
				() -> {
					userService.createUser(userDto);
				}
		);
	}

	@Test
	final void testCreateUser() {

		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("abracadabra1234");
		when(utils.generateUserId(anyInt())).thenReturn(userId);
		when(bCryptpasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.doNothing().when(emailService).verifyEmail(any(UserDto.class));

		UserDto userDto = new UserDto();
		userDto.setAddresses(getAddressesDto());
		userDto.setFirstName("Test");
		userDto.setLastName("User");
		userDto.setPassword("password");
		userDto.setEmail("test@test.com");

		UserDto storedUserDetails = userService.createUser(userDto);
		assertNotNull(storedUserDetails);
		assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
		assertNotNull(storedUserDetails.getUserId());
		assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
		verify(utils, times(2)).generateAddressId(30);
		verify(bCryptpasswordEncoder, times(1)).encode("password");
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}

	private List<AddressDTO> getAddressesDto() {
		AddressDTO addressDto = new AddressDTO();
        addressDto.setType("shipping");
        addressDto.setCity("Washington DC");
        addressDto.setCountry("USA");
        addressDto.setPostalCode("15044");
        addressDto.setStreetName("1600 Pennsylvania Ave.");

		AddressDTO billingAddressDto = new AddressDTO();
		billingAddressDto.setType("shipping");
		billingAddressDto.setCity("Washington DC");
		billingAddressDto.setCountry("USA");
		billingAddressDto.setPostalCode("15044");
		billingAddressDto.setStreetName("1600 Pennsylvania Ave.");

        ArrayList<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDto);
        addresses.add(billingAddressDto);

        return addresses;
	}

	private List<AddressEntity> getAddressesEntity(){
		List<AddressDTO> addresses = getAddressesDto();

		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();

		return new ModelMapper().map(addresses, listType);
	}
}
