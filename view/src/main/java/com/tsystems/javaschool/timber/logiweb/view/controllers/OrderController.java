package com.tsystems.javaschool.timber.logiweb.view.controllers;

import com.tsystems.javaschool.timber.logiweb.service.exceptions.OrderNotCreated;
import com.tsystems.javaschool.timber.logiweb.service.util.Services;
import com.tsystems.javaschool.timber.logiweb.view.exceptions.CargoNameException;
import com.tsystems.javaschool.timber.logiweb.view.exceptions.CargoWeightOutOfRangeException;
import com.tsystems.javaschool.timber.logiweb.view.exceptions.IntegerOutOfRangeException;
import com.tsystems.javaschool.timber.logiweb.persistence.dao.jpa.CityDaoJpa;
import com.tsystems.javaschool.timber.logiweb.persistence.dao.jpa.DriverDaoJpa;
import com.tsystems.javaschool.timber.logiweb.persistence.dao.jpa.OrderDaoJpa;
import com.tsystems.javaschool.timber.logiweb.persistence.dao.jpa.TruckDaoJpa;
import com.tsystems.javaschool.timber.logiweb.persistence.entity.*;
import com.tsystems.javaschool.timber.logiweb.service.exceptions.DoubleLoadCargoException;
import com.tsystems.javaschool.timber.logiweb.service.exceptions.NotAllCargosUnloadedException;
import com.tsystems.javaschool.timber.logiweb.service.exceptions.UnloadNotLoadedCargoException;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.CityService;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.DriverService;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.OrderService;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.TruckService;
import com.tsystems.javaschool.timber.logiweb.service.impl.CityServiceImpl;
import com.tsystems.javaschool.timber.logiweb.service.impl.DriverServiceImpl;
import com.tsystems.javaschool.timber.logiweb.service.impl.OrderServiceImpl;
import com.tsystems.javaschool.timber.logiweb.service.impl.TruckServiceImpl;
import com.tsystems.javaschool.timber.logiweb.view.util.InputParser;
import org.apache.log4j.Logger;

