package com.flycode.transporttrackeryerevan.response;

import com.flycode.transporttrackeryerevan.model.ItemBus;

import java.util.ArrayList;

/**
 * Created on 10/17/16 __ Schumakher .
 */

public class BusesListResponse {
    private int count;
    private int currentPage;
    private int limit;
    private ArrayList<ItemBus> objs;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ArrayList<ItemBus> getObjs() {
        return objs;
    }

    public void setObjs(ArrayList<ItemBus> objs) {
        this.objs = objs;
    }
}
