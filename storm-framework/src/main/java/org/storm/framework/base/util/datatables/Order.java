package org.storm.framework.base.util.datatables;

import java.io.Serializable;

public class Order implements Serializable {

    private int column;
    private String dir;

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
