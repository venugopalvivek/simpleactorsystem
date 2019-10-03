package com.vivek.actorsystem;

import com.vivek.actorsystem.actor.Actor;
import com.vivek.actorsystem.actor.ActorState;
import com.vivek.actorsystem.actor.impl.LogActor;
import com.vivek.actorsystem.message.Message;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ActorSystem {

    private List<Actor> actors;
    private ExecutorService executorService;
    private boolean shutdown;

    public ActorSystem() {
        actors = new LinkedList<>();
        executorService = Executors.newFixedThreadPool(1);
        initializeActors();
        run();
    }

    private void initializeActors() {
        actors.add(new LogActor(getUniqueId(LogActor.class), 10));
        actors.add(new LogActor(getUniqueId(LogActor.class), 3));
        actors.add(new LogActor(getUniqueId(LogActor.class), 5));
    }

    public List<String> getActors() {
        return actors
                .stream()
                .map(actor -> {return actor.getId();})
                .collect(Collectors.toList());
    }

    public void shutDownActor(String actorId) {
        actors.stream().filter(actor -> {return actor.getId().equals(actorId);}).findFirst().get().shutdown();
    }

    public void shutDown() {
        for (Actor actor : actors) {
            actor.shutdown();
        }
    }

    public void sendMessage(Message message) throws Exception {
        for (Actor actor : actors) {
            if (actor.getId().equals(message.getTarget())) {
                actor.sendMessage(message);
                return;
            }
        }
        throw new IllegalArgumentException("No actor found for the message");
    }

    public void run() {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (!actors.isEmpty()) {
                    List<Future> results = new LinkedList<>();
                    Iterator<Actor> actorIterator = actors.iterator();
                    while (actorIterator.hasNext()) {
                        Actor actor = actorIterator.next();
                        if (actor.getState() == ActorState.TEARING_DOWN) {
                            if (!actor.hasMoreMessages()) {
                                actorIterator.remove();
                                continue;
                            }
                        }
                        results.add(executorService.submit(actor));
                    }

                    for (Future result : results) {
                        try {
                            result.get();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

                executorService.shutdownNow();
            }
        };

        t.start();
    }

    private String getUniqueId(Class<? extends Actor> actor) {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return actor.getSimpleName() + new String(array);
    }
}
