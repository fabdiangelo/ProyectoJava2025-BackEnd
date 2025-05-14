
package com.Tisj.api.pojo.PayPal;

import lombok.Data;

@Data
public class Amount {
    private String currency_code;
    private String value;
    private Breakdown breakdown;

    @Data
    public static class Breakdown {
        private ItemTotal item_total;
        private Shipping shipping;
        private Handling handling;
        private Tax tax;
        private Insurance insurance;
        private ShippingDiscount shipping_discount;
        private Discount discount;
    }

    @Data
    public static class ItemTotal {
        private String currency_code;
        private String value;
    }

    @Data
    public static class Shipping {
        private String currency_code;
        private String value;
    }

    @Data
    public static class Handling {
        private String currency_code;
        private String value;
    }

    @Data
    public static class Tax {
        private String currency_code;
        private String value;
    }

    @Data
    public static class Insurance {
        private String currency_code;
        private String value;
    }

    @Data
    public static class ShippingDiscount {
        private String currency_code;
        private String value;
    }

    @Data
    public static class Discount {
        private String currency_code;
        private String value;
    }
}

