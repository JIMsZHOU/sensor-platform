package edu.northeastern.base.manager;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * Created by Jim Z on 11/29/20 15:55
 *
 * Alert Actor is design to receive alert information from sensors
 * and give proper actions to handle the alert
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
        getContext().getLog().warn("[alert]  " + alert.info);
        return this;
    }
}
