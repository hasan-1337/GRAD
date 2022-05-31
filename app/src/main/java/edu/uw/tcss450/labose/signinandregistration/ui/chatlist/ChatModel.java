package edu.uw.tcss450.labose.signinandregistration.ui.chatlist;

import java.io.Serializable;

public class ChatModel implements Serializable {
    int chatID;

    public ChatModel(int chatID) { this.chatID = chatID; }

    public int getChatID() {
        return chatID;
    }

    public String getChatName() {
        return Integer.toString(chatID);
    }
}
