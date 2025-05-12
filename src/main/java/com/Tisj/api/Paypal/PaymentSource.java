package com.Tisj.api.Paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentSource {
    @JsonProperty("card")
    private Card card;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public static class Card {
        @JsonProperty("number")
        private String number;

        @JsonProperty("expiry")
        private String expiry;

        @JsonProperty("security_code")
        private String securityCode;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getSecurityCode() {
            return securityCode;
        }

        public void setSecurityCode(String securityCode) {
            this.securityCode = securityCode;
        }
    }
} 