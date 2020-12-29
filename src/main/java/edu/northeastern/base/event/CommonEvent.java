package edu.northeastern.base.event;

import edu.northeastern.base.condition.CommonCondition;
import edu.northeastern.base.condition.Condition;
import edu.northeastern.base.enums.AlertLevel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jim Z on 12/25/20 03:42
 */
public class CommonEvent extends Event{


    private String message;
    private Map<String, Condition> conditions;

    public CommonEvent(String key, AlertLevel level, List<CommonCondition> conditions, String receiverName, Date expireDate, String message) {
        super(key, level, expireDate, receiverName);
        buildMap(conditions);
        this.message = message;
    }

    private void buildMap(List<CommonCondition> conditions) {
        this.conditions = new HashMap<>();
        for (CommonCondition c : conditions) {
            this.conditions.put(c.getKey(), c);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Map<String, Condition> getConditions() {
        return conditions;
    }

    public void addConditions(CommonCondition condition) {
        this.conditions.put(condition.getKey(), condition);
    }

    @Override
    public void removeCondition(String key) {
        this.conditions.remove(key);
    }
}
