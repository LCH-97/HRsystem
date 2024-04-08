package com.HelloRolha.HR.feature.refreshToken.repo;

import com.HelloRolha.HR.feature.refreshToken.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
}