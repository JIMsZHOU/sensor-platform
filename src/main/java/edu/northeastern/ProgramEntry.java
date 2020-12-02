package edu.northeastern;

import akka.actor.typed.ActorSystem;
import edu.northeastern.base.ActorManager;
import edu.northeastern.base.SensorManager;
import edu.northeastern.process.sensors.DemoSensor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry of the project
 */
@SpringBootApplication
public class ProgramEntry {
  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(ProgramEntry.class, args);
    // Init actor system
    ActorSystem<Void> system = ActorSystem.create(ActorManager.create(), "actorManager");
    while (!ActorManager.isReady()) {
      Thread.sleep(1000);
    }

    // From here is the demo process:

    // First:
    // create a new sensor at Main process
    // Here use Demo Sensor you can inspect the code inside DemoSensor.class
    ActorManager.getSensorManager().tell(new SensorManager.Add("demo", DemoSensor.create()));

    // create a scheduled job
    ActorManager.getSensorManager().tell(new SensorManager.Send("demo", new DemoSensor.Schedule()));
    // Seconds:
    // send sensor message within the process
    for (int i = 0; i < 11; i++) {
      ActorManager.getSensorManager().tell(new SensorManager.Send("demo", new DemoSensor.Msg()));
      Thread.sleep(2000);
    }

    // Finally:
    // At the end of the process, send message to stop process
    ActorManager.getSensorManager().tell(new SensorManager.Stop("demo"));
    system.terminate();
  }
}
