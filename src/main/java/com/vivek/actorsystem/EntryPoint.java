package com.vivek.actorsystem;

import com.vivek.actorsystem.actor.Actor;
import com.vivek.actorsystem.message.Message;

import java.util.List;
import java.util.stream.IntStream;

public class EntryPoint {

    public static void main(String[] args) {
        ActorSystem actorSystem = new ActorSystem();

        List<String> actors =  actorSystem.getActors();

        for (int i = 0; i <2; i++) {
            String actor = actors.get(i);
            IntStream.rangeClosed(1, 5)
                    .forEach(value
                            -> {
                        try {
                            actorSystem.sendMessage(new Message("print", actor, value + ""));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    });
        }

        IntStream.rangeClosed(1, 3)
                .forEach(value
                        -> {
                    try {
                        actorSystem.sendMessage(new Message("print", actors.get(2), value + ""));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });

        actorSystem.shutDownActor(actors.get(2));
        try {
            actorSystem.sendMessage(new Message("print", actors.get(2), 1000 + ""));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        actorSystem.shutDown();

    }

}
