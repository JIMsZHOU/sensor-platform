package edu.northeastern.base;

import akka.actor.AbstractActor;
import akka.actor.Scheduler;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension;

import java.util.Optional;

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
public class ActorManager {

    private static ActorSystem SYSTEM;
    private static QuartzSchedulerExtension SCHEDULER;
    private static ActorRef<SensorManager.SensorManagerCommand> SENSORMANAGER;
    private static ActorRef<AlertSensor.Alert> ALERTSENSOR;
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
    public static ActorRef<AlertSensor.Alert> getAlertSensor() {
        return ALERTSENSOR;
    }
    public static boolean isReady() {
        return isReady;
    }

    public static Behavior<Void> create() {
        return Behaviors.setup(
                context -> {
                    SYSTEM = context.getSystem();
                    SCHEDULER = QuartzSchedulerExtension.get(SYSTEM.classicSystem());
                    SENSORMANAGER = context.spawn(SensorManager.create(), "sensorManager");
                    ALERTSENSOR = context.spawn(AlertSensor.create(), "alertSensor");
                    isReady = true;
                    context.getLog().info("Actor Manager Setup Complete");
                    return Behaviors.receive(Void.class)
                            .onSignal(Terminated.class, sig -> Behaviors.stopped())
                            .build();
                }
        );
    }
}
