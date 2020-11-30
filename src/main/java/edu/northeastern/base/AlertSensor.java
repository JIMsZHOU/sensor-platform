package edu.northeastern.base;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * Created by Jim Z on 11/29/20 15:55
 */
public class AlertSensor extends AbstractBehavior<AlertSensor.Alert> {
    // the message alert sensor (actor) can receive
    public static final class Alert {
        public final String info;

        public Alert(String info) {
            this.info = info;
        }
    }

    public static Behavior<Alert> create() {
        return Behaviors.setup(AlertSensor::new);
    }

    private AlertSensor(ActorContext<Alert> context) {
        super(context);
    }

    @Override
    public Receive<Alert> createReceive() {
        return newReceiveBuilder().onMessage(Alert.class, this::onAlert).build();
    }

    private Behavior<Alert> onAlert(Alert alert) {
        getContext().getLog().warn("This is the alert info:" + alert.info);
        return this;
    }
}
