package com.kitadigi.poskita.fragment.additem;
import java.util.List;

public class AddBarangResult {

    private String message;
    private List<String> errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }


}
