package com.android.tutorapp.Chat;

public class ChatMessage {
    public boolean left;
    public String offer;
    public String msg_id;
    public String message;
    public String amount;
    public String month;
    public String days;
    public String hours;
    public String description;
    public String name;
    public String date;
    public String withdraw;

    public String getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(String withdraw) {
        this.withdraw = withdraw;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
        this.offer = "N";
    }

    public ChatMessage(boolean left, String description, String amount, String month, String days, String hours, String msg_id, String withdraw) {
        super();
        this.left = left;
        this.offer = "Y";
        this.description = description;
        this.amount = amount;
        this.month = month;
        this.days = days;
        this.hours = hours;
        this.msg_id = msg_id;
        this.withdraw = withdraw;
    }

    public ChatMessage(boolean left, String message, String date) {
        super();
        this.left = left;
        this.message = message;
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}