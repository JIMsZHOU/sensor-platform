package edu.northeastern.process.sensors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import edu.northeastern.base.condition.Condition;
import edu.northeastern.base.condition.DatabaseCondition;
import edu.northeastern.base.event.DatabaseEvent;
import edu.northeastern.base.manager.*;
import edu.northeastern.base.sensor.AbstractSensor;
import edu.northeastern.base.sensor.SensorCommand;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Jim Z on 12/29/20 14:16
 */
public class DatabaseSensor extends AbstractSensor {

    private final String id;

    public DatabaseSensor(String id, ActorContext<SensorCommand> context) {
        super(context);
        this.id = id;
    }

    public static Behavior<SensorCommand> create(String key) {
        return Behaviors.setup(param -> new DatabaseSensor(key, param));
    }

    public static final class Matching implements SensorCommand {

    }

    @Override
    public Receive<SensorCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Matching.class, this::onMatching)
                .build();
    }

    private Behavior<SensorCommand> onMatching(Matching matching) {
        System.out.println("scheduled check database matching");
        DatabaseEvent event = (DatabaseEvent) EventManager.getInstance().getEvent(id);
        ResultSet rs = event.queryResult();
        try {
            rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Map<String, Condition> conditionMap = event.getConditions();
        for (String key : conditionMap.keySet()) {
            System.out.println("checking key: " + key);
            try {
                String v = rs.getString(key);
                DatabaseCondition c = (DatabaseCondition) conditionMap.get(key);
                if (c.isMatched(key, v)) {
                    PartitionManager.getInstance().getPartition(ActorManager.userPartitionName).tell(new SensorManager.Send(event.getReceiverName(), new UserSensor.Mail("Database condition with key: " + key + " is matched")));
                }
                System.out.println("Query result: " + key + ":" + v);
            } catch (SQLException throwables) {
                event.removeCondition(key);
                ActorManager.getAlertSensor().tell(new AlertSensor.Alert("Don't have column with key:" + key));
                throwables.printStackTrace();
            }
        }
        return this;
    }
}