import javax.persistence.PersistenceException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * Servlet implementation class Test
 */
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    final static Logger logger = Logger.getLogger(OrderController.class);
    private CityService cityService = Services.getCityService();
    private TruckService truckService = Services.getTruckService();
    private DriverService driverService = Services.getDriverService();
    OrderService orderService = Services.getOrderService();


    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderController() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        List<Order> orders = orderService.findAll();
        RequestDispatcher requestDispatcher;

        if (action != null) {
            switch (action) {
                case "list":
                    request.setAttribute("orders", orders);
                    requestDispatcher = getServletContext().getRequestDispatcher("/jsp/manager/orders/orders.jsp");
                    requestDispatcher.forward(request, response);
                    break;
                case "stateList":
                    request.setAttribute("orders", orders);
                    requestDispatcher = getServletContext().getRequestDispatcher("/jsp/manager/orders/ordersState.jsp");
                    requestDispatcher.forward(request, response);
                    break;
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        int id;

        List<RoutePoint> route = new ArrayList<RoutePoint>();
        Order orderToCreate = new Order();
        List<Truck> suitableTrucks = new ArrayList<Truck>();
        List<Driver> suitableDrivers = new ArrayList<Driver>();
        List<City> cities = cityService.findAll();
        List<Cargo> loadedCargos = new ArrayList<Cargo>();

        if (action != null) {
            switch (action) {
                case "list":
                    break;
                case "stateList": {
                    List<Order> orders = orderService.findAll();
                    request.setAttribute("orders", orders);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/ordersState.jsp");
                    rd.forward(request, response);
                    break;
                }
                case "deleteOrder": {
                    id = parseOrderId(request);
                    try {
                        orderService.delete(id);
                    } catch (PersistenceException ex) {
                        logger.error(ex.toString());
                        request.getSession().setAttribute("errorMessage", ex.getMessage());
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/error.jsp");
                        rd.forward(request, response);
                        return;
                    }
                    break;
                }
                case "add": {
                    clearAttributes(request.getSession(), route, orderToCreate, suitableTrucks, suitableDrivers, cities, loadedCargos);
                    setAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/addOrder.jsp");
                    rd.forward(request, response);
                    break;
                }
                case "addLoadPoint": {
                    try {
                        retrieveAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                                suitableDrivers, cities, loadedCargos);
                        orderToCreate = (Order) request.getSession().getAttribute("order");
                        RoutePoint routePoint = parseRoutePoint(request);
                        if (route.size() > 0)
                            route.get(route.size() - 1).setNextRoutePoint(routePoint);
                        route.add(routePoint);
                        loadedCargos.add(routePoint.getCargo());
                        setAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                                suitableDrivers, cities, loadedCargos);
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/addOrder.jsp");
                        rd.forward(request, response);
                    } catch (CargoNameException ex) {
                        request.setAttribute("cargoNameException", ex);
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/addOrder.jsp");
                        rd.forward(request, response);
                    } catch (NumberFormatException | CargoWeightOutOfRangeException ex) {
                        request.setAttribute("cargoWeightOutOfRangeException", ex);
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/addOrder.jsp");
                        rd.forward(request, response);
                    } catch (Exception ex) {
                        logger.error(ex.toString());
                        request.getSession().setAttribute("errorMessage", ex.toString());
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/error.jsp");
                        rd.forward(request, response);
                        return;
                    }
                    break;
                }
                case "addUnloadPoint": {
                    retrieveAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    orderToCreate = (Order) request.getSession().getAttribute("order");
                    RoutePoint routePoint = parseUnloadRoutePoint(request, loadedCargos);
                    if (route.size() > 0)
                        route.get(route.size() - 1).setNextRoutePoint(routePoint);
                    route.add(routePoint);
                    loadedCargos.remove(routePoint.getCargo());
                    setAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/addOrder.jsp");
                    rd.forward(request, response);
                    break;
                }
                case "getTrucks": {
                    retrieveAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    orderToCreate = (Order) request.getSession().getAttribute("order");
                    orderToCreate.setRoute(route.get(0));
                    request.getSession().setAttribute("isRouteFormed", true);
                    suitableTrucks = truckService.getSuitableTrucksForOrder(orderToCreate);
                    setAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/addOrder.jsp");
                    rd.forward(request, response);
                    break;
                }
                case "assignTruck": {
                    retrieveAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    orderToCreate = (Order) request.getSession().getAttribute("order");
                    int truckId = Integer.valueOf(request.getParameter("truckToAssign"));
                    Truck truckToAssign = truckService.findById(truckId);
                    orderToCreate.setAssignedTruck(truckToAssign);
                    int deliveryTimeThisMonth = orderService.getDeliveryTimeThisMonth(orderToCreate);
                    int deliveryTimeNextMonth = orderService.getDeliveryTimeNextMonth(orderToCreate);
                    suitableDrivers = driverService.getSuitableDriversForOrder(orderToCreate, deliveryTimeThisMonth, deliveryTimeNextMonth);
                    setAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/addOrder.jsp");
                    rd.forward(request, response);
                    break;
                }
                case "assignDriver": {
                    retrieveAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    orderToCreate = (Order) request.getSession().getAttribute("order");
                    String[] params = request.getParameter("driverToAssign").split(" ");
                    int driverId = Integer.valueOf(params[0]);
                    int driverIndex = Integer.valueOf(params[1]);
                    Driver driver = driverService.findById(driverId);
                    if (orderToCreate.getAssignedDrivers() == null)
                        orderToCreate.setAssignedDrivers(new ArrayList<Driver>());
                    orderToCreate.getAssignedDrivers().add(driver);
                    driver.setOrder(orderToCreate);
                    driver.setCurrentTruck(orderToCreate.getAssignedTruck());
                    // it' s better to reget from db cause another client
                    // could have assigned these drivers
                    suitableDrivers.remove(driverIndex);
                    setAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/addOrder.jsp");
                    rd.forward(request, response);
                    break;
                }
                case "create": {
                    retrieveAddOrderSessionAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    orderToCreate = (Order) request.getSession().getAttribute("order");
                    try {
                        orderService.create(orderToCreate);
                    } catch (UnloadNotLoadedCargoException |
                            NotAllCargosUnloadedException | DoubleLoadCargoException ex) {
                        logger.error(ex.toString());
                        request.getSession().setAttribute("errorMessage", ex.toString());
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/error.jsp");
                        rd.forward(request, response);
                        return;
                    } catch (OrderNotCreated ex) {
                        logger.error(ex.toString());
                        request.getSession().setAttribute("errorMessage", ex.getMessage());
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/error.jsp");
                        rd.forward(request, response);
                        return;
                    }
                    clearAttributes(request.getSession(), route, orderToCreate, suitableTrucks,
                            suitableDrivers, cities, loadedCargos);
                    response.sendRedirect(getServletContext().getContextPath() + "/Order?action=list");
                    return;
                }
            }
        }

        List<Order> orders = orderService.findAll();
        request.setAttribute("orders", orders);
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/manager/orders/orders.jsp");
        rd.forward(request, response);
    }

    private void clearAttributes(HttpSession session, List<RoutePoint> route, Order orderToCreate,
                                 List<Truck> suitableTrucks, List<Driver> suitableDrivers,
                                 List<City> cities, List<Cargo> loadedCargos) {
        route.clear();
        session.setAttribute("route", route);
        orderToCreate = new Order();
        session.setAttribute("order", orderToCreate);
        suitableDrivers.clear();
        session.setAttribute("drivers", suitableDrivers);
        suitableTrucks.clear();
        session.setAttribute("trucks", suitableTrucks);
        loadedCargos.clear();
        session.setAttribute("loadedCargos", loadedCargos);
        session.setAttribute("isRouteFormed", false);
    }

    private void setAddOrderSessionAttributes(HttpSession session, List<RoutePoint> route,
                                              Order orderToCreate, List<Truck> suitableTrucks,
                                              List<Driver> suitableDrivers, List<City> cities, List<Cargo> loadedCargos) {
        session.setAttribute("route", route);
        session.setAttribute("cities", cities);
        Boolean isValidRoute = isValidRoute(route);
        session.setAttribute("isValidRoute", isValidRoute.toString());
        session.setAttribute("order", orderToCreate);
        session.setAttribute("loadedCargos", loadedCargos);
        session.setAttribute("trucks", suitableTrucks);
        session.setAttribute("drivers", suitableDrivers);
    }

    private void retrieveAddOrderSessionAttributes(HttpSession session, List<RoutePoint> route,
                                                   Order orderToCreate, List<Truck> suitableTrucks,
                                                   List<Driver> suitableDrivers, List<City> cities, List<Cargo> loadedCargos) {
        route.addAll((List<RoutePoint>) session.getAttribute("route"));
        loadedCargos.addAll((List<Cargo>) session.getAttribute("loadedCargos"));
        suitableTrucks.addAll((List<Truck>) session.getAttribute("trucks"));
        suitableDrivers.addAll((List<Driver>) session.getAttribute("drivers"));
    }

    private RoutePoint parseRoutePoint(HttpServletRequest request) throws CargoNameException, CargoWeightOutOfRangeException {
        String name = "";
        int weight = 0;
        try {
            name = InputParser.parseLettersOnlyString(request.getParameter("cargoName"));
            weight = InputParser.parseNumber(request.getParameter("cargoWeight"), 1, 40000);
        } catch (PatternSyntaxException ex) {
            throw new CargoNameException();
        } catch (IntegerOutOfRangeException ex) {
            throw new CargoWeightOutOfRangeException();
        }
        Cargo cargo = new Cargo(name, weight);
        int cityId = Integer.valueOf(request.getParameter("pointCity"));
        RoutePointType pointType = RoutePointType.valueOf(request.getParameter("pointType"));
        City city = cityService.findById(cityId);
        RoutePoint routePoint = new RoutePoint(city, cargo, pointType);
        return routePoint;
    }

    private RoutePoint parseUnloadRoutePoint(HttpServletRequest request, List<Cargo> loadedCargos) {
        String[] params = request.getParameter("cargoToUnload").split(" ");
        int cargoId = Integer.valueOf(params[0]);
        int cargoIndex = Integer.valueOf(params[1]);
        Cargo cargoToUnload = loadedCargos.get(cargoIndex);
        int cityId = Integer.valueOf(request.getParameter("unloadPointCityId"));
        City city = cityService.findById(cityId);
        RoutePoint routePoint = new RoutePoint(city, cargoToUnload, RoutePointType.UNLOAD);
        return routePoint;
    }

    private int parseOrderId(HttpServletRequest request) {
        int id = Integer.valueOf(request.getParameter("id"));
        return id;
    }

    /**
     * Checks that number of loaded cargos is equal to number of unloaded.
     *
     * @param route contains of RoutePoints to check
     * @return true - if all loaded cargos are unloaded, false otherwise
     */
    boolean isValidRoute(List<RoutePoint> route) {
        int numOfRoutePoints = route.size();
        if (numOfRoutePoints > 0 && numOfRoutePoints % 2 == 0) {
            int numOfLoadPoints = 0;
            for (RoutePoint routePoint : route) {
                if (routePoint.getType() == RoutePointType.LOAD)
                    numOfLoadPoints++;
            }
            int numOfUnloadPoints = numOfRoutePoints - numOfLoadPoints;
            if (numOfLoadPoints == numOfUnloadPoints)
                return true;
        }
        return false;
    }
}