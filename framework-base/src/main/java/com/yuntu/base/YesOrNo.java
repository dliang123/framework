package com.yuntu.base;

public enum YesOrNo implements Describable {

    NO("否"), YES("是");
    private String description;

    YesOrNo(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}