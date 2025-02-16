package com.repinsky.copywise.constants;

public enum Constant {
    ROLES("roles"),
    BEARER("Bearer");

    private final String value;

    Constant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
