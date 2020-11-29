package edu.northeastern;

import akka.actor.typed.ActorSystem;
import edu.northeastern.examples.GreeterMain;
import edu.northeastern.process.serivce.PaymentService;

import java.io.IOException;

public class ProgramEntry {
  public static void main(String[] args) {
    PaymentService ps = new PaymentService();
    ps.makeAPayment("#001");
    System.out.println("Finished #001 payment request");
//    /*
//     Those are the code generate by the Akka Starter project
//     */
//    //#actor-system
//    final ActorSystem<GreeterMain.SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "helloakka");
//    //#actor-system
//
//    //#main-send-messages
//    greeterMain.tell(new GreeterMain.SayHello("Charles"));
//    //#main-send-messages
//
//    try {
//      System.out.println(">>> Press ENTER to exit <<<");
//      System.in.read();
//    } catch (IOException ignored) {
//    } finally {
//      greeterMain.terminate();
//    }
  }
}
