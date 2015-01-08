package com.logicalexpert.places.web.inner;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.vaadin.maddon.button.ConfirmButton;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;

import com.logicalexpert.places.domain.GPlace;
import com.logicalexpert.places.service.PlaceRepository;
import com.logicalexpert.places.web.PlaceModifiedEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;


@UIScope
@VaadinComponent
public class ListView extends MVerticalLayout implements InitializingBean {
	private String categoryName;
    private PlaceRepository placeRepository;
    private PlaceForm form;
    private EventBus eventBus;
    
    public ListView(String categoryName, PlaceRepository placeRepository,PlaceForm form,EventBus eventBus) {
    	this.categoryName=categoryName;
    	this.placeRepository=placeRepository;
    	this.form=form;
    	this.eventBus=eventBus;
    	this.eventBus.subscribe(this);
    	init();
    }

    private MTable<GPlace> list = new MTable<>(GPlace.class)
            .withProperties("name", "category", "address", "hours")
            .withColumnHeaders("Name", "Category", "Address", "Hours")
            .withFullWidth();

    private Button edit = new MButton(
            FontAwesome.PENCIL_SQUARE_O,
            clickEvent -> {
                form.setEntity(this.placeRepository.findOne(list.getValue().getId()));
                form.openInModalPopup().setHeight("90%");
            });

    private Button delete = new ConfirmButton(FontAwesome.TRASH_O,
            "Are you sure you want to delete the entry?",
            clickEvent -> {
                GPlace place = list.getValue();
                placeRepository.delete(place);
                list.setValue(null);
                listEntities();
                eventBus.publish(this, new PlaceModifiedEvent(place));
            });

    protected void init() {
        setCaption("Details");
        addComponents(new MVerticalLayout(new MHorizontalLayout(edit, delete), list).expand(list));
        listEntities();
        list.addMValueChangeListener(e -> adjustActionButtonState());
    }

    private void adjustActionButtonState() {
        boolean hasSelection = list.getValue() != null;
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    private void listEntities() {
        list.setBeans((Collection<GPlace>) placeRepository.findByCategory(getCategoryName()));
        adjustActionButtonState();
    }

    @EventBusListenerMethod
    protected void onPlaceModifiedEvent(PlaceModifiedEvent event) {
        listEntities();
        UI.getCurrent().getWindows().forEach(UI.getCurrent()::removeWindow);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventBus.subscribe(this);
    }
    
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
