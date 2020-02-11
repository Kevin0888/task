package com.sale.supermarketboot.enums;

public enum TaskStatus {
    TASK_UNDO(1,"未执行"),
    TASK_DOING(2,"正在执行"),
    TASK_SUCCESS(3,"成功"),
    TASK_FAILED(4,"失败"),
    ;
    TaskStatus(Integer code,String description){
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Integer code;
    private String description;
}
