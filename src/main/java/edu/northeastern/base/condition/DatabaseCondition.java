package edu.northeastern.base.condition;

import edu.northeastern.base.dao.DatabaseRetrieve;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jim Z on 12/25/20 04:33
 */
public class DatabaseCondition extends Condition<String, String>{

    public DatabaseCondition(String key, String value) {
        super(key, value);
    }

}
