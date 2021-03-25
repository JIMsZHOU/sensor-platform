package edu.northeastern.process.serivce;

import akka.actor.typed.ActorRef;
import edu.northeastern.base.manager.ActorManager;
import edu.northeastern.base.manager.MailBoxManager;
import edu.northeastern.base.manager.PartitionManager;
import edu.northeastern.base.manager.SensorManager;
import edu.northeastern.process.beans.User;
import edu.northeastern.process.sensors.UserSensor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jim Z on 12/25/20 17:33
 */
@Service
public class UserService {

    private ActorRef<SensorManager.SensorManagerCommand> userPartition;

    @PostConstruct
    private void init() {
        userPartition = PartitionManager.getInstance().getPartition("user-partition");
        while (userPartition == null) {
            userPartition = PartitionManager.getInstance().getPartition(ActorManager.userPartitionName);
        }
    }

    public String addUser(String userID) {
        User user =  MailBoxManager.getInstance().addNewUser(userID);
        if (user == null) {
            return "user id already exist";
        } else {
            userPartition.tell(new SensorManager.Add(userID, UserSensor.create(userID)));
            return "Success add new user: " + userID;
        }
    }

    public List<String> getAllUser() {
        List<User> users = MailBoxManager.getInstance().getAllUsers();
        return users.stream().map(User::getID).collect(Collectors.toList());
    }

    public String sendMessage(String userID, String message) {
        if (MailBoxManager.getInstance().getUser(userID) == null) {
            return "No user with id: " + userID;
        } else {
            userPartition.tell(new SensorManager.Send(userID, new UserSensor.Mail(message)));
            return "Send message to [user: "+userID+"] with [message: "+message+"]";
        }
    }

    public List<String> getMails(String userID) {
        List<String> res = MailBoxManager.getInstance().getMails(userID);
        return res == null ? Collections.emptyList() : res;
    }

    public void readMails(String userID) {
        MailBoxManager.getInstance().readMails(userID);
    }
}
