package com.lindsey.coutil.sql.dto;

public class OperationResultDTO {

    private String sql;

    private Integer errorLineNumber;

    public OperationResultDTO() {
    }

    public OperationResultDTO(String sql, Integer errorLineNumber) {
        this.sql = sql;
        this.errorLineNumber = errorLineNumber;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Integer getErrorLineNumber() {
        return errorLineNumber;
    }

    public void setErrorLineNumber(Integer errorLineNumber) {
        this.errorLineNumber = errorLineNumber;
    }
}
