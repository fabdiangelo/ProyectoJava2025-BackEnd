package com.Tisj.api.Paypal;

public class Request {

    private PaymentToken paymentToken;
    private ShippingAddress shippingAddress;

    public PaymentToken getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(PaymentToken paymentToken) {
        this.paymentToken = paymentToken;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
