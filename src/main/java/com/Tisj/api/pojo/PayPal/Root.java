package com.Tisj.api.pojo.PayPal;

import java.util.ArrayList;

public class Root{
    public String scope;
    public String access_token;
    public String token_type;
    public String app_id;
    public int expires_in;
    public ArrayList<String> supported_authn_schemes;
    public String nonce;
    public ClientMetadata client_metadata;
}
