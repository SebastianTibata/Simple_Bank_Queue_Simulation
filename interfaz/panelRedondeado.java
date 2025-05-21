/*
    Integrantes:
    1031651866 JUAN SEBASTIAN TIBATÁ PIRALIGUA
    1027210985 VALENTINA MONTENEGRO QUEVEDO
*/
package interfaz;
//Para hacer el redondeado de los cuadros
import javax.swing.*;
import java.awt.*;

//Hereda de JPanel
public class panelRedondeado extends JPanel {
    private int arc;//Valor de redondez de las esquinas

    public panelRedondeado(int arc) {
        this.arc = arc;
        setOpaque(false);//No pinta el borde cuadrado de JPanel
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        g2.dispose();
        super.paintComponent(g);
    }
}
