package com.minetwice.phantomsmp.models;

public class PowerBook {
    private String name;
    private BookAbility ability;

    public PowerBook(String name, BookAbility ability) {
        this.name = name;
        this.ability = ability;
    }

    public String getName() {
        return name;
    }

    public BookAbility getAbility() {
        return ability;
    }
}