package edu.northeastern.process.sensors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;

/**
 * Created by Jim Z on 11/30/20 13:49
 */
public abstract class AbstractSensor extends AbstractBehavior<SensorCommand> {
    public AbstractSensor(ActorContext<SensorCommand> context) {
        super(context);
    }
    @Override
    public abstract Receive<SensorCommand> createReceive();
}
