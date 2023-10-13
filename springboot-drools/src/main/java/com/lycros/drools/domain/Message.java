package com.lycros.drools.domain;

import lombok.Data;

@Data
public  class Message {
    public static final int HELLO   = 0;
    public static final int GOODBYE = 1;

    private String          message;
    private int             status;
}