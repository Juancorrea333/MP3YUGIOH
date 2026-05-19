ackage view;

import controller.Controlador;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Vista de la pantalla de duelo.
 * Solo muestra el estado del juego y notifica al Controlador
 * cuando el usuario pulsa un boton.
 * No contiene logica de juego ni llama directamente al MotorJuego.
 */
public class VentanaDuelo extends JFrame {

    private JLabel lblTurno;
    private JLabel lblJugadorActivo;
    private JLabel lblLpPropio;
    private JLabel lblLpRival;
    private JLabel lblMazoPropio;
    private JLabel lblMazoRival;

    private DefaultListModel<String> modeloCampoRival  = new DefaultListModel<>();
    private DefaultListModel<String> modeloCampoPropio = new DefaultListModel<>();
    private DefaultListModel<String> modeloMano        = new DefaultListModel<>();

    private JList<String> lstCampoRival;
    private JList<String> lstCampoPropio;
    private JList<String> lstMano;

    private JTextArea txtLog;

    private JButton btnJugarCarta;
    private JButton btnAtacar;
    private JButton btnPasarTurno;

    private Controlador controlador;

    public VentanaDuelo() {
        setTitle("Yu-Gi-Oh! - Duelo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponentes();
    }

    // El Controlador se registra aqui despues de construir la vista
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }