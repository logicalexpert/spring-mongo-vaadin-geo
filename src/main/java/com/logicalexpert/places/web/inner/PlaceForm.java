package com.logicalexpert.places.web.inner;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.easyuploads.MultiFileUpload;
import org.vaadin.maddon.MBeanFieldGroup;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.layouts.MFormLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;

import com.logicalexpert.places.domain.GPlace;
import com.logicalexpert.places.service.PlaceRepository;
import com.logicalexpert.places.web.PlaceModifiedEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;

@UIScope
@VaadinComponent
public class PlaceForm extends AbstractForm<GPlace> {

    private MTextField name = new MTextField("Name");
    private MTextField category = new MTextField("Category");
    private MTextField address = new MTextField("Address");
    private MTextField hours = new MTextField("Hours");
    private MultiFileUpload upload = new MultiFileUpload() {

        @Override
        protected void handleFile(File file, String fileName, String mimeType, long length) {
            try {
//                gridFsTemplate.store(new FileInputStream(file), place.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            displayImageIfAvailable(place.getId());
        }
    };
    private MTextField country = new MTextField("Country");

    private Image image;

    @Autowired
    private EventBus eventBus;

//    @Autowired
//    private GridFsTemplate gridFsTemplate;

    @Autowired
    private PlaceRepository placeRepository;

    private GPlace place;

    private MFormLayout layout;

    @Override
    public MBeanFieldGroup<GPlace> setEntity(GPlace entity) {
        MBeanFieldGroup<GPlace> placeMBeanFieldGroup = super.setEntity(entity);
        this.place = entity;
        return placeMBeanFieldGroup;
    }

    protected void displayImageIfAvailable(String imageId) {
        if (image != null) {
            layout.removeComponent(image);
        }
//        Optional.ofNullable(this.gridFsTemplate.getResource(imageId))
//                .ifPresent(gfr -> {
//                    image = new Image("Image", new StreamResource(
//                            (StreamResource.StreamSource) () -> {
//                                try {
//                                    return gridFsTemplate.getResource(imageId).getInputStream();
//                                } catch (IOException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            }, "photo" + this.place.getId() + System.currentTimeMillis() + ".jpg"));
//                    image.setWidth(400, Unit.PIXELS);
//                    layout.addComponent(image);
//                });
    }

    public PlaceForm() {

        this.setSizeUndefined();
        this.setEagarValidation(true);
        this.setSavedHandler(place -> {
            this.placeRepository.save(place);
            this.eventBus.publish(this, new PlaceModifiedEvent(place));
        });

        this.getResetButton().setVisible(false);
    }


    @Override
    protected Component createContent() {

        layout = new MFormLayout(
                this.name,
                this.category,
                this.address,
                this.hours,
                this.upload
        ).withWidth("");

        this.displayImageIfAvailable(this.place.getId());

        return new MVerticalLayout(layout, getToolbar()).withWidth("");
    }
}