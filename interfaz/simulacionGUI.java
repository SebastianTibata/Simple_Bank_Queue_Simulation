/*
    Integrantes:
    1031651866 JUAN SEBASTIAN TIBATÁ PIRALIGUA
    1027210985 VALENTINA MONTENEGRO QUEVEDO
*/
package interfaz;

//Importar la cola y la simulacion
import modelo.queue;
import modelo.simulador;

//Librerias para interfaz
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class simulacionGUI {
    //Creació1n de las colas
    private queue<String> preferencial;
    private queue<String> intermedio;
    private queue<String> regular;

    //Objeto de tipo simulador
    private simulador simulador;

    //Componentes de la interfaz grafica
    private JFrame frame;
    private JPanel panelPreferencial, panelIntermedio, panelRegular;
    private JTextArea areaEstadisticas;
    private JTextField campoTiempo;
    private JLabel labelNotificacion;

    //Para mantener la simulacion por el tiempo determinado
    private Timer timerSimulacion;

    //Inicializa las colas y las envia a simulador
    public simulacionGUI() {
        preferencial = new queue<>();
        intermedio = new queue<>();
        regular = new queue<>();

        simulador = new simulador(preferencial, intermedio, regular);
        inicializarGUI();
    }

    //Crea la ventana
    private void inicializarGUI() {
        frame = new JFrame("Simulación de Atención en Banco");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());

        crearComponentes();

        frame.setVisible(true);
    }

    //Crea cada uno de los componentes de la interfaz
    private void crearComponentes() {
        // Panel de título
        JLabel titulo = new JLabel("Simulación de Atención en el Banco", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Panel de colas
        panelPreferencial = crearPanelCola("Cola Preferencial");
        panelIntermedio = crearPanelCola("Cola Intermedio");
        panelRegular = crearPanelCola("Cola Regular");

        JPanel panelColas = new JPanel(new GridLayout(1, 3, 10, 10));
        panelColas.add(panelPreferencial);
        panelColas.add(panelIntermedio);
        panelColas.add(panelRegular);
        panelColas.setBackground(Color.BLACK);


        // Panel de controles
        JPanel panelControles = new JPanel();
        panelControles.setBackground(Color.BLACK);
        campoTiempo = new JTextField(5);

        //Recibe el cliente atendido
        simulador.setNotificadorClienteAtendido(cliente -> labelNotificacion.setText(cliente));

        //Inicia la simulación enviando el tiempo digitado
        JButton btnIniciar = new JButton("Iniciar Simulación");
        btnIniciar.addActionListener(e -> {
            try {
                int horas = Integer.parseInt(campoTiempo.getText());
                simulador.ejecutarSimulacion(horas, this::actualizarVista, timer -> timerSimulacion = timer);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ingrese un número válido de horas.");
            }
        });

        //Finaliza la simulación e imprime estadisticas
        JButton btnTerminar = new JButton("Terminar Simulación");
        btnTerminar.addActionListener(e -> {
            if (timerSimulacion != null) {
                timerSimulacion.stop();
                JOptionPane.showMessageDialog(frame, "Simulación detenida manualmente.");
                JOptionPane.showMessageDialog(null, simulador.resumenEstadisticas());
            } else {
                JOptionPane.showMessageDialog(frame, "No hay una simulación activa");
            }
        });


        JLabel labelTiempo = new JLabel("Tiempo (horas):");
        labelTiempo.setForeground(Color.WHITE);
        panelControles.add(labelTiempo);
        panelControles.add(campoTiempo);
        panelControles.add(btnIniciar);
        panelControles.add(btnTerminar);

        // Panel de estadísticas
        areaEstadisticas = new JTextArea(6, 80);
        areaEstadisticas.setEditable(false);
        areaEstadisticas.setBackground(Color.WHITE);
        areaEstadisticas.setForeground(Color.BLACK);
        areaEstadisticas.setBorder(BorderFactory.createEmptyBorder());

        labelNotificacion = new JLabel(" ");
        labelNotificacion.setForeground(Color.BLACK);
        labelNotificacion.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelNotificacion.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JScrollPane scroll = new JScrollPane(areaEstadisticas);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panelRedondeado panelEstadisticas = new panelRedondeado(30);
        panelEstadisticas.setLayout(new BorderLayout());
        panelEstadisticas.setBackground(Color.WHITE);
        panelEstadisticas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelEstadisticas.add(labelNotificacion, BorderLayout.NORTH);
        panelEstadisticas.add(scroll, BorderLayout.CENTER);


        // Panel central (une colas, controles y estadisticas)
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(Color.BLACK);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;


        gbc.weighty = 0.8;
        panelCentral.add(panelColas, gbc);
        gbc.weighty = 0.05;
        panelCentral.add(panelControles, gbc);
        gbc.weighty = 0.05;
        panelCentral.add(panelEstadisticas, gbc);


        // Agregar todo al frame
        frame.add(titulo, BorderLayout.NORTH);
        frame.add(panelCentral, BorderLayout.CENTER);

        // Estética general
        frame.getContentPane().setBackground(Color.BLACK);
        campoTiempo.setBackground(Color.WHITE);
        campoTiempo.setForeground(Color.BLACK);

        actualizarVista();
    }


    //Para crear cada uno de los paneles de las colas
    private JPanel crearPanelCola(String titulo) {
        panelRedondeado panel = new panelRedondeado(30);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // Crear un borde de título sin línea visible
        TitledBorder bordeTitulo = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), titulo);
        bordeTitulo.setTitleJustification(TitledBorder.CENTER);
        bordeTitulo.setTitleColor(Color.BLACK);

        panel.setBorder(BorderFactory.createCompoundBorder(
                bordeTitulo,
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Margen interno
        ));

        return panel;
    }

    //Función para actualizar vista y poder mostrar las estadisticas en tiempo real
    private void actualizarVista() {
        SwingUtilities.invokeLater(() -> {
            actualizarPanel(panelPreferencial, preferencial);
            actualizarPanel(panelIntermedio, intermedio);
            actualizarPanel(panelRegular, regular);

            areaEstadisticas.setText(
                    "Clientes en cola:\n" +
                            "Preferencial: " + preferencial.tamaño() + "\n" +
                            "Intermedio: " + intermedio.tamaño() + "\n" +
                            "Regular: " + regular.tamaño()
            );
        });
    }

    //Crea las etiquetas de los clientes para mostrarlos
    private void actualizarPanel(JPanel panel, queue<String> cola) {
        panel.removeAll();
        for (String cliente : cola.toString().replace("[", "").replace("]", "").split(", ")) {
            if (!cliente.trim().isEmpty()) {
                JLabel label = new JLabel(cliente);
                panel.add(label);
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
