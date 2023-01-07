package com.example.intelligentequipmentinspectionsystem;

public class JWTBody {
    private String token_type,exp,iat,jti,user_id,username,is_staff;

    public JWTBody(String token_type, String exp, String iat, String jti, String user_id, String username, String is_staff) {
        this.token_type = token_type;
        this.exp = exp;
        this.iat = iat;
        this.jti = jti;
        this.user_id = user_id;
        this.username = username;
        this.is_staff = is_staff;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getIat() {
        return iat;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIs_staff() {
        return is_staff;
    }

    public void setIs_staff(String is_staff) {
        this.is_staff = is_staff;
    }
}
