package Test;

import Funciones.GeneradorDeArrays; // Para generar arrays.
import Funciones.QuickSort_Concurrente; // Para el QuickSort concurrente.
import Funciones.QuickSort_Secuencial; // Para el QuickSort secuencial.
import java.util.concurrent.Future; // Necesario para Future.

public class TestDescendente {
    public static void main(String[] args) {

    	// Configuraci�n de Tama�o del Array y tama�o minimo de la particion
    	//int cantidadElementos = 10;
    	//int cantidadElementos = 1000;
    	//int cantidadElementos = 100000;
    	//int cantidadElementos = 1000000;
    	//int cantidadElementos = 10000000;
        int cantidadElementos = 50000000;
        // Define el umbral de partici�n para decidir si se paraleliza o se ordena secuencialmente.
        int minPartitionSize = 5000;

        System.out.println("--- Prueba de Rendimiento con Arrays Descendentes ---");
        System.out.println("Cantidad de Elementos a Ordenar: " + cantidadElementos);
        System.out.println("Umbral de Paralelizaci�n (minPartitionSize): " + minPartitionSize);
        System.out.println("N�mero de Procesadores Disponibles: " + Runtime.getRuntime().availableProcessors());

        // --- Prueba de QuickSort Concurrente ---
        System.out.println("\n--- Iniciando QuickSort Concurrente ---");

        // Genera un array con n�meros ordenados de forma descendente para la prueba concurrente.
        int[] arrConcurrente = GeneradorDeArrays.generarArrayDescendente(cantidadElementos);

        // Crea la tarea inicial para el QuickSort concurrente.
        QuickSort_Concurrente tareaInicialConcurrente = new QuickSort_Concurrente(minPartitionSize, arrConcurrente, 0, arrConcurrente.length - 1);

        // Inicia el contador de tiempo para la ejecuci�n concurrente.
        long tiempoInicioConcurrente = System.currentTimeMillis();

        // Env�a la tarea al ForkJoinPool y obtiene un Future para esperar su finalizaci�n.
        Future<?> future = QuickSort_Concurrente.forkJoinPool.submit(tareaInicialConcurrente);

        try {
            future.get(); // Espera a que la tarea concurrente termine.
        } catch (Exception ex) {
            System.err.println("�ERROR durante la ejecuci�n concurrente! Detalles:");
            ex.printStackTrace(); // Imprime el rastro completo del error.
            if (ex.getCause() != null) {
                System.err.println("Causa ra�z: " + ex.getCause().getMessage());
                ex.getCause().printStackTrace();
            }
            Thread.currentThread().interrupt(); // Restablece el estado de interrupci�n del hilo.
        }

        // Calcula y muestra el tiempo transcurrido para el QuickSort concurrente.
        long tiempoFinConcurrente = System.currentTimeMillis();
        long duracionConcurrente = (tiempoFinConcurrente - tiempoInicioConcurrente);
        System.out.println("QuickSort Concurrente Terminado. Tiempo transcurrido: " + duracionConcurrente + " ms");

        // --- Prueba de QuickSort Secuencial ---
        System.out.println("\n--- Iniciando QuickSort Secuencial ---");

        // Genera un array con n�meros ordenados de forma descendente para la prueba secuencial.
        int[] arrSecuencial = GeneradorDeArrays.generarArrayDescendente(cantidadElementos);

        // Inicia el contador de tiempo para la ejecuci�n secuencial.
        long tiempoInicioSecuencial = System.currentTimeMillis();
        // Llama al m�todo de ordenamiento QuickSort secuencial.
        QuickSort_Secuencial.quickSort(arrSecuencial, 0, arrSecuencial.length - 1);
        // Calcula y muestra el tiempo transcurrido para el QuickSort secuencial.
        long tiempoFinSecuencial = System.currentTimeMillis();
        long duracionSecuencial = (tiempoFinSecuencial - tiempoInicioSecuencial);

        System.out.println("QuickSort Secuencial Terminado. Tiempo transcurrido: " + duracionSecuencial + " ms");

        // --- Apagado del Pool de Hilos ---
        // Asegura que el ForkJoinPool se apague correctamente para liberar recursos.
        QuickSort_Concurrente.shutdownPool();
        System.out.println("\nForkJoinPool apagado.");
    }
}