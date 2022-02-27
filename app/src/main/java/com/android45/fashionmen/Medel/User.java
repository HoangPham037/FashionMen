package com.android45.fashionmen.Medel;

import android.net.Uri;

public class User {
    public String fullName, Email;
    private Uri uri;
    public void setUri(Uri uri) {
        this.uri = uri;
    }
    public User() {
    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.Email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }
}
