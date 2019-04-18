package com.example.janethdelgado.dndnme;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Questions")
public class Questions extends ParseObject {

    public static final String KEY_CAT = "category";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_QUES = "question";

    public String getCategory() {
        return getString(KEY_CAT);
    }
    public void setCategory(String cat) {
        put(KEY_CAT, cat);
    }

    public int getWeight() {
        return getInt(KEY_WEIGHT);
    }
    public void setWeight(int w) {
        put(KEY_WEIGHT, w);
    }

    public String getQuestion() {
        return getString(KEY_QUES);
    }
    public void setQuestion(String q) {
        put(KEY_QUES, q);
    }
}