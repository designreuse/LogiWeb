package com.tsystems.javaschool.timber.logiweb.service;

import com.tsystems.javaschool.timber.logiweb.entity.Cargo;

import java.util.List;

/**
 * Business logic related to Cargo entity.
 * @author Timur Salakhetdinov
 */
public interface CargoService {
    /**
     * Create Cargo entity in database.
     */
    void create(Cargo cargo);

    /**
     * Find cargos.
     * @return List of all cargos in database.
     */
    List<Cargo> findAll();
}