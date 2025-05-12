
package com.Tisj.api.pojo.PayPal;

import lombok.Data;

@Data
public class Payee {
    private String email_address;
    private String merchant_id;
    private String display_data;
    private String payee_display_metadata;
}

