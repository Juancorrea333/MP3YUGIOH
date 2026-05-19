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