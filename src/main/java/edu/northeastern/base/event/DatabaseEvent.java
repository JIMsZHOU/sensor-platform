package edu.northeastern.base.event;

import edu.northeastern.base.condition.CommonCondition;
import edu.northeastern.base.condition.Condition;
import edu.northeastern.base.condition.DatabaseCondition;
import edu.northeastern.base.dao.DatabaseRetrieve;
import edu.northeastern.base.enums.AlertLevel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jim Z on 12/25/20 04:14
 */
public class DatabaseEvent extends Event{

    private String url;
    private String query;

    private Map<String, Condition> conditions;

    public DatabaseEvent(String key, AlertLevel level, Date expireTime, String receiverName, String url, String query, List<DatabaseCondition> conditions) {
        super(key, level, expireTime, receiverName);
        this.conditions = new HashMap<>();
        buildMap(conditions);
        this.url = url;
        this.query = query;
    }

    private void buildMap(List<DatabaseCondition> conditions) {
        for (DatabaseCondition c : conditions) {
            this.conditions.put(c.getKey(), c);
        }
    }

    public ResultSet queryResult() {
        return DatabaseRetrieve.executeQuery(this.url, this.query);
    }

    @Override
    public Map<String, Condition> getConditions() {
        return conditions;
    }

    @Override
    public void removeCondition(String key) {
        this.conditions.remove(key);
    }
}
