package com.appsdeveloperblog.app.ws.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
	List<UserEntity> findUserbyFirstName(String firstName);

	@Query(value="select * from Users u where u.last_name = :lastName",nativeQuery=true)
	List<UserEntity> findUserbyLastName(@Param("lastName") String lastName);

	@Query(value="select * from Users where first_name LIKE %:keyword% OR last_name LIKE %:keyword%",nativeQuery=true)
	List<UserEntity> findUsersByKeyword(@Param("keyword") String keyword);

	@Query(value="select first_name, last_name from Users where first_name LIKE %:keyword% OR last_name LIKE %:keyword%",nativeQuery=true)
	List<Object[]> findUserFirstNameLastNameByKeyword(@Param("keyword") String keyword);

	@Query("select user from UserEntity user where user.userId =:userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);

	@Query("select user.firstName, user.lastName from UserEntity user where user.userId =:userId")
	List<Object[]> getUserEntityFullNameById(@Param("userId") String userId);

	@Modifying
	@Transactional
	@Query("UPDATE UserEntity u set u.emailVerificationStatus =:emailVerificationStatus where u.userId =:userId")
	void updateUserEntityVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,
			                                @Param("userId") String userId);
}
