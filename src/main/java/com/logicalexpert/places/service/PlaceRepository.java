package com.logicalexpert.places.service;


import java.util.Collection;
import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;

import com.logicalexpert.places.domain.GPlace;


public interface PlaceRepository extends CrudRepository<GPlace, String> {

	List<GPlace> findByCategory(String category);

//    List<GPlace> findByPositionNear(Point p, Distance d);
}