package org.gradle;

import java.util.List;

import se.walkercrou.places.AddressComponent;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

public class PlaceFinder {
	public static void main(String[] args) {
		double latitude = 17.3990570;
        double longitude = 78.5099280;
        double radius = 5000;
        GooglePlaces client = new GooglePlaces("AIzaSyA1ncf6REriGv4Ed7-qo_65HojN-4cWKHI");
		List<Place> places = client.getNearbyPlaces(latitude, longitude, radius, 2);
		places.forEach(q -> debugPlace(q));
	}
	private static void debugPlace(Place place) {
		    Place detailedPlace = place.getDetails(); // sends a GET request for more details
		    // Just an example of the amount of information at your disposal:
		    System.out.println("ID: " + detailedPlace.getId());
		    System.out.println("Name: " + detailedPlace.getName());
		    System.out.println("Phone: " + detailedPlace.getPhoneNumber());
		    System.out.println("International Phone: " + place.getInternationalPhoneNumber());
		    System.out.println("Website: " + detailedPlace.getWebsite());
		    System.out.println("Always Opened: " + detailedPlace.isAlwaysOpened());
		    System.out.println("Status: " + detailedPlace.getStatus());
		    System.out.println("Google Place URL: " + detailedPlace.getGoogleUrl());
		    System.out.println("Price: " + detailedPlace.getPrice());
		    System.out.println("Address: " + detailedPlace.getAddress());
		    System.out.println("Vicinity: " + detailedPlace.getVicinity());
		    System.out.println("Reviews: " + detailedPlace.getReviews().size());
		    System.out.println("Hours:\n " + detailedPlace.getHours());
		    for(AddressComponent ac:detailedPlace.getAddressComponents()) {
		    	System.out.println("AC : " + ac.getTypes()+" = "+ac.getLongName());
		    }
	}
}
