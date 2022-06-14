package com.revature.shoe.dtos.requests;

public class ReimbursementSort {
    private String columnName;
    private boolean ascending;

    public ReimbursementSort() {
        super();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }
}
