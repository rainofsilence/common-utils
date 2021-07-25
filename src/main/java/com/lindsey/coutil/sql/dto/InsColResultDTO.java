package com.lindsey.coutil.sql.dto;

public class InsColResultDTO {

    private String colType;

    private String colName;

    private String colValue;

    public InsColResultDTO() {
    }

    public InsColResultDTO(String colType, String colName, String colValue) {
        this.colType = colType;
        this.colName = colName;
        this.colValue = colValue;
    }

    @Override
    public String toString() {
        return "InsColResultDTO{" +
                "colType='" + colType + '\'' +
                ", colName='" + colName + '\'' +
                ", colValue='" + colValue + '\'' +
                '}';
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColValue() {
        return colValue;
    }

    public void setColValue(String colValue) {
        this.colValue = colValue;
    }
}
