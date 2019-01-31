package com.mahidanehal21.letstalk3;

public class msg_class {
    private String message;
    private String name;

    public void setFrom(String from) {
        From = from;
    }

    public String getFrom() {

        return From;
    }

    private String From;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public msg_class(String message, String name) {
        this.message = message;
        this.name = name;
    }

    public msg_class() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }
}
