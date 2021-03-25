package edu.northeastern.base.sensor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import edu.northeastern.base.manager.EventManager;
import edu.northeastern.base.manager.MailBoxManager;

/**
 * Created by Jim Z on 12/26/20 17:18
 */
public class HealthChecker extends AbstractSensor {

    public static final class HealthCheck implements SensorCommand {

    }

    public HealthChecker(ActorContext<SensorCommand> context) {
        super(context);
    }

    @Override
    public Receive<SensorCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(HealthCheck.class, this::onHealthCheck)
                .build();
    }

    private Behavior<SensorCommand> onHealthCheck(HealthCheck healthCheck) {
        MailBoxManager.getInstance().checkAllMailBoxes();
        EventManager.getInstance().checkExpiredEvents();
        return this;
    }

    public static Behavior<SensorCommand> create() {
        return Behaviors.setup(HealthChecker::new);
    }
}
