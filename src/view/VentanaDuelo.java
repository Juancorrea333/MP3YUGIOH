package view;

import controller.Controlador;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }
        public void actualizarVista(MotorJuego motor) {
        Jugador activo   = motor.getActivo();
        Jugador oponente = motor.getOponente();

        lblTurno.setText("Turno: " + motor.getNumeroTurno());
        lblJugadorActivo.setText("Turno de: " + activo.getNombre());
        lblLpPropio.setText("Tus LP: " + activo.getLp());
        lblLpRival.setText("LP Rival (" + oponente.getNombre() + "): " + oponente.getLp());
        lblMazoPropio.setText("Tu mazo: " + activo.getMazo().size() + " cartas");
        lblMazoRival.setText("Mazo rival: " + oponente.getMazo().size() + " cartas");

        modeloCampoRival.clear();
        for (Monstruo m : oponente.getCampo()) {
            modeloCampoRival.addElement(m.getNombre()
                    + " | ATK:" + m.getAtk()
                    + " DEF:" + m.getDef()
                    + " | " + m.getPosicion());
        }
        if (oponente.tieneTrampas()) {
            modeloCampoRival.addElement("[ " + oponente.getTrampas().size() + " trampa(s) boca abajo ]");
        }

        modeloCampoPropio.clear();
        for (Monstruo m : activo.getCampo()) {
            String puedeAtacar = m.puedeAtacar() ? " ⚔" : "";
            modeloCampoPropio.addElement(m.getNombre()
                    + " | ATK:" + m.getAtk()
                    + " DEF:" + m.getDef()
                    + " | " + m.getPosicion() + puedeAtacar);
        }
        if (activo.tieneTrampas()) {
            modeloCampoPropio.addElement("[ " + activo.getTrampas().size() + " trampa(s) boca abajo ]");
        }
