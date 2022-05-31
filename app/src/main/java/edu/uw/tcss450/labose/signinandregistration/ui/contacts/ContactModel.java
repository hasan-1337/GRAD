package edu.uw.tcss450.labose.signinandregistration.ui.contacts;

import java.io.Serializable;

public class ContactModel implements Serializable {
    String id;
    String name;
    String email;

    public ContactModel(String email) { this.email = email; }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}