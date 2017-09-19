package com.yuntu.validator.support;

public enum Sex{
    men(){
        @Override
        public String toString() {
            return "男";
        }
    } ,
    women(){
        @Override
        public String toString() {
            return "女";
        }
    }
}
