/*
    Integrantes:
    1031651866 JUAN SEBASTIAN TIBATÁ PIRALIGUA
    1027210985 VALENTINA MONTENEGRO QUEVEDO
*/
package modelo;

import java.util.LinkedList;
import java.util.Queue;

//Clase cola con sus atributos
public class queue <T>{
    private Queue<T> cola;

    public queue() {
        this.cola = new LinkedList<>();
    }
    public boolean estaVacia(){

        return cola.isEmpty();
    }

    public void encolar(T elemento){

        cola.add(elemento);
    }

    public T desencolar(){
        if(estaVacia()) {
            throw new IllegalStateException("La cola esta vacia");
        }
        return cola.remove();
    }

    public int tamaño() {
        return cola.size();
    }

    @Override
    public String toString(){
        return cola.toString();
    }

}

