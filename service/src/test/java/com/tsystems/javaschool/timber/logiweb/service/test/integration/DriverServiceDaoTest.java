package com.tsystems.javaschool.timber.logiweb.service.test.integration;

import com.tsystems.javaschool.timber.logiweb.persistence.entity.City;
import com.tsystems.javaschool.timber.logiweb.persistence.entity.Driver;
import com.tsystems.javaschool.timber.logiweb.persistence.entity.DriverState;
import com.tsystems.javaschool.timber.logiweb.persistence.entity.Truck;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.CityService;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.DriverService;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.TruckService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by tims on 2/15/2016.
 */
public class DriverServiceDaoTest {
    @Autowired
    private DriverService driverService;
    @Autowired
    private CityService cityService;
    @Autowired
    private TruckService truckService;

    @Test
    public void CanCreateDriverInDB() {
        Driver driver = new Driver();
        driver.setName("Misha");
        driver.setSurname("Popov");
        driver.setHoursWorkedThisMonth(10);
        driver.setState(DriverState.DRIVE);
        City city = cityService.findById(1);
        driver.setCurrentCity(city);
        Truck truck = truckService.findById(1);
        driver.setCurrentTruck(truck);

        int numOfDriversBefore = driverService.findAll().size();
        driverService.create(driver);
        int numOfDriversAfter = driverService.findAll().size();
        Assert.assertEquals(numOfDriversBefore + 1, numOfDriversAfter);
        driverService.delete(getLastDriverId());
    }

    @Test
    public void CanReadDriversTableFromDB() {
        List<Driver> drivers = driverService.findAll();
        Assert.assertNotNull(drivers);
        Assert.assertTrue(drivers.size() > 0);
    }

    @Test
    public void CanUpdateDriverInDB() {
        createDriver();
        Driver driverBeforeUpdate = driverService.findById(getLastDriverId());
        Driver driverAfterUpdate = driverService.findById(getLastDriverId());
        driverAfterUpdate.setHoursWorkedThisMonth(driverAfterUpdate.getHoursWorkedThisMonth()+1);
        driverService.update(driverAfterUpdate);
        Assert.assertNotEquals(driverAfterUpdate, driverBeforeUpdate);
        driverService.delete(getLastDriverId());
    }

    @Test
    public void CanDeleteDriverInDB() {
        createDriver();
        List<Driver> drivers = driverService.findAll();
        int lenBefore = drivers.size();
        driverService.delete(drivers.get(lenBefore-1).getId());
        int lenAfter = driverService.findAll().size();
        Assert.assertEquals(lenAfter, lenBefore - 1);
    }

    private void createDriver() {
        Driver driver = new Driver();
        driver.setName("Misha");
        driver.setSurname("Popov");
        driver.setHoursWorkedThisMonth(10);
        driver.setState(DriverState.DRIVE);
        City city = cityService.findById(1);
        driver.setCurrentCity(city);
        Truck truck = truckService.findById(1);
        driver.setCurrentTruck(truck);

        driverService.create(driver);
    }

    private int getLastDriverId() {
        List<Driver> drivers = driverService.findAll();
        int len = drivers.size();
        return drivers.get(len-1).getId();
    }
}