package com.vivek.actorsystem.actor;

import com.vivek.actorsystem.exceptions.ActorCannotAcceptMessages;
import com.vivek.actorsystem.exceptions.MailboxFullException;
import com.vivek.actorsystem.message.Message;

public interface Actor extends Runnable {

    String getId();
    ActorState getState();
    void shutdown();

    boolean hasMoreMessages();
    void sendMessage(Message message) throws MailboxFullException, ActorCannotAcceptMessages;

}
