package edu.northeastern.base.enums;

/**
 * Created by Jim Z on 12/25/20 03:46
 */
public enum AlertLevel {
    HIGH(0),
    MED(1),
    LOW(2),
    URGENT(3);

    public final int label;

    private AlertLevel(int label) {
        this.label = label;
    }
}
