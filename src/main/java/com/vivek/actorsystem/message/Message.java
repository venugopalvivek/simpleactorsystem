package com.vivek.actorsystem.message;

public class Message {

    private String tag; //operation
    private String target; //unique actor address
    private String data; //data to be used for operation. Keeping it string for extensibility

    public Message(String tag, String target, String data) {
        this.tag = tag;
        this.target = target;
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public String getTarget() {
        return target;
    }

    public String getData() {
        return data;
    }
}
