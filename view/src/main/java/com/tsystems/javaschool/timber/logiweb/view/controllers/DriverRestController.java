package com.tsystems.javaschool.timber.logiweb.view.controllers;

import com.tsystems.javaschool.timber.logiweb.persistence.entity.Driver;
import com.tsystems.javaschool.timber.logiweb.persistence.entity.DriverState;
import com.tsystems.javaschool.timber.logiweb.service.dto.DriverDto;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.DriverService;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/driver")
public class DriverRestController {

    @Autowired
    DriverService driverService;

    @RequestMapping(value = "/open-shift", method = RequestMethod.POST, produces = "application/json")
    Driver openShift(@RequestBody DriverDto driver) {
        Driver driverFound = driverService.findById(driver.getId());
        Date creationTime = new Date();
        driverFound.setShiftStartTime(creationTime);
        driverFound.setState(driver.getState());
        driverService.update(driverFound);
        return driverFound;
    }

    @RequestMapping(value = "/change-state", method = RequestMethod.POST, produces = "application/json")
    Driver changeState(@RequestBody DriverDto driver) {
        Driver driverFound = driverService.findById(driver.getId());
        driverFound.setState(driver.getState());
        driverService.update(driverFound);
        return driverFound;
    }

    @RequestMapping(value = "/close-shift", method = RequestMethod.POST, produces = "application/json")
    Driver closeShift(@RequestBody DriverDto driver) {
        Driver driverFound = driverService.findById(driver.getId());
        DateTime startTime = new DateTime(driverFound.getShiftStartTime());
        DateTime endTime = new DateTime();
        Period p = new Period(startTime, endTime);
        int hoursWorked = p.toStandardHours().getHours();
        driverFound.addWorkHours(hoursWorked);
        driverFound.setShiftStartTime(null);
        driverFound.setState(DriverState.REST);
        driverService.update(driverFound);
        return driverFound;
    }
}
