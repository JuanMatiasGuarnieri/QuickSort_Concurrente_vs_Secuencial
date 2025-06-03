package Test;

import Funciones.GeneradorDeArrays;
import Funciones.QuickSort_Concurrente;
import Funciones.QuickSort_Secuencial;
import java.util.concurrent.Future; // Necesario para Future

public class TestAleatorio {
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
        

        System.out.println("Cantidad de Elementos a Ordenar: " + cantidadElementos);
        System.out.println("Umbral de Paralelizaci�n (minPartitionSize): " + minPartitionSize);
        System.out.println("N�mero de Procesadores Disponibles: " + Runtime.getRuntime().availableProcessors());

        // --- Prueba de QuickSort Concurrente ---
        System.out.println("\n--- Iniciando QuickSort Concurrente ---");

        int[] arrConcurrente = GeneradorDeArrays.generarArrayAleatorio(cantidadElementos, 0, 50000);

        QuickSort_Concurrente tareaInicialConcurrente = new QuickSort_Concurrente(minPartitionSize, arrConcurrente, 0, arrConcurrente.length - 1);

        long tiempoInicioConcurrente = System.currentTimeMillis();

        //Usa submit() y luego get() para esperar
        Future<?> future = QuickSort_Concurrente.forkJoinPool.submit(tareaInicialConcurrente);

        try {
            future.get(); // Espera a que la tarea principal termine
        } catch (Exception ex) {
            System.err.println("Error durante la ejecuci�n concurrente: " + ex.getMessage());
            ex.printStackTrace();
            Thread.currentThread().interrupt(); // Restablece el estado de interrupci�n
        }


        long tiempoFinConcurrente = System.currentTimeMillis();
        long duracionConcurrente = (tiempoFinConcurrente - tiempoInicioConcurrente);

        System.out.println("QuickSort Concurrente Terminado. Tiempo transcurrido: " + duracionConcurrente + " ms");

        // Prueba de QuickSort Secuencial --- y apagado del pool...

        System.out.println("\n--- Iniciando QuickSort Secuencial ---");

        int[] arrSecuencial = GeneradorDeArrays.generarArrayAleatorio(cantidadElementos, 0, 50000);
        
        long tiempoInicioSecuencial = System.currentTimeMillis();
        QuickSort_Secuencial.quickSort(arrSecuencial, 0, arrSecuencial.length - 1);
        long tiempoFinSecuencial = System.currentTimeMillis();
        long duracionSecuencial = (tiempoFinSecuencial - tiempoInicioSecuencial);

        System.out.println("QuickSort Secuencial Terminado. Tiempo transcurrido: " + duracionSecuencial + " ms");


        QuickSort_Concurrente.shutdownPool();
        System.out.println("\nForkJoinPool apagado.");
    }
}
