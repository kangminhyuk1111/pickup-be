package com.example.shoppingmall.auth.domain.member;

import com.example.shoppingmall.auth.domain.type.ProviderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("멤버 저장에 성공한다.")
  void 멤버_저장_테스트() {
    // given
    Member member = Member.builder()
        .oauthId("oauth-123")
        .profileUrl("https://example.com/profile.jpg")
        .name("강민혁")
        .providerType(ProviderType.GITHUB)
        .build();

    // when
    Member savedMember = memberRepository.save(member);

    // then
    assertThat(savedMember).isNotNull();
    assertThat(savedMember.getId()).isNotNull();
    assertThat(savedMember.getOauthId()).isEqualTo("oauth-123");
    assertThat(savedMember.getName()).isEqualTo("강민혁");
    assertThat(savedMember.getProviderType()).isEqualTo(ProviderType.GITHUB);
    assertThat(savedMember.getProfileUrl()).isEqualTo("https://example.com/profile.jpg");
  }

  @Test
  @DisplayName("멤버 저장시 OAuth ID 중복으로 실패")
  void 멤버_저장시_OAuth_ID_중복으로_실패() {
    // given
    String duplicateOauthId = "duplicate-oauth-id";

    Member existingMember = Member.builder()
        .oauthId(duplicateOauthId)
        .name("기존멤버")
        .profileUrl("https://example.com/profile1.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    memberRepository.save(existingMember);

    Member duplicateMember = Member.builder()
        .oauthId(duplicateOauthId)  // 동일한 OAuth ID 사용
        .name("중복멤버")
        .profileUrl("https://example.com/profile2.jpg")
        .providerType(ProviderType.GOOGLE)
        .build();

    // when & then
    assertThatThrownBy(() -> memberRepository.save(duplicateMember))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  @Test
  @DisplayName("OAuth ID로 멤버 조회에 성공한다.")
  void OAuth_ID로_멤버_조회_성공() {
    // given
    Member savedMember = Member.builder()
        .oauthId("find-oauth-123")
        .name("찾을멤버")
        .profileUrl("https://example.com/find.jpg")
        .providerType(ProviderType.GOOGLE)
        .build();

    memberRepository.save(savedMember);

    // when
    Optional<Member> foundMember = memberRepository.findByOauthId("find-oauth-123");

    // then
    assertThat(foundMember).isPresent();
    assertThat(foundMember.get().getName()).isEqualTo("찾을멤버");
    assertThat(foundMember.get().getProviderType()).isEqualTo(ProviderType.GOOGLE);
  }

  @Test
  @DisplayName("존재하지 않는 OAuth ID로 조회시 빈 Optional 반환")
  void 존재하지_않는_OAuth_ID로_조회시_빈_Optional_반환() {
    // when
    Optional<Member> foundMember = memberRepository.findByOauthId("non-existent-oauth-id");

    // then
    assertThat(foundMember).isEmpty();
  }

  @Test
  @DisplayName("Provider Type으로 멤버 목록 조회")
  void Provider_Type으로_멤버_목록_조회() {
    // given
    Member githubMember1 = Member.builder()
        .oauthId("github-1")
        .name("깃허브유저1")
        .profileUrl("https://github.com/user1.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    Member githubMember2 = Member.builder()
        .oauthId("github-2")
        .name("깃허브유저2")
        .profileUrl("https://github.com/user2.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    Member googleMember = Member.builder()
        .oauthId("google-1")
        .name("구글유저1")
        .profileUrl("https://google.com/user1.jpg")
        .providerType(ProviderType.GOOGLE)
        .build();

    memberRepository.save(githubMember1);
    memberRepository.save(githubMember2);
    memberRepository.save(googleMember);

    // when
    List<Member> githubMembers = memberRepository.findByProviderType(ProviderType.GITHUB);

    // then
    assertThat(githubMembers).hasSize(2);
    assertThat(githubMembers)
        .extracting(Member::getProviderType)
        .containsOnly(ProviderType.GITHUB);
  }

  @Test
  @DisplayName("멤버 정보 업데이트")
  void 멤버_정보_업데이트() {
    // given
    Member member = Member.builder()
        .oauthId("update-test")
        .name("원래이름")
        .profileUrl("https://example.com/old.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    Member savedMember = memberRepository.save(member);

    // when
    savedMember.updateProfileUrl("새로운이름", "https://example.com/new.jpg");
    Member updatedMember = memberRepository.save(savedMember);

    // then
    assertThat(updatedMember.getName()).isEqualTo("새로운이름");
    assertThat(updatedMember.getProfileUrl()).isEqualTo("https://example.com/new.jpg");
    assertThat(updatedMember.getOauthId()).isEqualTo("update-test"); // 변경되지 않음
    assertThat(updatedMember.getProviderType()).isEqualTo(ProviderType.GITHUB); // 변경되지 않음
  }

  @Test
  @DisplayName("멤버 삭제")
  void 멤버_삭제() {
    // given
    Member member = Member.builder()
        .oauthId("delete-test")
        .name("삭제될멤버")
        .profileUrl("https://example.com/delete.jpg")
        .providerType(ProviderType.GOOGLE)
        .build();

    Member savedMember = memberRepository.save(member);
    Long memberId = savedMember.getId();

    // when
    memberRepository.delete(savedMember);

    // then
    Optional<Member> deletedMember = memberRepository.findById(memberId);
    assertThat(deletedMember).isEmpty();
  }

  @Test
  @DisplayName("전체 멤버 수 조회")
  void 전체_멤버_수_조회() {
    // given
    Member member1 = Member.builder()
        .oauthId("count-1")
        .name("멤버1")
        .profileUrl("https://example.com/1.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    Member member2 = Member.builder()
        .oauthId("count-2")
        .name("멤버2")
        .profileUrl("https://example.com/2.jpg")
        .providerType(ProviderType.GOOGLE)
        .build();

    memberRepository.save(member1);
    memberRepository.save(member2);

    // when
    long count = memberRepository.count();

    // then
    assertThat(count).isGreaterThanOrEqualTo(2);
  }

  @Test
  @DisplayName("OAuth ID와 Provider Type으로 멤버 조회")
  void OAuth_ID와_Provider_Type으로_멤버_조회() {
    // given
    Member member = Member.builder()
        .oauthId("multi-find-test")
        .name("복합조회테스트")
        .profileUrl("https://example.com/multi.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    memberRepository.save(member);

    // when
    Optional<Member> foundMember = memberRepository
        .findByOauthIdAndProviderType("multi-find-test", ProviderType.GITHUB);

    // then
    assertThat(foundMember).isPresent();
    assertThat(foundMember.get().getName()).isEqualTo("복합조회테스트");
  }

  @Test
  @DisplayName("잘못된 Provider Type으로 조회시 빈 결과")
  void 잘못된_Provider_Type으로_조회시_빈_결과() {
    // given
    Member member = Member.builder()
        .oauthId("wrong-provider-test")
        .name("잘못된프로바이더테스트")
        .profileUrl("https://example.com/wrong.jpg")
        .providerType(ProviderType.GITHUB)
        .build();

    memberRepository.save(member);

    // when
    Optional<Member> foundMember = memberRepository
        .findByOauthIdAndProviderType("wrong-provider-test", ProviderType.GOOGLE);

    // then
    assertThat(foundMember).isEmpty();
  }
}