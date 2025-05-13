package com.Tisj.api.pojo.PayPal;

import lombok.Data;
import java.util.List;

@Data
public class WebhookPayload {
    private String id;
    private String event_type;
    private String create_time;
    private Resource resource;
    private List<Link> links;

    @Data
    public static class Resource {
        private String id;
        private String status;
        private Amount amount;
        private String create_time;
        private String update_time;
        private Payer payer;
    }

    @Data
    public static class Payer {
        private String email_address;
        private String payer_id;
        private Name name;
    }
} 