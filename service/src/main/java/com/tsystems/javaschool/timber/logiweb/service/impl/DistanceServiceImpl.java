package com.tsystems.javaschool.timber.logiweb.service.impl;

import com.tsystems.javaschool.timber.logiweb.persistence.dao.interfaces.GenericDao;
import com.tsystems.javaschool.timber.logiweb.persistence.entity.Distance;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.DistanceService;

import java.util.List;

/**
 * Created by tims on 2/22/2016.
 */
public class DistanceServiceImpl implements DistanceService {
    private static GenericDao distanceDao;

    public DistanceServiceImpl(GenericDao distanceDao) {
        this.distanceDao = distanceDao;
    }

    @Override
    public List<Distance> findAll() {
        return distanceDao.findAll();
    }
}
