package com.example.shoppingmall.auth.config;

import com.example.shoppingmall.auth.application.token.FakeJwtTokenProvider;
import com.example.shoppingmall.auth.application.token.JwtTokenProvider;
import com.example.shoppingmall.auth.domain.member.MemberRepository;
import com.example.shoppingmall.board.domain.match.MatchRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class
})
public class TestAuthConfig {

    @Bean
    @Primary
    public MemberRepository memberRepository() {
        return Mockito.mock(MemberRepository.class);
    }

    @Bean
    @Primary
    public MatchRepository matchRepository() {
        return Mockito.mock(MatchRepository.class);
    }

    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        return new FakeJwtTokenProvider();
    }
}