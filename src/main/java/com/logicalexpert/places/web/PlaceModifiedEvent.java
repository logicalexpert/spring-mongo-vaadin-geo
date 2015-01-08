package com.logicalexpert.places.web;

import com.logicalexpert.places.domain.GPlace;

public class PlaceModifiedEvent {
    private final GPlace place;

    public GPlace getPlace() {
        return place;
    }

    public PlaceModifiedEvent(GPlace place) {
        this.place = place;
    }
}