
package com.Tisj.api.pojo.PayPal;

import lombok.Data;

@Data
public class Address {
    private String address_line_1;
    private String address_line_2;
    private String admin_area_2;
    private String admin_area_1;
    private String postal_code;
    private String country_code;
}
