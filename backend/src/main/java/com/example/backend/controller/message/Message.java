package com.example.backend.controller.message;

import java.awt.*;
import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 904580124116256409L;

    private String message;
    private TrayIcon.MessageType typeMessage;

    public Message() {
        super();
    }

    public Message(String message, TrayIcon.MessageType typeMessage) {
        super();
        this.message = message;
        this.typeMessage = typeMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TrayIcon.MessageType getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(TrayIcon.MessageType typeMessage) {
        this.typeMessage = typeMessage;
    }

}
