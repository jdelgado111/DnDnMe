package com.example.janethdelgado.dndnme.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

//cat1 = rules
//cat2 = openness
//cat3 = experience
//cat4 = creativity

@ParseClassName("Stats")
public class Stats extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_PSTAT1 = "personalStat1";
    public static final String KEY_PSTAT2 = "personalStat2";
    public static final String KEY_PSTAT3 = "personalStat3";
    public static final String KEY_PSTAT4 = "personalStat4";

    public static final String KEY_SSTAT1 = "searchStat1";
    public static final String KEY_SSTAT2 = "searchStat2";
    public static final String KEY_SSTAT3 = "searchStat3";
    public static final String KEY_SSTAT4 = "searchStat4";

    public static final String KEY_PREF = "preference";

    public Stats() {
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }

    public double getPStat1() {
        return getDouble(KEY_PSTAT1);
    }
    public void setPStat1(double newStat) {
        put(KEY_PSTAT1, newStat);
    }

    public double getPStat2() {
        return getDouble(KEY_PSTAT2);
    }
    public void setPStat2(double newStat) {
        put(KEY_PSTAT2, newStat);
    }

    public double getPStat3() {
        return getDouble(KEY_PSTAT3);
    }
    public void setPStat3(double newStat) {
        put(KEY_PSTAT3, newStat);
    }

    public double getPStat4() {
        return getDouble(KEY_PSTAT4);
    }
    public void setPStat4(double newStat) {
        put(KEY_PSTAT4, newStat);
    }

    public double getSStat1() {
        return getDouble(KEY_SSTAT1);
    }
    public void setSStat1(double newStat) {
        put(KEY_SSTAT1, newStat);
    }

    public double getSStat2() {
        return getDouble(KEY_SSTAT2);
    }
    public void setSStat2(double newStat) {
        put(KEY_SSTAT2, newStat);
    }

    public double getSStat3() {
        return getDouble(KEY_SSTAT3);
    }
    public void setSStat3(double newStat) {
        put(KEY_SSTAT3, newStat);
    }

    public double getSStat4() {
        return getDouble(KEY_SSTAT4);
    }
    public void setSStat4(double newStat) {
        put(KEY_SSTAT4, newStat);
    }

    public String getPreference() {
        return getString(KEY_PREF);
    }
    public void setPreference(String pref) {
        put(KEY_PREF, pref);
    }
}