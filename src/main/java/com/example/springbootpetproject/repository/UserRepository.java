package com.example.springbootpetproject.repository;

import com.example.springbootpetproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User getUserById(Long id);
    boolean existsUserById(Long id);
    User findByUsername(String username);
    boolean existsUserByUserEmail(String email);
    boolean existsUserByUsername(String username);

    Page<User> findAll(Pageable pageable);

    @Modifying
    @Query(value = "UPDATE user_info SET user_count_of_money = user_count_of_money + :money WHERE username = :userName",nativeQuery = true)
    void topUpAccount(@Param("money")BigDecimal money, @Param("userName") String userName);

    @Modifying
    @Query(value = "UPDATE user_info SET user_count_of_money = user_count_of_money - :money WHERE username = :userName",nativeQuery = true)
    void spendMoney(@Param("money")BigDecimal money, @Param("userName") String userName);

    @Modifying
    @Query(value = "UPDATE user_info SET acount_verified = :newVerificationStatus WHERE username = :userName", nativeQuery = true)
    void setUserVerification(@Param("newVerificationStatus") boolean newVerificationStatus, @Param("userName") String userName);

    @Modifying
    @Query(value = "UPDATE user_info SET user_password =:newUserPassword WHERE username = :userName", nativeQuery = true)
    void changeUserPassword(@Param("newUserPassword") String newUserPassword, @Param("userName") String userName);


}
