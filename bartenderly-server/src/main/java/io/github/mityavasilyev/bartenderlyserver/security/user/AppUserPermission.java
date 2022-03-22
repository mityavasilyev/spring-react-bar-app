package io.github.mityavasilyev.bartenderlyserver.security.user;

/**
 * Contains specific permissions for certain actions
 */
public enum AppUserPermission {

    COCKTAILS_READ("cocktails:read"),
    COCKTAILS_WRITE("cocktails:write"),

    TAGS_READ("tags:read"),
    TAGS_WRITE("tags:write"),

    PRODUCTS_READ("products:read"),
    PRODUCTS_WRITE("products:write"),

    AUTH_READ("auth:write"),
    AUTH_WRITE("auth:write");

    private final String permission;

    AppUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
