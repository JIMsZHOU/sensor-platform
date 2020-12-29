package edu.northeastern.base.event;

import edu.northeastern.base.condition.Condition;
import edu.northeastern.base.enums.AlertLevel;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jim Z on 12/25/20 03:32
 */
public abstract class Event {
    private String key;
    private AlertLevel level;
    private Date expireTime;
    private String receiverName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AlertLevel getLevel() {
        return level;
    }

    public void setLevel(AlertLevel level) {
        this.level = level;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Event(String key, AlertLevel level, Date expireTime, String receiverName) {
        this.key = key;
        this.level = level;
        this.expireTime = expireTime;
        this.receiverName = receiverName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public abstract Map<String, Condition> getConditions();

    public abstract void removeCondition(String key);
}
