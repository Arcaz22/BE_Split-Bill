package com.portofolio.splitbill.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portofolio.splitbill.model.Token;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByRefreshTokenId(UUID refreshTokenId);

    boolean existsByTokenId(UUID tokenId);

    boolean existsByRefreshTokenId(UUID refreshTokenId);

    void deleteByTokenId(UUID tokenId);

    void deleteByRefreshTokenId(UUID refreshTokenId);
}
