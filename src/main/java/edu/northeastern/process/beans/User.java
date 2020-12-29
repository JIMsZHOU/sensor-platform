package edu.northeastern.process.beans;

/**
 * Created by Jim Z on 12/25/20 17:14
 */


public class User {
    private String ID;
    private MailBox mailBox;

    public User(String ID, MailBox mailBox) {
        this.ID = ID;
        this.mailBox = mailBox;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public MailBox getMailBox() {
        return mailBox;
    }

    public void setMailBox(MailBox mailBox) {
        this.mailBox = mailBox;
    }
}
