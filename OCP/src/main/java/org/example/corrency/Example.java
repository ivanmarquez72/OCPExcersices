package org.example.corrency;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class Example {
}

class SumaTarea extends RecursiveTask<Long> {
    private final int[] array;
    private final int inicio;
    private final int fin;
    private static final int UMBRAL = 1000; // Umbral para dividir la tarea

    public SumaTarea(int[] array, int inicio, int fin) {
        this.array = array;
        this.inicio = inicio;
        this.fin = fin;
    }

    @Override
    protected Long compute() {
        // Si el tamaño de la tarea es pequeño, calcular directamente
        if ((fin - inicio) <= UMBRAL) {
            long suma = 0;
            for (int i = inicio; i < fin; i++) {
                suma += array[i];
            }
            return suma;
        } else {
            // Dividir la tarea en dos subtareas más pequeñas
            int mitad = (inicio + fin) / 2;
            SumaTarea tareaIzquierda = new SumaTarea(array, inicio, mitad);
            SumaTarea tareaDerecha = new SumaTarea(array, mitad, fin);

            // Ejecutar las subtareas en paralelo
            tareaIzquierda.fork(); // Bifurca la tarea izquierda
            long resultadoDerecha = tareaDerecha.compute(); // Computa la tarea derecha
            long resultadoIzquierda = tareaIzquierda.join(); // Espera el resultado de la tarea izquierda

            // Combinar los resultados
            return resultadoIzquierda + resultadoDerecha;
        }
    }
}

class EjemploForkJoin {
    public static void main(String[] args) {
        // Crear un arreglo grande de enteros
        int[] array = new int[10000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1; // Rellenar con valores 1, 2, ..., 10000
        }

        // Crear un ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();

        // Crear la tarea principal
        SumaTarea tareaPrincipal = new SumaTarea(array, 0, array.length);

        // Ejecutar la tarea principal
        long resultado = pool.invoke(tareaPrincipal);

        // Imprimir el resultado
        System.out.println("Suma total: " + resultado);
    }
}
