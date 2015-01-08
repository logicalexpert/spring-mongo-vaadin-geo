package com.logicalexpert.places.web.inner;

import java.io.Serializable;
import java.util.List;

import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;

import com.logicalexpert.places.domain.GPlace;
import com.logicalexpert.places.service.PlaceRepository;
import com.logicalexpert.places.web.PlaceModifiedEvent;
import com.vaadin.ui.Notification;

@UIScope
@VaadinComponent
public class LocationView extends LMap implements Serializable {

    private LLayerGroup layerGroup = new LLayerGroup();
	private String categoryName;
    private PlaceRepository placeRepository;
    private PlaceForm form;
    private EventBus eventBus;
    
    public LocationView(String categoryName, PlaceRepository placeRepository,PlaceForm form,EventBus eventBus) {
    	this.categoryName=categoryName;
    	this.placeRepository=placeRepository;
    	this.form=form;
    	this.eventBus=eventBus;
    	init();
    }

    @EventBusListenerMethod
    public void refreshPlaces(PlaceModifiedEvent o) {
        this.layerGroup.removeAllComponents();
        List<GPlace> places = this.placeRepository.findByCategory(getCategoryName());
        places.forEach(p -> {
            LMarker marker = new LMarker(p.getLatitude(), p.getLongitude());
            marker.addClickListener(leafletClickEvent -> {
                GPlace place = this.placeRepository.findOne(p.getId());
                this.form.setEntity(place);
                this.form.openInModalPopup().setHeight("90%");
            });
            this.layerGroup.addComponent(marker);
        });
        zoomToContent();
    }

    public void init() {
        setCaption("Map");
        addLayer(new LOpenStreetMapLayer());
        addLayer(this.layerGroup);
        addClickListener(e -> Notification.show("you clicked on " +
                e.getPoint().getLon() + ", " + e.getPoint().getLat()));
        this.eventBus.subscribe(this);
        this.refreshPlaces(null);
    }

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
