package com.kitadigi.poskita.activities.login;

public class LoginResult {


    String auth_token_mobile;
    Data_User data_user;
    String message;

    public String getAuth_token_mobile() {
        return auth_token_mobile;
    }

    public void setAuth_token_mobile(String auth_token_mobile) {
        this.auth_token_mobile = auth_token_mobile;
    }

    public Data_User getData_user() {
        return data_user;
    }

    public void setData_user(Data_User data_user) {
        this.data_user = data_user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
