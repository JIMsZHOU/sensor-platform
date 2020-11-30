package edu.northeastern.process.sensors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import edu.northeastern.base.ActorManager;
import edu.northeastern.base.AlertSensor;

/**
 * Created by Jim Z on 11/29/20 18:40
 *
 * This is the Demo Sensor
 *
 * Implementations required by user:
 *
 * 1. Give default constructor or enrich by user
 * 2. define Message that sensor will receive (should implement top type SensorCommand interface)
 * 3. define actions when receive message
 * 4. Inside actions should define the workflow when meet conditions
 */
public class DemoSensor extends AbstractSensor {

    private int cnt = 0;

    private DemoSensor(ActorContext<SensorCommand> context) {
        super(context);
    }

    @Override
    public Receive<SensorCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Msg.class, this::onMessage)
                .build();
    }

    public static Behavior<SensorCommand> create() {
        return Behaviors.setup(DemoSensor::new);
    }

    public static final class Msg implements SensorCommand { }

    private Behavior<SensorCommand> onMessage(Msg msg) {
        cnt++;
        if (cnt > 10) {
            ActorManager.getAlertSensor().tell(new AlertSensor.Alert("Demo sensor received 10+ times message"));
        }
        return this;
    }
}
