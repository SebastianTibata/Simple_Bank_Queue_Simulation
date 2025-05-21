/*
    Integrantes:
    1031651866 JUAN SEBASTIAN TIBATÁ PIRALIGUA
    1027210985 VALENTINA MONTENEGRO QUEVEDO
*/
import interfaz.simulacionGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            simulacionGUI gui = new simulacionGUI();
            gui.mostrar();
        });
    }
}