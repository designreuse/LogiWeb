package com.tsystems.javaschool.timber.logiweb.persistence.dao.jpa;

import com.tsystems.javaschool.timber.logiweb.persistence.dao.interfaces.DistanceDao;
import com.tsystems.javaschool.timber.logiweb.persistence.entity.Distance;
import org.springframework.stereotype.Repository;

/**
 * Created by tims on 2/18/2016.
 */
@Repository
public class DistanceDaoJpa extends GenericDaoJpa<Distance> implements DistanceDao {
}
