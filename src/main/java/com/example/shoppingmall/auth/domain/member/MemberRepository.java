package com.example.shoppingmall.auth.domain.member;

import com.example.shoppingmall.auth.domain.type.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.oauthId = :oauthId")
    Optional<Member> findByOauthId(@Param("oauthId") String oauthId);

    List<Member> findByProviderType(ProviderType providerType);

    boolean existsByOauthId(String oauthId);

    @Query("SELECT m FROM Member m WHERE m.oauthId = :oauthId AND m.providerType = :providerType")
    Optional<Member> findByOauthIdAndProviderType(@Param("oauthId") String oauthId,
        @Param("providerType") ProviderType providerType);
}