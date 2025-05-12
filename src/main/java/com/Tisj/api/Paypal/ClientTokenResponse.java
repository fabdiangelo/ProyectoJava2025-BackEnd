package com.Tisj.api.Paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientTokenResponse {
    @JsonProperty("client_token")
    private String clientToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }
}
