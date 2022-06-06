package edu.uw.tcss450.labose.signinandregistration.ui.contacts;

import java.io.Serializable;

/**
 * The Model used for the contact classes.
 */
public class ContactModel implements Serializable {
    int id;
    String name;
    String email;

    public ContactModel(String email) { this.email = email; }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}