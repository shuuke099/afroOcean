package com.tinka.auth.repository;

import com.tinka.auth.entity.User;
import com.tinka.auth.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Basic lookups
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);

    // Token lookups
    Optional<User> findByEmailVerificationToken(String token);
    Optional<User> findByResetToken(String token);

    // ✅ Add custom query to get users by their address
    @Query("SELECT a.user FROM Address a WHERE a.country = :country AND a.city = :city")
    List<User> findUsersByAddressCountryAndCity(@Param("country") String country, @Param("city") String city);

    // Filter by role or status
    List<User> findAllByRole(Role role);
    List<User> findAllByActiveTrue();
    List<User> findAllByBlockedFalse();

    // Search by name
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstNameKeyword, String lastNameKeyword);

    // Search seller-specific
    List<User> findAllByRoleAndSellerStatus(Role role, String sellerStatus);
    Optional<User> findByIdAndActiveTrue(Long id);

    // Multi-Tenant support (if needed)
    List<User> findAllByTenantId(String tenantId);
}
