package com.resumeiq.backend.constant;

public final class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String AUTH_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String JWT_ISSUER = "ResumeIQ";

    public static final long JWT_EXPIRATION =
            1000L * 60 * 60 * 24; // 24 Hours
}