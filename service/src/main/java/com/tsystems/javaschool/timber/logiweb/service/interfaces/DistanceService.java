package com.tsystems.javaschool.timber.logiweb.service.interfaces;

import com.tsystems.javaschool.timber.logiweb.persistence.entity.Distance;

import java.util.List;

/**
 * Business logic related to Distance entity.
 *
 * @author Timur Salakhetdinov
 */
public interface DistanceService {
    /**
     * Get set of all distances in database.
     * @return List of all distance between cities in database.
     */
    List<Distance> findAll();
}
