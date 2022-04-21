package ru.dartanum.passwordmanager.domain;

import androidx.core.util.Pair;

import java.util.List;
import java.util.Map;

public class Record {
    private int id;
    private Category category;
    private String name;
    private List<Pair<Field, String>> valuesByField;

    public Record() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pair<Field, String>> getValuesByField() {
        return valuesByField;
    }

    public void setValuesByField(List<Pair<Field, String>> valuesByField) {
        this.valuesByField = valuesByField;
    }
}
