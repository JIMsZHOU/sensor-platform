package edu.northeastern.base.condition;

/**
 * Created by Jim Z on 12/25/20 03:35
 */
public abstract class Condition <T, V> {
    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    private T key;

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    private V value;

    public Condition(T key, V value) {
        this.key = key;
        this.value = value;
    }

    public boolean isMatched(T key, V value) {
        return this.getKey().equals(key) && this.getValue().equals(value);
    }
}
