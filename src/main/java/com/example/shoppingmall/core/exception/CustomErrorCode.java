package com.example.shoppingmall.core.exception;

import lombok.Getter;

@Getter
public enum CustomErrorCode {

    // ===== 공통 에러 =====
    INTERNAL_SERVER_ERROR(500, "COMMON_001", "내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST(400, "COMMON_002", "잘못된 요청입니다."),
    RESOURCE_NOT_FOUND(404, "COMMON_003", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(405, "COMMON_004", "허용되지 않은 HTTP 메서드입니다."),

    // 회원가입
    USER_ALREADY_EXISTS(409, "USER_001", "이미 존재하는 사용자입니다."),
    EMAIL_ALREADY_EXISTS(409, "USER_002", "이미 사용 중인 이메일입니다."),
    INVALID_EMAIL_FORMAT(400, "USER_003", "올바르지 않은 이메일 형식입니다."),
    PASSWORD_TOO_WEAK(400, "USER_004", "비밀번호가 너무 약합니다."),
    PASSWORD_MISMATCH(400, "USER_005", "비밀번호가 일치하지 않습니다."),
    INVALID_PHONE_FORMAT(400, "USER_006", "올바르지 않은 전화번호 형식입니다."),
    REQUIRED_FIELD_MISSING(400, "USER_007", "필수 입력 항목이 누락되었습니다."),

    // 로그인/인증
    INVALID_CREDENTIALS(401, "AUTH_001", "아이디 또는 비밀번호가 올바르지 않습니다."),
    USER_NOT_FOUND(404, "AUTH_002", "존재하지 않는 사용자입니다."),
    ACCOUNT_LOCKED(423, "AUTH_003", "계정이 잠겨있습니다."),
    ACCOUNT_DISABLED(423, "AUTH_004", "비활성화된 계정입니다."),
    TOO_MANY_LOGIN_ATTEMPTS(429, "AUTH_005", "로그인 시도 횟수를 초과했습니다."),

    // JWT/토큰 관련
    TOKEN_EXPIRED(401, "TOKEN_001", "토큰이 만료되었습니다."),
    INVALID_TOKEN(401, "TOKEN_002", "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(401, "TOKEN_003", "토큰이 존재하지 않습니다."),
    BEARER_NOT_FOUND(401, "TOKEN_004", "Bearer 토큰이 존재하지 않습니다."),

    // 권한 관련
    ACCESS_DENIED(403, "AUTH_006", "접근 권한이 없습니다."),
    INSUFFICIENT_PRIVILEGES(403, "AUTH_007", "권한이 부족합니다."),
    ADMIN_ONLY_ACCESS(403, "AUTH_008", "관리자만 접근 가능합니다."),

    // 게시글 조회
    POST_NOT_FOUND(404, "POST_001", "존재하지 않는 게시글입니다."),
    POST_DELETED(410, "POST_002", "삭제된 게시글입니다."),
    PRIVATE_POST_ACCESS_DENIED(403, "POST_003", "비공개 게시글에 접근할 수 없습니다."),

    // 게시글 생성
    POST_TITLE_REQUIRED(400, "POST_004", "게시글 제목은 필수입니다."),
    POST_CONTENT_REQUIRED(400, "POST_005", "게시글 내용은 필수입니다."),
    POST_TITLE_TOO_LONG(400, "POST_006", "게시글 제목이 너무 깁니다. (최대 100자)"),
    POST_CONTENT_TOO_LONG(400, "POST_007", "게시글 내용이 너무 깁니다. (최대 5000자)"),
    INVALID_CATEGORY(400, "POST_008", "유효하지 않은 카테고리입니다."),

    // 게시글 수정/삭제
    POST_UPDATE_FORBIDDEN(403, "POST_009", "게시글 수정 권한이 없습니다."),
    POST_DELETE_FORBIDDEN(403, "POST_010", "게시글 삭제 권한이 없습니다."),
    POST_ALREADY_DELETED(409, "POST_011", "이미 삭제된 게시글입니다."),

    // 게시판 관련 기타
    BOARD_NOT_FOUND(404, "BOARD_001", "존재하지 않는 게시판입니다."),
    BOARD_ACCESS_DENIED(403, "BOARD_002", "게시판 접근 권한이 없습니다."),
    POST_LIMIT_EXCEEDED(429, "BOARD_003", "게시글 작성 제한을 초과했습니다.");

    private final int status;
    private final String code;
    private final String message;

    CustomErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}