package com.example.janethdelgado.dndnme;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Stats")
public class Stats extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_PSTAT1 = "personalStat1";
    public static final String KEY_PSTAT2 = "personalStat2";
    public static final String KEY_PSTAT3 = "personalStat3";
    public static final String KEY_PSTAT4 = "personalStat4";
    public static final String KEY_PSTAT5 = "personalStat5";
    public static final String KEY_SSTAT1 = "searchStat1";
    public static final String KEY_SSTAT2 = "searchStat2";
    public static final String KEY_SSTAT3 = "searchStat3";
    public static final String KEY_SSTAT4 = "searchStat4";
    public static final String KEY_SSTAT5 = "searchStat5";

    public Stats() {
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public int getPStat1() {
        return getInt(KEY_PSTAT1);
    }
    public void setPStat1(int newStat) {
        put(KEY_PSTAT1, newStat);
    }

    public int getPStat2() {
        return getInt(KEY_PSTAT2);
    }
    public void setPStat2(int newStat) {
        put(KEY_PSTAT2, newStat);
    }

    public int getPStat3() {
        return getInt(KEY_PSTAT3);
    }
    public void setPStat3(int newStat) {
        put(KEY_PSTAT3, newStat);
    }

    public int getPStat4() {
        return getInt(KEY_PSTAT4);
    }
    public void setPStat4(int newStat) {
        put(KEY_PSTAT4, newStat);
    }

    public int getPStat5() {
        return getInt(KEY_PSTAT5);
    }
    public void setPStat5(int newStat) {
        put(KEY_PSTAT5, newStat);
    }

    public int getSStat1() {
        return getInt(KEY_SSTAT1);
    }
    public void setSStat1(int newStat) {
        put(KEY_SSTAT1, newStat);
    }

    public int getSStat2() {
        return getInt(KEY_SSTAT2);
    }
    public void setSStat2(int newStat) {
        put(KEY_SSTAT2, newStat);
    }

    public int getSStat3() {
        return getInt(KEY_SSTAT3);
    }
    public void setSStat3(int newStat) {
        put(KEY_SSTAT3, newStat);
    }

    public int getSStat4() {
        return getInt(KEY_SSTAT4);
    }
    public void setSStat4(int newStat) {
        put(KEY_SSTAT4, newStat);
    }

    public int getSStat5() {
        return getInt(KEY_SSTAT5);
    }
    public void setSStat5(int newStat) {
        put(KEY_SSTAT5, newStat);
    }
}
