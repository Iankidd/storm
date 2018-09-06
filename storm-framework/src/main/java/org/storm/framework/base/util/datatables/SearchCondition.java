package org.storm.framework.base.util.datatables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchCondition implements Serializable {

    private int draw;
    private List<Column> columns = new ArrayList<Column>();
    private List<Order> orders = new ArrayList<Order>();
    private int start = 0;
    private int length = 25;
    private Search search = new Search();

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
