package com.example.janethdelgado.dndnme;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Profile")
public class Profile extends ParseObject {
    // Ensure that your subclass has a public default constructor

    public static final String KEY_IMAGE = "profileImage";
    public static final String KEY_SHORT_BIO = "shortBio";
    public static final String KEY_LONG_BIO = "longBio";
    public static final String KEY_USER = "user";

    public ParseFile getProfileImage() {
        return getParseFile(KEY_IMAGE);
    }
    public void setProfileImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public String getShortBio() { return getString(KEY_SHORT_BIO); }
    public void setShortBio(String newBio) {
        put(KEY_SHORT_BIO, newBio);
    }

    public String getLongBio() {
        return getString(KEY_LONG_BIO);
    }
    public void setLongBio(String newBio) {
        put(KEY_LONG_BIO, newBio);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }
}
