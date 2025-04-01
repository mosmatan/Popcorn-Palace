package com.att.tdp.popcorn_palace.Models;

public class ErrorResponseObject {
    private String error;

    public ErrorResponseObject(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
