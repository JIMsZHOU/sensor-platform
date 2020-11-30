package edu.northeastern;

import akka.actor.typed.ActorSystem;
import edu.northeastern.base.ActorManager;
import edu.northeastern.base.SensorManager;
import edu.northeastern.process.sensors.DemoSensor;

/**
 * The entry of the project
 */
public class ProgramEntry {
  public static void main(String[] args) {
    // Init actor system
    ActorSystem<Void> system = ActorSystem.create(ActorManager.create(), "actorManager");
    while (!ActorManager.isReady()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // From here is the demo process:

    // First:
    // create a new sensor at Main process
    // Here use Demo Sensor you can inspect the code inside DemoSensor.class
    ActorManager.getSensorManager().tell(new SensorManager.Add("demo", DemoSensor.create()));

    // Seconds:
    // send sensor message within the process
    for (int i = 0; i < 11; i++) {
      ActorManager.getSensorManager().tell(new SensorManager.Send("demo", new DemoSensor.Msg()));
    }

    // Finally:
    // At the end of the process, send message to stop process
    ActorManager.getSensorManager().tell(new SensorManager.Stop("demo"));
    system.terminate();
  }
}
