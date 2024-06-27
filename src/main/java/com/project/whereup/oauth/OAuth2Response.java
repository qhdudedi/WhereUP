package com.project.whereup.oauth;

public interface OAuth2Response {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
