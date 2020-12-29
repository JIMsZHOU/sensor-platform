package edu.northeastern.base.manager;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Adapter;
import akka.actor.typed.javadsl.Behaviors;
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension;
import edu.northeastern.base.sensor.HealthChecker;
import edu.northeastern.base.sensor.SensorCommand;
import scala.Option;

import java.util.TimeZone;

/**
 * Created by Jim Z on 11/29/20 18:31
 *
 * Actor Manager will store useful actor references.
 * Current include below:
 * 1. typed ActorSystem
 * 2. Akka Quartz Scheduler
 * 3. Sensor Manager Actor
 * 4. Alert Actor
 */
public final class ActorManager {

    public final static String userPartitionName = "user-partition";
    public final static String eventPartitionName = "event-partition";

    private static ActorRef<SensorManager.SensorManagerCommand> PARTITIONMANAGER;
    private static ActorSystem SYSTEM;
    private static QuartzSchedulerExtension SCHEDULER;
    private static ActorRef<SensorManager.SensorManagerCommand> SENSORMANAGER;
    private static ActorRef<AlertSensor.Alert> ALERTSENSOR;
    private static ActorRef<SensorCommand> HEALTHCHECKER;
    private static boolean isReady;


    public static ActorSystem getSystem() {
        return SYSTEM;
    }
    public static QuartzSchedulerExtension getScheduler() {
        return SCHEDULER;
    }
    public static ActorRef<SensorManager.SensorManagerCommand> getSensorManager() {
        return SENSORMANAGER;
    }
    public static ActorRef<SensorManager.SensorManagerCommand> getPARTITIONMANAGER() {
        return PARTITIONMANAGER;
    }
    public static ActorRef<AlertSensor.Alert> getAlertSensor() {
        return ALERTSENSOR;
    }
    public static boolean isReady() {
        return isReady;
    }
    public static ActorRef<SensorCommand> getHEALTHCHECKER() {
        return HEALTHCHECKER;
    }


    public static Behavior<Void> create() {
        return Behaviors.setup(
                context -> {
                    init(context);

                    return Behaviors.receive(Void.class)
                            .onSignal(Terminated.class, sig -> Behaviors.stopped())
                            .build();
                }
        );
    }


    /**
     * Init function will build actor system
     * 1. store useful static reference
     * 2. store the top level context ( in order to spawn, delete, cancel in the future)
     * 3. start scheduled mail box checker
     * 4. start needed partitions
     */
    private static void init(ActorContext<Void> context) {
        SYSTEM = context.getSystem();
        SCHEDULER = QuartzSchedulerExtension.get(SYSTEM.classicSystem());
        SENSORMANAGER = context.spawn(SensorManager.create(), "sensorManager");
        PARTITIONMANAGER = context.spawn(PartitionManager.create(), "partitionManager");
        ALERTSENSOR = context.spawn(AlertSensor.create(), "alertSensor");
        HEALTHCHECKER = context.spawn(HealthChecker.create(), "healthChecker");

        spawnPartition(userPartitionName);
        spawnPartition(eventPartitionName);

        SCHEDULER.createJobSchedule(
                "demenHealtherChecker",
                Adapter.toClassic(HEALTHCHECKER),
                new HealthChecker.HealthCheck(),
                Option.apply("listening mailboxes"),
                "*/10 * * ? * *",
                Option.empty(),
                TimeZone.getDefault()
        );

        isReady = true;
        context.getLog().info("Actor Manager Setup Complete");
    }

    private static void spawnPartition(String name) {
        PARTITIONMANAGER.tell(new PartitionManager.Spawn(name, SensorManager.create()));
    }
}
