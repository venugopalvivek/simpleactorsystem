package com.vivek.actorsystem.actor;

import com.vivek.actorsystem.exceptions.ActorCannotAcceptMessages;
import com.vivek.actorsystem.exceptions.MailboxFullException;
import com.vivek.actorsystem.message.Message;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class BaseActor implements Actor {

    private Queue<Message> mailbox;
    private String actorId;
    private ActorState state;

    public BaseActor(String actorId, int size) {
        this.actorId = actorId;
        this.mailbox = new LinkedBlockingQueue<>(size); // creating a bounded queue here
    }

    protected void setState(ActorState state) {
        this.state = state;
    }

    @Override
    public String getId() {
        return actorId;
    }

    @Override
    public void sendMessage(Message message) throws MailboxFullException, ActorCannotAcceptMessages {
        if (state != ActorState.ACTIVE) throwException();
        boolean addedSuccessfully = this.mailbox.offer(message);
        if (!addedSuccessfully) throw new MailboxFullException();
    }

    private void throwException() throws ActorCannotAcceptMessages {
        if (state == ActorState.TEARING_DOWN)
            throw new ActorCannotAcceptMessages(ActorCannotAcceptMessages.ErrorCode.TEARING_DOWN);
        if (state == ActorState.INITIALIZING)
            throw new ActorCannotAcceptMessages(ActorCannotAcceptMessages.ErrorCode.INITIALIZING);
    }

    @Override
    public boolean hasMoreMessages() {
        return !mailbox.isEmpty();
    }

    @Override
    public void run() {
        if (mailbox.isEmpty()) return;
        Message message = mailbox.peek(); //Not removing the message from the queue
        process(message);
        //if no exception thrown
        mailbox.remove(message); //
    }

    protected abstract void process(Message message);

    @Override
    public void shutdown() {
        this.state = ActorState.TEARING_DOWN;
    }

    @Override
    public ActorState getState() {
        return this.state;
    }
}
