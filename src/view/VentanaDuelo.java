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
       modeloMano.clear();
        for (Carta c : activo.getMano()) {
            if (c.esMonstruo()) {
                Monstruo m = c.comoMonstruo();
                modeloMano.addElement("[MONSTRUO] " + m.getNombre()
                        + " ATK:" + m.getAtk()
                        + " DEF:" + m.getDef()
                        + " LVL:" + m.getNivel());
            } else if (c instanceof Magica) {
                modeloMano.addElement("[MAGICA] " + c.getNombre()
                        + " - " + ((Magica) c).getDescripcion());
            } else if (c instanceof Trampa) {
                modeloMano.addElement("[TRAMPA] " + c.getNombre()
                        + " - " + ((Trampa) c).getDescripcion());
            }
        }

        txtLog.setText("");
        for (String msg : motor.getLog()) {
            txtLog.append(msg + "\n");
        }
        txtLog.setCaretPosition(txtLog.getDocument().getLength());

        boolean juegoActivo = !motor.isJuegoTerminado();
        btnJugarCarta.setEnabled(juegoActivo && !motor.yaJugoUnaCarta());
        btnAtacar.setEnabled(juegoActivo && !motor.esPrimerTurno() && !motor.yaAtaco());
        btnPasarTurno.setEnabled(juegoActivo);
    }

    public int getIndiceCartaSeleccionada() {
        return lstMano.getSelectedIndex();
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje,
                "Error", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarAviso(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje,
                "Accion no permitida", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarCambioDeTurno(int numeroTurno, String nombreJugador) {
        JOptionPane.showMessageDialog(this,
                "Turno " + numeroTurno + " — Le toca a: " + nombreJugador,
                "Cambio de turno", JOptionPane.INFORMATION_MESSAGE);
    }

    private void initComponentes() {
        setLayout(new BorderLayout(5, 5));
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(),  BorderLayout.CENTER);
        add(crearPanelMano(),     BorderLayout.SOUTH);
    }
        private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        lblTurno         = new JLabel("Turno: 1",        SwingConstants.LEFT);
        lblJugadorActivo = new JLabel("Turno de: ...",   SwingConstants.CENTER);
        lblLpRival       = new JLabel("LP Rival: 8000",  SwingConstants.RIGHT);
        lblMazoRival     = new JLabel("Mazo Rival: 20",  SwingConstants.RIGHT);
        lblLpPropio      = new JLabel("Tus LP: 8000",    SwingConstants.LEFT);
        lblMazoPropio    = new JLabel("Tu mazo: 20",     SwingConstants.LEFT);

        lblJugadorActivo.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(lblTurno);
        panel.add(lblJugadorActivo);
        panel.add(lblLpRival);
        panel.add(lblLpPropio);
        panel.add(lblMazoPropio);
        panel.add(lblMazoRival);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel panelCampos = new JPanel(new GridLayout(2, 1, 5, 5));

        lstCampoRival  = new JList<>(modeloCampoRival);
        lstCampoPropio = new JList<>(modeloCampoPropio);

        JScrollPane scrollRival  = new JScrollPane(lstCampoRival);
        JScrollPane scrollPropio = new JScrollPane(lstCampoPropio);

        scrollRival.setBorder(BorderFactory.createTitledBorder("Campo Rival"));
        scrollPropio.setBorder(BorderFactory.createTitledBorder("Tu Campo"));

        panelCampos.add(scrollRival);
        panelCampos.add(scrollPropio);

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log del Duelo"));

        panel.add(panelCampos);
        panel.add(scrollLog);

        return panel;
    }