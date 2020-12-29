package edu.northeastern.process.beans;

import java.util.List;

/**
 * Created by Jim Z on 12/25/20 17:18
 */
public class MailBox {

    private List<String> messages;

    public MailBox(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addMessage(String msg) {
        this.messages.add(msg);
    }
}
