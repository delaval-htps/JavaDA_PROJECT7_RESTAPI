package com.nnk.springboot.security;

/**
 * enum to represents the client provider when OAuth2authentication.
 */
public enum AuthProvider {
    LOCAL("LOCAL"), GITHUB("GITHUB"), TEST("test");

    private final String provider;

    AuthProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return this.provider;
    }
}
