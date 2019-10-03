package com.vivek.actorsystem.exceptions;

public class MailboxFullException extends Exception {

    public MailboxFullException() {
        super("Mailbox is full");
    }
}
