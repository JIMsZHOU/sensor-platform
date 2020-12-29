package edu.northeastern.process.sensors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import edu.northeastern.base.manager.MailBoxManager;
import edu.northeastern.base.sensor.AbstractSensor;
import edu.northeastern.base.sensor.SensorCommand;

/**
 * Created by Jim Z on 12/26/20 16:41
 */
public class UserSensor extends AbstractSensor {
    private final String id;

    public static final class Mail implements SensorCommand {
        public String mail;
        public Mail(String mail) {
            this.mail = mail;
        }
    }

    public UserSensor(String id, ActorContext<SensorCommand> context) {
        super(context);
        this.id = id;
    }

    @Override
    public Receive<SensorCommand> createReceive() {
        return newReceiveBuilder()
                .onMessage(Mail.class, this::onMail)
                .build();
    }

    private Behavior<SensorCommand> onMail(Mail mail) {
        MailBoxManager.getInstance().getUser(id).getMailBox().addMessage(mail.mail);
        return this;
    }

    public static Behavior<SensorCommand> create(String id) {
        return Behaviors.setup(param -> new UserSensor(id, param));
    }
}
