package com.tsystems.javaschool.timber.logiweb.view.controllers;

import com.tsystems.javaschool.timber.logiweb.persistence.dao.jpa.CargoDaoJpa;
import com.tsystems.javaschool.timber.logiweb.persistence.entity.Cargo;
import com.tsystems.javaschool.timber.logiweb.service.interfaces.CargoService;
import com.tsystems.javaschool.timber.logiweb.service.impl.CargoServiceImpl;
import com.tsystems.javaschool.timber.logiweb.service.util.Services;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class Test
 */
public class CargoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CargoController() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        List<Cargo> cargos = Services.getCargoService().findAll();
        RequestDispatcher requestDispatcher;

        if (action != null) {
            switch (action) {
                case "list":
                    request.setAttribute("cargos", cargos);
                    requestDispatcher = getServletContext().getRequestDispatcher("/jsp/manager/cargos/cargos.jsp");
                    requestDispatcher.forward(request, response);
                    break;
                case "stateList":
                    request.setAttribute("cargos", cargos);
                    requestDispatcher = getServletContext().getRequestDispatcher("/jsp/manager/cargos/cargosState.jsp");
                    requestDispatcher.forward(request, response);
                    break;
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}