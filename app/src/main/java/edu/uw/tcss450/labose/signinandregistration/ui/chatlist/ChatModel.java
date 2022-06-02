package edu.uw.tcss450.labose.signinandregistration.ui.chatlist;

import java.io.Serializable;

public class ChatModel implements Serializable {
    int chatID;
    String chatName;

    public ChatModel(int chatID, String chatName) {
        this.chatID = chatID;
        this.chatName = chatName;
    }

    public int getChatID() {
        return chatID;
    }

    public String getChatName() {
        return chatName;
    }
}