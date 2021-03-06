package com.tsystems.javaschool.timber.logiweb.service.test.integration;

import com.tsystems.javaschool.timber.logiweb.persistence.entity.*;
import com.tsystems.javaschool.timber.logiweb.service.exceptions.DoubleLoadCargoException;
import com.tsystems.javaschool.timber.logiweb.service.exceptions.NotAllCargosUnloadedException;
import com.tsystems.javaschool.timber.logiweb.service.exceptions.OrderNotCreated;
import com.tsystems.javaschool.timber.logiweb.service.exceptions.UnloadNotLoadedCargoException;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.CityService;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.DriverService;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.OrderService;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.TruckService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tims on 2/18/2016.
 */
public class OrderServiceDaoTest {
    @Autowired
    private CityService cityService;
    @Autowired
    private TruckService truckService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private OrderService orderService;
    Order order;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void CreateOrder() throws Exception, DoubleLoadCargoException,
            NotAllCargosUnloadedException, UnloadNotLoadedCargoException, OrderNotCreated {

        //mimic addOrder.jsp order creation process
        //1st create route for order
        Cargo cargo = new Cargo("milk", 10);
        int cityId = 1;
        City city = cityService.findById(cityId);
        RoutePoint loadpoint = new RoutePoint(city, cargo, RoutePointType.LOAD);
        RoutePoint unloadPoint = new RoutePoint(city, cargo, RoutePointType.UNLOAD);
        loadpoint.setNextRoutePoint(unloadPoint);

        //2nd create order and assign a truck
        order = new Order();
        order.setRoute(loadpoint);
        List<Truck> trucks = truckService.getSuitableTrucksForOrder(order);
        Truck chosenTruck = trucks.get(1);
        order.setAssignedTruck(chosenTruck);

        //3rd assign drivers to form the truck shift
        int deliveryTimeThisMonth = orderService.getDeliveryTimeThisMonth(order);
        int deliveryTimeNextMonth = orderService.getDeliveryTimeNextMonth(order);
        List<Driver> drivers = driverService.getSuitableDriversForOrder(order, deliveryTimeThisMonth, deliveryTimeNextMonth);
        if (order.getAssignedDrivers() == null)
            order.setAssignedDrivers(new ArrayList<Driver>());
        for (int i=0; i<chosenTruck.getShiftSize();i++) {
            Driver driver = drivers.get(i);
            driver.setOrder(order);
            driver.setCurrentTruck(order.getAssignedTruck());
            order.getAssignedDrivers().add(driver);
        }

        int numOfOrders = orderService.findAll().size();
        orderService.create(order);
        Assert.assertEquals(orderService.findAll().size(), numOfOrders+1);
        orderService.delete(order.getId());
    }

    @Test
    public void DeleteOrder() throws DoubleLoadCargoException,
            NotAllCargosUnloadedException, UnloadNotLoadedCargoException, OrderNotCreated {

        //mimic addOrder.jsp order creation process
        //1st create route for order
        Cargo cargo = new Cargo("milk", 10);
        int cityId = 1;
        City city = cityService.findById(cityId);
        RoutePoint loadpoint = new RoutePoint(city, cargo, RoutePointType.LOAD);
        RoutePoint unloadPoint = new RoutePoint(city, cargo, RoutePointType.UNLOAD);
        loadpoint.setNextRoutePoint(unloadPoint);

        //2nd create order and assign a truck
        order = new Order();
        order.setRoute(loadpoint);
        List<Truck> trucks = truckService.getSuitableTrucksForOrder(order);
        Truck chosenTruck = trucks.get(1);
        order.setAssignedTruck(chosenTruck);

        //3rd assign drivers to form the truck shift
        int deliveryTimeThisMonth = orderService.getDeliveryTimeThisMonth(order);
        int deliveryTimeNextMonth = orderService.getDeliveryTimeNextMonth(order);
        List<Driver> drivers = driverService.getSuitableDriversForOrder(order, deliveryTimeThisMonth, deliveryTimeNextMonth);
        if (order.getAssignedDrivers() == null)
            order.setAssignedDrivers(new ArrayList<Driver>());
        for (int i=0; i<chosenTruck.getShiftSize();i++) {
            Driver driver = drivers.get(i);
            //driver.setOrder(order);
            //driver.setCurrentTruck(order.getAssignedTruck());
            order.getAssignedDrivers().add(driver);
        }


        orderService.create(order);
        int numOfOrders = orderService.findAll().size();
        orderService.delete(order.getId());
        Assert.assertEquals(orderService.findAll().size(), numOfOrders-1);
    }

    @Test
    public void CalculateOrderDeliveryTime() {
        Order order = orderService.findById(2);
        int deliveryTime = orderService.getDeliveryTime(order);
        Assert.assertEquals(54, deliveryTime);

        order = orderService.findById(1);
        deliveryTime = orderService.getDeliveryTime(order);
        Assert.assertEquals(157, deliveryTime);
    }

    @Test
    public void CalculateOrderDeliveryTimeThisMonth() {
        //TODO it's date dependent test
        Order order = orderService.findById(2);
        int deliveryTimeThisMonth = orderService.getDeliveryTimeThisMonth(order);
        Assert.assertEquals(54, deliveryTimeThisMonth);

        order = orderService.findById(1);
        deliveryTimeThisMonth = orderService.getDeliveryTimeThisMonth(order);
        Assert.assertEquals(157, deliveryTimeThisMonth);
    }

    @Test
    public void CalculateOrderDeliveryTimeNextMonth() {
        //TODO it's date dependent test
        Order order = orderService.findById(2);
        int deliveryTimeNextMonth = orderService.getDeliveryTimeNextMonth(order);
        Assert.assertEquals(0, deliveryTimeNextMonth);

        order = orderService.findById(1);
        deliveryTimeNextMonth = orderService.getDeliveryTimeNextMonth(order);
        Assert.assertEquals(0, deliveryTimeNextMonth);
    }
}