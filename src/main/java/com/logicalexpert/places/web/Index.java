package com.logicalexpert.places.web;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.events.EventBus;

import com.logicalexpert.places.service.PlaceRepository;
import com.logicalexpert.places.web.inner.CategoryView;
import com.logicalexpert.places.web.inner.PlaceForm;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
//import org.springframework.data.jpa.domain.


@Theme("valo")
@Widgetset("AppWidgetset")
@VaadinUI
public class Index extends UI {

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceForm form;

    @Autowired
    private EventBus eventBus;

	
    @Override
    protected void init(VaadinRequest request) {

        Page.getCurrent().setTitle("Spring Boot, MongoDB, Vaadin, Google Places - Demo");

        TabSheet sheet = new TabSheet();
        sheet.setSizeFull();
        Arrays.asList("park","food","movie_theater","hospital","bank","school","police").forEach(tfilter->
        sheet.addComponents(new CategoryView(tfilter, placeRepository, form, eventBus, sheet)));
        setContent(sheet);
    }
}



