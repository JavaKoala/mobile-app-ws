package com.appsdeveloperblog.app.ws.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	UserEntity findUserByEmailVerificationToken(String token);

	// Native Query
	@Query(value="select * from Users u where u.EMAIL_VERIFICATION_STATUS = 1",
		    countQuery="select count(*) from Users u where u.EMAIL_VERIFICATION_STATUS = 1",
			nativeQuery = true)

	Page<UserEntity> findAllUsersWithConfirmedEmailAddress( Pageable pageableRequest);

	@Query(value="select * from Users u where u.first_name = ?1",nativeQuery=true)
	List<UserEntity> findUserbyFirstname(String firstname);
}
