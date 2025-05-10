package com.Tisj.api.pojo.PayPal;

import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class PurchaseUnit {
    private String reference_id;
    @JsonProperty("amount")
    private Amount amount;
    private Payee payee;
    private String soft_descriptor;
    @JsonProperty("items")
    private List<Item> items;
    private Shipping shipping;
    private PaymentInstruction payment_instruction;
    @JsonProperty("description")
    private String description;

    @Data
    public static class Item {
        private String name;
        private String description;
        private String sku;
        private String category;
        private String quantity;
        private Amount unit_amount;
    }

    @Data
    public static class Shipping {
        private Address address;
        private String method;
    }

    @Data
    public static class PaymentInstruction {
        private String platform_fees;
        private String disbursement_mode;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

