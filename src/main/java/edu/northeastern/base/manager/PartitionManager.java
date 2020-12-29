package edu.northeastern.base.manager;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Jim Z on 12/26/20 19:16
 */

public class PartitionManager extends AbstractBehavior<SensorManager.SensorManagerCommand> {

    private Map<String, ActorRef<SensorManager.SensorManagerCommand>> dic;

    private static PartitionManager instance;
    private PartitionManager(ActorContext<SensorManager.SensorManagerCommand> context) {
        super(context);
        this.dic = new Hashtable<>();
    }

    public static PartitionManager getInstance() {
        return instance;
    }

    public ActorRef<SensorManager.SensorManagerCommand> getPartition(String name) {
        return this.dic.get(name);
    }

    public void registerNewPartition(ActorRef<SensorManager.SensorManagerCommand> ref, String name) {
        this.dic.put(name, ref);
    }

    public ActorRef<SensorManager.SensorManagerCommand> removePartition(String name) {
        return this.dic.remove(name);
    }

    public static final class Spawn implements SensorManager.SensorManagerCommand {
        private String name;
        private Behavior<SensorManager.SensorManagerCommand> behavior;
        public Spawn(String name, Behavior<SensorManager.SensorManagerCommand> behavior) {
            this.behavior = behavior;
            this.name = name;
        }
    }

    public static final class Cancel implements SensorManager.SensorManagerCommand {
        private String name;
        public Cancel(String name) {
            this.name = name;
        }
    }

    @Override
    public Receive<SensorManager.SensorManagerCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Spawn.class, this::onSpawn)
                .onMessage(Cancel.class, this::onCancel)
                .build();
    }

    private Behavior<SensorManager.SensorManagerCommand> onSpawn(Spawn spawn) {
        ActorRef<SensorManager.SensorManagerCommand> res = getContext().spawn(spawn.behavior, spawn.name);
        registerNewPartition(res, spawn.name);
        return this;
    }

    private Behavior<SensorManager.SensorManagerCommand> onCancel(Cancel cancel) {
        getContext().stop(this.dic.get(cancel.name));
        removePartition(cancel.name);
        return this;
    }

    public static Behavior<SensorManager.SensorManagerCommand> create() {
        return Behaviors.setup(param -> {
            instance = new PartitionManager(param);
            return instance;
        });
    }
}
