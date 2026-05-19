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
        public void accionIniciarDuelo() {
        String nombre1 = ventanaInicio.getNombreJugador1();
        String nombre2 = ventanaInicio.getNombreJugador2();

        if (nombre1.isEmpty() || nombre2.isEmpty()) {
            ventanaInicio.mostrarError("Por favor ingresa los nombres de ambos duelistas.");
            return;
        }
        if (nombre1.equals(nombre2)) {
            ventanaInicio.mostrarError("Los nombres deben ser diferentes.");
            return;
        }

        Jugador j1 = new Jugador(nombre1);
        Jugador j2 = new Jugador(nombre2);

        List<Carta> mazoCompleto = FabricaCartas.crearMazoCompleto();
        Collections.shuffle(mazoCompleto);

        for (int i = 0;  i < 25; i++) j1.agregarAlMazo(mazoCompleto.get(i));
        for (int i = 25; i < 50; i++) j2.agregarAlMazo(mazoCompleto.get(i));

        for (int i = 0; i < 5; i++) {
            j1.robarCarta();
            j2.robarCarta();
        }

        motor = new MotorJuego(j1, j2);
        motor.iniciarTurno();

        ventanaDuelo = new VentanaDuelo();
        ventanaDuelo.setControlador(this);
        ventanaDuelo.actualizarVista(motor);
        ventanaDuelo.setVisible(true);

        ventanaInicio.dispose();
    }