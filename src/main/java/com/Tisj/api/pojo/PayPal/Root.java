
package com.Tisj.api.pojo.PayPal;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Root {
    private String id;
    private String status;
    private String intent;
    private List<PurchaseUnit> purchase_units;
    private List<Link> links;
    private String create_time;
    private String update_time;
    private Payer payer;
    private ApplicationContext application_context;

    @Data
    public static class Payer {
        private String payer_id;
        private String email_address;
        private Name name;
        private Address address;
        private String payer_type;
    }

    @Data
    public static class Name {
        private String given_name;
        private String surname;
    }

    @Data
    public static class ApplicationContext {
        private String brand_name;
        private String locale;
        private String landing_page;
        private String shipping_preference;
        private String user_action;
        private String return_url;
        private String cancel_url;
    }
}

