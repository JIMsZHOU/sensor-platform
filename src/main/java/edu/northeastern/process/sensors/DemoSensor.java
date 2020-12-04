package edu.northeastern.process.sensors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import edu.northeastern.base.sensor.AbstractSensor;
import edu.northeastern.base.sensor.SensorCommand;
import edu.northeastern.process.beans.DemoEntity;

import java.util.Optional;

/**
 * Created by Jim Z on 12/4/20 16:37
 */
public class DemoSensor extends AbstractSensor {

    /*
     Messages
     */
    public static final class Number implements SensorCommand {
        public String info;

        public Number(String info) {
            this.info = info;
        }
    }
    public static final class Query implements SensorCommand {
        public DemoEntity entity;
        public Query(DemoEntity entity) {
            this.entity = entity;
        }
    }
    public static final class Log implements SensorCommand {
        public String line;
        public Log(String line) {
            this.line = line;
        }
    }

    public DemoSensor(ActorContext<SensorCommand> context) {
        super(context);
    }

    public static Behavior<SensorCommand> create() {
        return Behaviors.setup(DemoSensor::new);
    }

    @Override
    public Receive<SensorCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Number.class, this::onNumber)
                .onMessage(Query.class, this::onQuery)
                .onMessage(Log.class, this::onLog)
                .build();
    }

    /*
     Message handler
     */
    private Behavior<SensorCommand> onNumber(Number number) {
        getContext().getLog().info(number.info);
        return this;
    }

    private Behavior<SensorCommand> onQuery(Query query) {
        if (query.entity.isSuccess()) {
            getContext().getLog().warn("database table update success");
        } else {
            getContext().getLog().info("scheduled check database table");
        }
        return this;
    }

    private Behavior<SensorCommand> onLog(Log log) {
        if (log.line.contains("**88**")) {
            getContext().getLog().error("");
        }
        return this;
    }
}
