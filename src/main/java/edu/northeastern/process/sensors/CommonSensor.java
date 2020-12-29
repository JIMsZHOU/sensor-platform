package edu.northeastern.process.sensors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import edu.northeastern.base.condition.CommonCondition;
import edu.northeastern.base.condition.Condition;
import edu.northeastern.base.event.CommonEvent;
import edu.northeastern.base.event.Event;
import edu.northeastern.base.manager.ActorManager;
import edu.northeastern.base.manager.EventManager;
import edu.northeastern.base.manager.PartitionManager;
import edu.northeastern.base.manager.SensorManager;
import edu.northeastern.base.sensor.AbstractSensor;
import edu.northeastern.base.sensor.SensorCommand;

import java.util.Map;

/**
 * Created by Jim Z on 12/26/20 17:17
 */
public class CommonSensor extends AbstractSensor {

    private final String id;

    public static final class Update implements SensorCommand {
        public CommonCondition condition;
        public Update(CommonCondition condition) {
            this.condition = condition;
        }
    }

    public CommonSensor(String id, ActorContext<SensorCommand> context) {
        super(context);
        this.id = id;
    }

    @Override
    public Receive<SensorCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Update.class, this::onUpdate)
                .build();
    }

    private Behavior<SensorCommand> onUpdate(Update update) {
        CommonCondition cur = update.condition;
        CommonEvent e = (CommonEvent) EventManager.getInstance().getEvent(id);
        for (String cid :  e.getConditions().keySet()) {
            CommonCondition c = (CommonCondition) e.getConditions().get(cid);
            if (c.isMatched(cur.getKey(), cur.getValue())) {
                PartitionManager.getInstance().getPartition(ActorManager.userPartitionName).tell(new SensorManager.Send(e.getReceiverName(), new UserSensor.Mail(e.getMessage())));
            }
        }
        return this;
    }

    public static Behavior<SensorCommand> create(String id) {
        return Behaviors.setup(param -> new CommonSensor(id, param));
    }
}
