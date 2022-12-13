package com.nnk.springboot.security;

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
