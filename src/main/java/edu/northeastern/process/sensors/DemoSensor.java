package edu.northeastern.process.sensors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import edu.northeastern.base.manager.ActorManager;
import edu.northeastern.base.manager.AlertSensor;
import edu.northeastern.base.manager.SensorManager;
import edu.northeastern.base.sensor.AbstractSensor;
import edu.northeastern.base.sensor.SensorCommand;
import edu.northeastern.process.beans.DemoEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        public String file;
        public Log(String file) {
            this.file = file;
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
            ActorManager.getScheduler().cancelJob("databasedemo");
        } else {
            getContext().getLog().info("scheduled check database table");
        }
        return this;
    }

    private Behavior<SensorCommand> onLog(Log log) {
        // TODO: Current approach use Runtime process
        // May use jsch to send command to host machine later
        // monitor old.log and find 88

        try {
            String command = "grep -E '\\*\\*88\\*\\*' ./log/old.log";
            Process process = Runtime.getRuntime().exec(command);
            StringBuilder out = new StringBuilder();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line = "";
            int cnt = 0;
            while (
                    (line = br.readLine()) != null
            ) {
                if (line.contains("**88**") && !line.contains("no matches ")) {
                    cnt++;
                    out.append(line + "\n");
                }
            }
            int exit = process.waitFor();
            if (exit == 0) { // this means the command is running success
                // give alert to sensor with correct information
                if (cnt != 0) {
                    getContext().getLog().info(cnt +" of pattern **88** are caught from old log");
                    ActorManager.getScheduler().cancelJob("logdemo");
                }
            } else { // means the command is running failed
                // give alert to alert sensor with error message
//                ActorManager.getAlertSensor().tell(new AlertSensor.Alert("Command to catch the information from logfile is been interrupted"));
                // Here use log error to present the alert
                getContext().getLog().error("No pattern matched in old log retry later");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            ActorManager.getScheduler().cancelJob("logdemo");
        }
        return this;
    }
}
