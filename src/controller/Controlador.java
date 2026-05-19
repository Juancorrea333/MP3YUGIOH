package controller;

import model.*;
import view.VentanaDuelo;
import view.VentanaFin;
import view.VentanaInicio;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class Controlador {

    private VentanaInicio ventanaInicio;
    private VentanaDuelo  ventanaDuelo;

    private MotorJuego motor;

    public Controlador(VentanaInicio ventanaInicio) {
        this.ventanaInicio = ventanaInicio;

        this.ventanaInicio.setControlador(this);
    }