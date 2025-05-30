package com.example.shoppingmall.auth.application.dto;

import com.example.shoppingmall.auth.domain.type.ProviderType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GoogleMemberInfoResponseTest {

  @Test
  void 구글_정보를_DB에_저장할_OAuthMemberInfoResponse로_변경() {
    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse(
        "123456789", "testuser", "https://lh3.googleusercontent.com/test");

    final OAuthMemberInfoResponse oAuthMemberInfoResponse = info.toOAuthMemberInfoResponse();

    assertThat(oAuthMemberInfoResponse.getOauthId()).isEqualTo(info.getId());
    assertThat(oAuthMemberInfoResponse.getName()).isEqualTo(info.getName());
    assertThat(oAuthMemberInfoResponse.getProfileUrl()).isEqualTo(info.getPicture());
    assertThat(oAuthMemberInfoResponse.getProviderType()).isEqualTo(ProviderType.GOOGLE);
  }

  @Test
  void id가_null일_때_예외_발생() {
    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse(
        null, "testuser", "https://picture.url");

    assertThatThrownBy(info::toOAuthMemberInfoResponse)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("id 는 null이 될 수 없습니다.");
  }

  @Test
  void name이_null일_때_예외_발생() {
    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse(
        "123", null, "https://picture.url");

    assertThatThrownBy(info::toOAuthMemberInfoResponse)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("name 은 null이 될 수 없습니다.");
  }

  @Test
  void picture가_null일_때_예외_발생() {
    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse(
        "123", "testuser", null);

    assertThatThrownBy(info::toOAuthMemberInfoResponse)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("picture 은 null이 될 수 없습니다.");
  }

  @Test
  void 모든_필드가_null일_때_id_검증이_먼저_실행됨() {
    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse(
        null, null, null);

    assertThatThrownBy(info::toOAuthMemberInfoResponse)
        .isInstanceOf(NullPointerException.class)
        .hasMessage("id 는 null이 될 수 없습니다.");
  }

  @Test
  void 유효한_모든_값으로_변환_성공() {
    final String id = "987654321";
    final String name = "Google User";
    final String picture = "https://lh3.googleusercontent.com/a/example";

    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse(id, name, picture);
    final OAuthMemberInfoResponse result = info.toOAuthMemberInfoResponse();

    assertThat(result.getOauthId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getProfileUrl()).isEqualTo(picture);
    assertThat(result.getProviderType()).isEqualTo(ProviderType.GOOGLE);
  }

  @Test
  void 빈_문자열_값으로_변환_성공() {
    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse("", "", "");
    final OAuthMemberInfoResponse result = info.toOAuthMemberInfoResponse();

    assertThat(result.getOauthId()).isEmpty();
    assertThat(result.getName()).isEmpty();
    assertThat(result.getProfileUrl()).isEmpty();
    assertThat(result.getProviderType()).isEqualTo(ProviderType.GOOGLE);
  }

  @Test
  void 한글_이름_포함_값으로_변환_성공() {
    final String id = "google-korean-123";
    final String name = "구글 사용자";
    final String picture = "https://lh3.googleusercontent.com/korean-user";

    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse(id, name, picture);
    final OAuthMemberInfoResponse result = info.toOAuthMemberInfoResponse();

    assertThat(result.getOauthId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getProfileUrl()).isEqualTo(picture);
    assertThat(result.getProviderType()).isEqualTo(ProviderType.GOOGLE);
  }

  @Test
  void 긴_Google_ID와_URL로_변환_성공() {
    final String longId = "1234567890123456789012345678901234567890";
    final String name = "Very Long Google Username That Exceeds Normal Length";
    final String longPictureUrl = "https://lh3.googleusercontent.com/a/ACg8ocK" +
        "VeryLongGoogleImageIdThatExceedsNormalLength123456789";

    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse(longId, name,
        longPictureUrl);
    final OAuthMemberInfoResponse result = info.toOAuthMemberInfoResponse();

    assertThat(result.getOauthId()).isEqualTo(longId);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getProfileUrl()).isEqualTo(longPictureUrl);
    assertThat(result.getProviderType()).isEqualTo(ProviderType.GOOGLE);
  }

  @Test
  void 특수문자_포함_값으로_변환_성공() {
    final String id = "google.user@example.com";
    final String name = "Test User (Google)";
    final String picture = "https://example.com/picture?size=96&crop=circle";

    final GoogleMemberInfoResponse info = createGoogleMemberInfoResponse(id, name, picture);
    final OAuthMemberInfoResponse result = info.toOAuthMemberInfoResponse();

    assertThat(result.getOauthId()).isEqualTo(id);
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getProfileUrl()).isEqualTo(picture);
    assertThat(result.getProviderType()).isEqualTo(ProviderType.GOOGLE);
  }

  @Test
  void validateFields_메서드_정상_동작_확인() {
    final GoogleMemberInfoResponse validInfo = createGoogleMemberInfoResponse(
        "123", "user", "url");

    // 예외가 발생하지 않음을 확인
    assertThatCode(validInfo::toOAuthMemberInfoResponse)
        .doesNotThrowAnyException();
  }

  private GoogleMemberInfoResponse createGoogleMemberInfoResponse(String id, String name,
      String picture) {
    try {
      final GoogleMemberInfoResponse response = new GoogleMemberInfoResponse();

      java.lang.reflect.Field idField = GoogleMemberInfoResponse.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(response, id);

      java.lang.reflect.Field nameField = GoogleMemberInfoResponse.class.getDeclaredField("name");
      nameField.setAccessible(true);
      nameField.set(response, name);

      java.lang.reflect.Field pictureField = GoogleMemberInfoResponse.class.getDeclaredField(
          "picture");
      pictureField.setAccessible(true);
      pictureField.set(response, picture);

      return response;
    } catch (Exception e) {
      throw new RuntimeException("테스트 객체 생성 실패", e);
    }
  }
}