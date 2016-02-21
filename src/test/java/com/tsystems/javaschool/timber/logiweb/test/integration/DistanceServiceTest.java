package com.tsystems.javaschool.timber.logiweb.test.integration;

import com.tsystems.javaschool.timber.logiweb.dao.jpa.CityDaoJpa;
import com.tsystems.javaschool.timber.logiweb.dao.jpa.DistanceDaoJpa;
import com.tsystems.javaschool.timber.logiweb.entity.City;
import com.tsystems.javaschool.timber.logiweb.entity.Distance;
import com.tsystems.javaschool.timber.logiweb.service.CityService;
import com.tsystems.javaschool.timber.logiweb.service.DistanceService;
import com.tsystems.javaschool.timber.logiweb.service.impl.CityServiceImpl;
import com.tsystems.javaschool.timber.logiweb.service.impl.DistanceServiceImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by tims on 2/22/2016.
 */
public class DistanceServiceTest {

    @Test
    public void FindAll() {
        DistanceService distanceService = new DistanceServiceImpl(new DistanceDaoJpa(Distance.class));
        List<Distance> distances = distanceService.findAll();
        CityService cityService = new CityServiceImpl(new CityDaoJpa(City.class));
        List<City> cities = cityService.findAll();
        int numOfCities = cities.size();
        // in full directed graph G(V,E) |E| = |V|*(|V|-1)
        int numOfDistances = numOfCities * (numOfCities - 1);
        assertEquals(numOfDistances, distances.size());
    }
}