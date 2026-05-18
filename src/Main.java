import controller.Controlador;
import view.VentanaInicio;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaInicio ventanaInicio = new VentanaInicio();
            new Controlador(ventanaInicio);
            ventanaInicio.setVisible(true);
        });
    }
}
