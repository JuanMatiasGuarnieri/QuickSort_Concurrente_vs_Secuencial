package Funciones;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ForkJoinPool; // Importa ForkJoinPool para una gestión de hilos más eficiente

// Implementa Runnable para permitir ejecución en un hilo.
public class QuickSort_Concurrente implements Runnable {

    // Instancia de ForkJoinPool, creada una vez y usada por todos los hilos.
    // Número de hilos basado en los núcleos de CPU disponibles.
    public static final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    // Array a ordenar y rango (inicio, fin) de la partición actual.
    final int[] my_array;
    final int start, end;

    // Umbral: si la partición es más pequeña, se ordena secuencialmente para evitar sobrecarga de hilos.
    private final int minPartitionSize;

    // Constructor: inicializa el array, rango y tamaño mínimo de partición.
    public QuickSort_Concurrente(int minPartitionSize, int[] array, int start, int end) {
        this.minPartitionSize = minPartitionSize;
        this.my_array = array;
        this.start = start;
        this.end = end;
    }

    // Método que se ejecuta al iniciar el hilo: llama al quicksort principal.
    @Override
    public void run() {
        quicksort(my_array, start, end);
    }

    // El método generarArrayAleatorio ha sido movido a GeneradorDeArrays.

    // Imprime los elementos de un array en la consola.
    public static void mostrarArray(int[] arr) {
        System.out.println("\n-----------------------:\n"); // Imprime un separador.
        for (int num : arr) { // Itera y muestra cada número.
            System.out.print(num + " - ");
        }
    }

    // Implementación del algoritmo QuickSort, con lógica para paralelizar o ejecutar secuencialmente.
    private void quicksort(int[] array, int start, int end) {
        int len = end - start + 1;

        if (len <= 1) { // Caso base: array de 0 o 1 elemento ya está ordenado.
            return;
        }

        // Si la partición es pequeña (debajo del umbral), se ordena secuencialmente.
        if (len <= minPartitionSize) {
            // Lógica de partición y ordenamiento secuencial (sin crear nuevos hilos).
            int pivot_index = medianOfThree(array, start, end);
            int pivotValue = array[pivot_index];
            swap(array, pivot_index, end);

            int storeIndex = start;
            for (int i = start; i < end; i++) {
                if (array[i] <= pivotValue) {
                    swap(array, i, storeIndex);
                    storeIndex++;
                }
            }
            swap(array, storeIndex, end);

            // Llamadas recursivas secuenciales para las sub-particiones.
            quicksort(array, start, storeIndex - 1);
            quicksort(array, storeIndex + 1, end);

        } else { // Si la partición es grande, se paraleliza.
            // Lógica de partición (igual que en el caso secuencial).
            int pivot_index = medianOfThree(array, start, end);
            int pivotValue = array[pivot_index];
            swap(array, pivot_index, end);

            int storeIndex = start;
            for (int i = start; i < end; i++) {
                if (array[i] <= pivotValue) {
                    swap(array, i, storeIndex);
                    storeIndex++;
                }
            }
            swap(array, storeIndex, end);

            // Crea tareas para las sub-particiones izquierda y derecha.
            QuickSort_Concurrente leftQuick = new QuickSort_Concurrente(minPartitionSize, array, start, storeIndex - 1);
            QuickSort_Concurrente rightQuick = new QuickSort_Concurrente(minPartitionSize, array, storeIndex + 1, end);

            // Envía las tareas al ForkJoinPool para ejecución concurrente.
            Future<?> leftFuture = forkJoinPool.submit(leftQuick);
            Future<?> rightFuture = forkJoinPool.submit(rightQuick);

            // Espera a que ambas tareas (hilos) terminen antes de continuar.
            try {
                leftFuture.get();
                rightFuture.get();
            } catch (Exception ex) { // Captura y maneja posibles excepciones de los hilos.
                ex.printStackTrace();
                Thread.currentThread().interrupt(); // Restaura el estado de interrupción.
            }
        }
    }

    // Elige el pivote usando la "mediana de tres" para una mejor selección.
    private int medianOfThree(int[] array, int start, int end) {
        int mid = start + (end - start) / 2; // Cálculo seguro del punto medio.
        if (array[start] > array[mid]) { swap(array, start, mid); }
        if (array[start] > array[end]) { swap(array, start, end); }
        if (array[mid] > array[end]) { swap(array, mid, end); }
        return mid; // Retorna el índice del pivote (que ahora está en 'mid').
    }

    // Intercambia dos elementos en el array.
    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // Apaga el ForkJoinPool de forma segura, liberando los recursos de los hilos.
    public static void shutdownPool() {
        forkJoinPool.shutdown(); // Inicia el apagado.
        try {
            // Espera hasta 60 segundos para que las tareas terminen.
            if (!forkJoinPool.awaitTermination(60, TimeUnit.SECONDS)) {
                forkJoinPool.shutdownNow(); // Si no terminan, fuerza el apagado.
            }
        } catch (InterruptedException e) { // Maneja interrupciones durante la espera.
            forkJoinPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}