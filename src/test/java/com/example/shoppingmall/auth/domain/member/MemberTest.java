package com.example.shoppingmall.auth.domain.member;

import com.example.shoppingmall.auth.domain.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MemberTest {

  @Test
  @DisplayName("멤버 객체를 생성할 수 있다")
  void 멤버_객체_생성() {
    String email = "kang@gmail.com";
    String name = "kang";

    Member member = Member.builder().name(name).build();

    assertThat(member.getName()).isEqualTo(name);
    assertThat(member.getRole()).isEqualTo(Role.USER);
  }

  @Test
  @DisplayName("역할을 지정하지 않으면 기본값은 USER이다")
  void 기본_역할_USER() {
    Member member = Member.builder().name("user").build();

    assertThat(member.getRole()).isEqualTo(Role.USER);
  }

  @Test
  @DisplayName("역할을 null로 설정하면 USER로 설정된다")
  void 역할_null_처리() {
    Member member = Member.builder().name("test").build();

    assertThat(member.getRole()).isEqualTo(Role.USER);
  }

  @Test
  @DisplayName("같은 이메일을 가진 멤버는 동일하다")
  void 이메일_기반_equals() {
    String email = "same@gmail.com";
    Member member1 = Member.builder().name("member1").build();

    Member member2 = Member.builder().name("member2").build();

    assertThat(member1).isEqualTo(member2);
  }

  @Test
  @DisplayName("같은 이메일을 가진 멤버는 같은 hashCode를 가진다")
  void 이메일_기반_hashCode() {
    String email = "test@gmail.com";
    Member member1 = Member.builder().name("member1").build();

    Member member2 = Member.builder().name("member2").build();

    assertThat(member1.hashCode()).isEqualTo(member2.hashCode());
  }

  @Test
  @DisplayName("멤버 객체와 null 비교")
  void null_비교() {
    Member member = Member.builder().name("test").build();

    assertThat(member).isNotEqualTo(null);
  }

  @Test
  @DisplayName("멤버 객체와 다른 클래스 객체 비교")
  void 다른_클래스_비교() {
    Member member = Member.builder().name("test").build();
    String notMember = "not a member";

    assertThat(member).isNotEqualTo(notMember);
  }

  @Test
  @DisplayName("멤버 자기 자신과 비교")
  void 자기_자신_비교() {
    Member member = Member.builder()

        .name("test").build();

    assertThat(member).isEqualTo(member);
  }
}