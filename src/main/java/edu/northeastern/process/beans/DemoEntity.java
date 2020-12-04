package edu.northeastern.process.beans;

import javax.persistence.*;

/**
 * Created by Jim Z on 12/3/20 20:58
 */
@Table
@Entity
public class DemoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String data;
    private boolean success;
    private String email;
    private int cnt;

    public DemoEntity(String data, String email) {
        this.data = data;
        this.success = false;
        this.email = email;
        this.cnt = 0;
    }

    public DemoEntity() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
