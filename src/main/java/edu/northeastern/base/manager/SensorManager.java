package edu.northeastern.base.manager;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import edu.northeastern.base.sensor.SensorCommand;
import scala.Option;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
public final class SensorManager extends AbstractBehavior<SensorManager.SensorManagerCommand> {
    /*
        All messages that Sensor Manager can receive
        SensorManagerCommand        : interface for polymorphism
        Add                         : register a sensor to manager
        Stop                        : stop a sensor when process fulfilled
     */
    public interface SensorManagerCommand { }

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
    public static final class Schedule implements SensorManagerCommand {
        public String name;
        public String key;
        public SensorCommand msg;
        public String description;
        public String cronExpression;
        public String calendar;
        public TimeZone timezone;

        public Schedule(String name, String key, SensorCommand msg, String description, String cronExpression, String calendar, TimeZone timezone) {
            this.name = name;
            this.key = key;
            this.msg = msg;
            this.description = description;
            this.cronExpression = cronExpression;
            this.calendar = calendar;
            this.timezone = timezone;
        }
    }

    public static final class CancelSchedule implements SensorManagerCommand {
        public String key;

        public CancelSchedule(String key) {
            this.key = key;
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
                .onMessage(Schedule.class, this::onSchedule)
                .onMessage(CancelSchedule.class, this::onCancelSchedule)
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

    private Behavior<SensorManagerCommand> onSchedule(Schedule schedule) {
        createScheduledTask(
                schedule.name,
                schedule.key,
                schedule.msg,
                schedule.description,
                schedule.cronExpression,
                schedule.calendar,
                schedule.timezone
        );
        return this;
    }

    private Behavior<SensorManagerCommand> onCancelSchedule(CancelSchedule cancelSchedule) {
        cancelScheduledTask(cancelSchedule.key);
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

    private void createScheduledTask(String name, String key, SensorCommand msg, String description, String cronExpression, String calendar, TimeZone timezone) {
        ActorManager.getScheduler().createJobSchedule(
                name,
                Adapter.toClassic(map.get(key)),
                msg,
                Option.apply(description),
                cronExpression,
                Option.apply(calendar),
                timezone
        );
    }

    private void cancelScheduledTask(String key) {
        ActorManager.getScheduler().cancelJob(key);
    }
}
