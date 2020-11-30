package edu.northeastern;

import akka.actor.typed.ActorSystem;
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension;
import edu.northeastern.base.ActorManager;
import edu.northeastern.base.SensorManager;
import edu.northeastern.process.sensors.DemoSensor;

public class ProgramEntry {
  public static void main(String[] args) {
    ActorSystem<Void> system = ActorSystem.create(ActorManager.create(), "actorManager");
    while (!ActorManager.isReady()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    // create a new sensor at Main process
    // Here use Demo Sensor you can inspect the code inside DemoSensor.class
    ActorManager.getSensorManager().tell(new SensorManager.Add("demo", DemoSensor.create()));

    // send sensor message within the process
    for (int i = 0; i < 11; i++) {
      ActorManager.getSensorManager().tell(new SensorManager.Send("demo", new DemoSensor.Msg()));
    }

    // At the end of the process, send message to stop process
    ActorManager.getSensorManager().tell(new SensorManager.Stop("demo"));
    system.terminate();
  }
}
