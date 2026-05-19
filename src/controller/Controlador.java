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

    public void accionJugarCarta() {
        int indiceCarta = ventanaDuelo.getIndiceCartaSeleccionada();

        if (indiceCarta < 0) {
            ventanaDuelo.mostrarAviso("Selecciona una carta de tu mano.");
            return;
        }
        if (motor.yaJugoUnaCarta()) {
            ventanaDuelo.mostrarAviso("Ya jugaste una carta este turno.");
            return;
        }

        Carta carta = motor.getActivo().getMano().get(indiceCarta);

        if (carta.esMonstruo()) {
            accionJugarMonstruo(indiceCarta, carta.comoMonstruo());
        } else {

            String error = motor.jugarCarta(indiceCarta, Posicion.ATAQUE, -1);
            if (error != null) {
                ventanaDuelo.mostrarError(error);
            }
        }

        ventanaDuelo.actualizarVista(motor);
        verificarFin();
    }
       private void accionJugarMonstruo(int indiceCarta, Monstruo monstruo) {
        int indiceSacrificio  = -1;
        int indiceSacrificio2 = -1;

        if (monstruo.getNivel() > 4) {
            int sacrificiosRequeridos = monstruo.getNivel() >= 7 ? 2 : 1;
            List<Monstruo> campo = motor.getActivo().getCampo();

            if (campo.size() < sacrificiosRequeridos) {
                ventanaDuelo.mostrarAviso("Necesitas " + sacrificiosRequeridos
                        + " monstruo(s) en campo para sacrificar.");
                return;
            }

            String[] opciones = new String[campo.size()];
            for (int i = 0; i < campo.size(); i++) {
                opciones[i] = i + ": " + campo.get(i).getNombre()
                        + " ATK:" + campo.get(i).getAtk();
            }
            String eleccion = (String) JOptionPane.showInputDialog(ventanaDuelo,
                    "Elige el monstruo a sacrificar (1/" + sacrificiosRequeridos + "):",
                    "Sacrificio", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            if (eleccion == null) return;
            indiceSacrificio = Integer.parseInt(eleccion.split(":")[0]);

            if (sacrificiosRequeridos == 2) {
                String[] opciones2 = new String[campo.size() - 1];
                int idx = 0;
                for (int i = 0; i < campo.size(); i++) {
                    if (i != indiceSacrificio) {
                        opciones2[idx++] = i + ": " + campo.get(i).getNombre()
                                + " ATK:" + campo.get(i).getAtk();
                    }
                }
                String eleccion2 = (String) JOptionPane.showInputDialog(ventanaDuelo,
                        "Elige el segundo monstruo a sacrificar (2/2):",
                        "Segundo Sacrificio", JOptionPane.PLAIN_MESSAGE, null, opciones2, opciones2[0]);
                if (eleccion2 == null) return;
                int idxOriginal = Integer.parseInt(eleccion2.split(":")[0]);
                indiceSacrificio2 = idxOriginal > indiceSacrificio ? idxOriginal - 1 : idxOriginal;
            }
        }

        String[] posOpciones = {"Ataque", "Defensa"};
        int posEleccion = JOptionPane.showOptionDialog(ventanaDuelo,
                "¿En que posicion invocar a " + monstruo.getNombre() + "?",
                "Posicion de invocacion",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, posOpciones, posOpciones[0]);
        if (posEleccion < 0) return;

        Posicion posicion = posEleccion == 0 ? Posicion.ATAQUE : Posicion.DEFENSA;
        String error = motor.jugarCarta(indiceCarta, posicion, indiceSacrificio, indiceSacrificio2);
        if (error != null) {
            ventanaDuelo.mostrarError(error);
        }
    }