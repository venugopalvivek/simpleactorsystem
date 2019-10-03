package com.vivek.actorsystem.exceptions;

import com.vivek.actorsystem.actor.ActorState;

public class ActorCannotAcceptMessages extends Exception {

    public static enum ErrorCode {
        INITIALIZING(1, "Actor initializing"),
        TEARING_DOWN(2, "Actor shutting down");

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private ErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    private int code;

    public ActorCannotAcceptMessages(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }

}
