package edu.northeastern.process.sensors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Adapter;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import edu.northeastern.base.ActorManager;
import edu.northeastern.base.AlertSensor;
import scala.Option;
import java.util.TimeZone;

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

    public static final class Msg implements SensorCommand { }
    public static final class Schedule implements SensorCommand { }

    private int cnt = 0;

    private DemoSensor(ActorContext<SensorCommand> context) {
        super(context);
    }

    @Override
    public Receive<SensorCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Schedule.class, this::onSchdule)
                .onMessage(Msg.class, this::onUpdate)
                .build();
    }

    public static Behavior<SensorCommand> create() {
        return Behaviors.setup(DemoSensor::new);
    }

    private Behavior<SensorCommand> onUpdate(Msg msg) {
        cnt++;
        if (cnt > 10) {
            ActorManager.getAlertSensor().tell(new AlertSensor.Alert("Demo sensor received 10+ times message"));
        }
        return this;
    }

    private Behavior<SensorCommand> onSchdule(Schedule schedule) {
        createScheduleTask();
        return this;
    }

    // TODO: extract it to parent class
    private void createScheduleTask() {
        ActorManager.getScheduler().createJobSchedule(
                "demo scheduler",
                Adapter.toClassic(ActorManager.getAlertSensor()),
                new AlertSensor.Alert("schduled message"),
                Option.apply("Demo"),
                "*/1 * * ? * *",
                Option.empty(),
                TimeZone.getDefault()
        );
    }
}
