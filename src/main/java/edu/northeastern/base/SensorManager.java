package edu.northeastern.base;

import akka.actor.Terminated;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import edu.northeastern.process.sensors.SensorCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jim Z on 11/29/20 15:40
 *
 * Sensor manager will responsible to manager sensors
 * 1. register sensor
 * 2. pass messages to sensor
 * 3. stop sensor
 *
 * TODO: this may cause bottleneck issue, need to consider better approach
 */
public class SensorManager extends AbstractBehavior<SensorManager.SensorManagerCommand> {
    /*
        All messages that Sensor Manager can receive
        SensorManagerCommand        : interface for polymorphism
        Add                         : register a sensor to manager
        Stop                        : stop a sensor when process fulfilled
     */
    interface SensorManagerCommand {}

    public static final class Add implements SensorManagerCommand {
        public final String key;
        public final Behavior<SensorCommand> behavior;
        public Add(String key, Behavior<SensorCommand> behavior) {
            this.key = key;
            this.behavior = behavior;
        }
    }

    public static final class Stop implements SensorManagerCommand {
        public final String key;
        public Stop(String key) {
            this.key = key;
        }
    }

    public static final class Send implements SensorManagerCommand {
        public final String key;
        public final SensorCommand msg;
        public Send(String key, SensorCommand msg) {
            this.key = key;
            this.msg = msg;
        }
    }

    /*
        Define all messages reaction
     */
    @Override
    public Receive<SensorManagerCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Add.class, this::onAdd)
                .onMessage(Stop.class, this::onStop)
                .onMessage(Send.class, this::onSend)
                .build();
    }

    private Behavior<SensorManagerCommand> onAdd(Add cmd) {
        ActorRef<SensorCommand> actorRef = this.getContext().spawn(cmd.behavior, cmd.key);
        this.map.put(cmd.key, actorRef);
        getContext().getLog().info("Add sensor: " + cmd.key);
        return this;
    }

    private Behavior<SensorManagerCommand> onStop(Stop cmd) {
        getContext().stop(map.get(cmd.key));
        map.remove(cmd.key);
        getContext().getLog().info("Stop sensor: " + cmd.key);
        return this;
    }

    private Behavior<SensorManagerCommand> onSend(Send cmd) {
        map.get(cmd.key).tell(cmd.msg);
        return this;
    }

    /*
        Data inside the sensor manager
     */
    private final Map<String, ActorRef<SensorCommand>> map;

    private SensorManager(ActorContext<SensorManagerCommand> context) {
        super(context);
        this.map = new HashMap<>();
    }

    public static Behavior<SensorManagerCommand> create() {
        return Behaviors.setup(SensorManager::new);
    }
}
