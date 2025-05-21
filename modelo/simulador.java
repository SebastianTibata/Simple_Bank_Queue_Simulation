/*
    Integrantes:
    1031651866 JUAN SEBASTIAN TIBATÁ PIRALIGUA
    1027210985 VALENTINA MONTENEGRO QUEVEDO
*/
package modelo;

//Librerias
import java.util.Random;
import java.util.function.Consumer;//Funcion que recibe un parametro tipo T y no retorna nada
import javax.swing.Timer; // Permite ejecutar una accion repetitivamente en un tiempo fijo
import javax.swing.*;

public class simulador {
    //Creacion de las 3 colas
    private queue<String> preferencial;
    private queue<String> intermedio;
    private queue<String> regular;

    //Random para simular las llegadas y atendidas
    private Random random;

    //Probabilidades de llegada y atencion establecidas
    private double probLlegadaPreferencial = 0.1;
    private double probLlegadaIntermedio = 0.3;
    private double probLlegadaRegular = 0.6;

    private double probAtencionPreferencial = 0.5;
    private double probAtencionIntermedio = 0.4;
    private double probAtencionRegular = 0.3;

    //Contadores para estadisticas
    private int contadorClientes = 1;
    private int contadorPreferencial = 1;
    private int contadorIntermedio = 1;
    private int contadorRegular = 1;

    private int clientesAtendidos = 1;
    private int preferencialAtendidos = 1;
    private int intermedioAtendidos = 1;
    private int regularAtendidos = 1;

    //Función que recibe un String y no retorna nada
    private Consumer<String> notificadorClienteAtendido;

    //Constructor para inicializar atributos del objeto con las colas anteriormente creadas
    public simulador(queue<String> p, queue<String> i, queue<String> r) {
        this.preferencial = p;
        this.intermedio = i;
        this.regular = r;
        this.random = new Random();
    }

    //Recibe como parametro el String y guarda el atributo
    public void setNotificadorClienteAtendido(Consumer<String> notificador) {
        this.notificadorClienteAtendido = notificador;
    }
    //Genera las llegadas y Atenciones generando numeros entre 0 y 1
    public void pasoSimulacion() {
        // Llegadas
        if (random.nextDouble() < probLlegadaPreferencial) {
            preferencial.encolar("P-" + contadorPreferencial++);
            contadorClientes++;
        }
        if (random.nextDouble() < probLlegadaIntermedio) {
            intermedio.encolar("I-" + contadorIntermedio++);
            contadorClientes++;
        }
        if (random.nextDouble() < probLlegadaRegular) {
            regular.encolar("R-" + contadorRegular++);
            contadorClientes++;
        }

        // Atenciones comprobando que el notificador no sea null para que el Consumer<String> no bloquee la ejecucion
        if (random.nextDouble() < probAtencionPreferencial && !preferencial.estaVacia()) {
            String cliente = preferencial.desencolar();
            if (notificadorClienteAtendido != null) {
                notificadorClienteAtendido.accept("Cliente " + cliente + " atendido");//Envia la linea de texto al GUI
                preferencialAtendidos++;
            }
        }
        if (random.nextDouble() < probAtencionIntermedio && !intermedio.estaVacia()) {
            String cliente = intermedio.desencolar();
            if (notificadorClienteAtendido != null) {
                notificadorClienteAtendido.accept("Cliente " + cliente + " atendido");
                intermedioAtendidos++;
            }
        }
        if (random.nextDouble() < probAtencionRegular && !regular.estaVacia()) {
            String cliente = regular.desencolar();
            if (notificadorClienteAtendido != null) {
                notificadorClienteAtendido.accept("Cliente " + cliente + " atendido");
                regularAtendidos++;
            }
        }
    }

    //Funcion que retorna las estadisticas de la ejecución
    public String resumenEstadisticas() {
        clientesAtendidos = preferencialAtendidos+intermedioAtendidos+regularAtendidos;
        return
                "Estadisticas finales\n"+
                "Clientes totales: "+contadorClientes+"\n"+
                "Clientes atendidos: "+clientesAtendidos+"\n"+
                "Cola preferencial: \n"+
                "- Clientes que llegaron: "+ contadorPreferencial+"\n"+
                "- Clientes atendidos: "+ preferencialAtendidos+"\n"+
                "- Clientes sin atender: "+ (contadorPreferencial-preferencialAtendidos)+"\n"+
                "Cola Intermedio: \n"+
                "- Clientes que llegaron: "+ contadorIntermedio+"\n"+
                "- Clientes atendidos: "+ intermedioAtendidos+"\n"+
                "- Clientes sin atender: "+ (contadorIntermedio-intermedioAtendidos)+"\n"+
                "Cola Regular: \n"+
                "- Clientes que llegaron: "+ contadorRegular+"\n"+
                "- Clientes atendidos: "+ regularAtendidos+"\n"+
                "- Clientes sin atender: "+ (contadorRegular-regularAtendidos);
    }

    //Ejecuta la simulacion con Timer que obtiene el valor de horas insertado por el usuario creando un bucle y permitiendo actualizar la interfaz constantemente
    public void ejecutarSimulacion(int horas, Runnable callback, java.util.function.Consumer<Timer> onTimerCreated)
    {
        int segundosTotales = horas * 3600;
        Timer timer = new Timer(1000, null);  // 1 segundo (Se puede cambiar para velocidad en llegada y atencion de clientes)
        onTimerCreated.accept(timer);

        final int[] tiempoActual = {0};

        timer.addActionListener(ev -> {
            if (tiempoActual[0] >= segundosTotales) {
                ((Timer) ev.getSource()).stop();
                JOptionPane.showMessageDialog(null, "Simulación finalizada.");
                JOptionPane.showMessageDialog(null,resumenEstadisticas());
            } else {
                pasoSimulacion();
                callback.run();
                tiempoActual[0]++;
            }
        });
        timer.start();
    }
}