package com.example.shoppingmall.auth.application.client;

import com.example.shoppingmall.auth.application.dto.OAuthAccessTokenResponse;
import com.example.shoppingmall.auth.application.dto.OAuthMemberInfoResponse;

public interface OAuthClient {

    OAuthAccessTokenResponse getAccessToken(String code);

    OAuthMemberInfoResponse getMemberInfo(String accessToken);
}
