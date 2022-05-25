package com.ezlol.mesh.stableezchat.model;

public class Event {
    public static final String MESSAGE_NEW = "message_new";
    public static final String MESSAGE_CHANGE_STATUS = "message_change_status";
    public static final String MESSAGE_CHAT_NEW_MEMBER = "message_chat_new_member";
    public static final String MESSAGE_TYPING_STATE = "message_typing_state";

    public String type;
    public Object object;
    public Integer time;

    public Event(String type, Object object, int time) {
        this.type = type;
        this.object = object;
        this.time = time;
    }
}
