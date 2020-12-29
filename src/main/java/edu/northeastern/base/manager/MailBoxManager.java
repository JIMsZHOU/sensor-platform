package edu.northeastern.base.manager;

import edu.northeastern.process.beans.MailBox;
import edu.northeastern.process.beans.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jim Z on 12/25/20 17:21
 */
public class MailBoxManager {

    private static MailBoxManager instance;
    private MailBoxManager() {
        this.mailBoxes = new ArrayList<>();
    }
    public static synchronized MailBoxManager getInstance() {
        if (instance == null) {
            instance = new MailBoxManager();
        }
        return instance;
    }

    private List<User> mailBoxes;

    public void checkAllMailBoxes() {
        for (User u : mailBoxes) {
            MailBox m = u.getMailBox();
            int size = m.getMessages().size();
            for (String msg : m.getMessages()) {
                ActorManager.getAlertSensor().tell(new AlertSensor.Alert("Message: "+ msg +" for user: " + u.getID() + " is not read"));
            }
        }
    }

    public List<User> getAllUsers() {
        return this.mailBoxes;
    }

    public void addNewMailBox(User user) {
        this.mailBoxes.add(user);
    }

    public void removeMailBox(String userID) {
        for (User u : mailBoxes) {
            if (u.getID().equals(userID)) {
                this.mailBoxes.remove(u);
                break;
            }
        }
    }

    public User addNewUser(String id) {
        for (User u : mailBoxes) {
            if (u.getID().equals(id)) {
                return null;
            }
        }
        User res = new User(id, new MailBox(new ArrayList<>()));
        this.mailBoxes.add(res);
        return res;
    }

    public User getUser(String id) {
        for (User u : mailBoxes) {
            if (u.getID().equals(id)) {
                return u;
            }
        }
        return null;
    }

    public List<String> getMails(String userID) {
        User user = getUser(userID);
        return user == null ? null : user.getMailBox().getMessages();
    }

    public void readMails(String userID) {
        User u = getUser(userID);
        u.getMailBox().setMessages(new ArrayList<>());
    }
}
