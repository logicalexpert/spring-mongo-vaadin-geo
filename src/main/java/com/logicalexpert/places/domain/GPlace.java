package com.logicalexpert.places.domain;



import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import se.walkercrou.places.Place;

@Document
public class GPlace implements Serializable {

    @Id
    private String id;

    @GeoSpatialIndexed(name = "position")
    private double[] position;
    private double latitude;
    private double longitude;
    private Date insertionDate;
    private String category;
    private String types;
    private String name;
    private String address;
	private String hours;

	private String vicinity;

    public GPlace(Place p, String category) {
    	p=p.getDetails();
    	this.insertionDate = new Date();
        this.id = p.getId();
        this.name = p.getName();
        this.category=category;
        this.types = p.getTypes().stream().reduce((f, l) -> f + "," + l).get();
        this.latitude = p.getLatitude();
        this.longitude = p.getLongitude();
        this.address=p.getAddress();
        this.vicinity = p.getVicinity();
        this.hours = p.getHours()!=null?p.getHours().toString():null;
        this.position = new double[]{this.longitude, this.latitude};
    }

    public GPlace() {
    }
    
    public GPlace(String id) {
    	this.id = id;
    }

    public String getId() {
    	return id;
    }
    
    public void setId(String id) {
    	this.id = id;
    }
    
	public double[] getPosition() {
		return position;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getInsertionDate() {
		return insertionDate;
	}

	public void setInsertionDate(Date insertionDate) {
		this.insertionDate = insertionDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	@Override
	public String toString() {
		return "GPlace [id=" + id + ", position=" + Arrays.toString(position)
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", insertionDate=" + insertionDate + ", category=" + category
				+ ", name=" + name + ", address=" + address + ", hours="
				+ hours + "]";
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

}
