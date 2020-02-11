package com.sale.supermarketboot.enums;

public enum TaskType {

    TASK_COMMODITY("commodity","更新库存"),
    TASK_ORDER("order","更新订单状态"),
    ;
    private String code;
    private String description;

    TaskType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
