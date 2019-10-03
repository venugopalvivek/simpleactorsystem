package com.vivek.actorsystem.actor.impl;

import com.vivek.actorsystem.actor.BaseActor;
import com.vivek.actorsystem.message.Message;

public class LogActor extends BaseActor {

    public LogActor(String id, int size) {
        super(id, size);
    }

    @Override
    protected void process(Message message) {
        String logMsg = String.format("Logging message - tag:%s, target:%s, data:%s",
                message.getTag(), message.getTarget(), message.getData());
        System.out.println(logMsg);
    }
}
