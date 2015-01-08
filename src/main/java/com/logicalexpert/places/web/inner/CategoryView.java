package com.logicalexpert.places.web.inner;

import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.events.EventBus;

import com.logicalexpert.places.service.PlaceRepository;
import com.logicalexpert.places.web.inner.ListView;
import com.logicalexpert.places.web.inner.LocationView;
import com.logicalexpert.places.web.inner.PlaceForm;
import com.vaadin.ui.TabSheet;


public class CategoryView extends MVerticalLayout{

	private String categoryName;
	private PlaceRepository placeRepository;
    private PlaceForm form;
    private EventBus eventBus;
	private TabSheet topSheet;

    public CategoryView (String categoryName, PlaceRepository placeRepository,PlaceForm form,EventBus eventBus, TabSheet sheet) {
    	this.categoryName=categoryName;
    	this.placeRepository=placeRepository;
    	this.form=form;
    	this.eventBus=eventBus;
    	this.topSheet=sheet;
    	init();
    }
	public void init() {
		TabSheet sheet = new TabSheet();
        sheet.setSizeFull();
        sheet.setCaption(categoryName.toUpperCase());
		sheet.addComponents(new ListView(categoryName, placeRepository, form, eventBus));
		sheet.addComponents(new LocationView(categoryName, placeRepository, form, eventBus));
		topSheet.addComponent(sheet);
	}
}
