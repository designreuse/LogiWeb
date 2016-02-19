package com.tsystems.javaschool.timber.logiweb.service.impl;

import com.tsystems.javaschool.timber.logiweb.dao.GenericDao;
import com.tsystems.javaschool.timber.logiweb.entity.RoutePoint;
import com.tsystems.javaschool.timber.logiweb.service.RoutePointService;

/**
 * Created by tims on 2/18/2016.
 */
public class RoutePointServiceImpl implements RoutePointService {
    private static GenericDao routePointDao;

    public RoutePointServiceImpl(GenericDao routePointDao) {
        this.routePointDao = routePointDao;
    }

    @Override
    public void create(RoutePoint routePoint) {
        routePointDao.persist(routePoint);
    }
}
