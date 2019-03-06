package com.example.janethdelgado.dndnme;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Profile")
public class Profile extends ParseObject {
    // Ensure that your subclass has a public default constructor

    public static final String KEY_PROFILE_IMAGE = "profileImage";
    public static final String KEY_BIO = "bio";
    public static final String KEY_USER = "user";

    public ParseFile getProfileImage() {
        return getParseFile(KEY_PROFILE_IMAGE);
    }
    public void setProfileImage(ParseFile parseFile) {
        put(KEY_PROFILE_IMAGE, parseFile);
    }

    public String getBio() {
        return getString(KEY_BIO);
    }
    public void setBio(String newBio) {
        put(KEY_BIO, newBio);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }
}
