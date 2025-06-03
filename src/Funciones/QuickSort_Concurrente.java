package Funciones; // Define el paquete de la clase.

import java.util.concurrent.Future; // Para manejar el resultado de tareas asíncronas.
import java.util.concurrent.TimeUnit; // Para definir unidades de tiempo.
import java.util.concurrent.ForkJoinPool; // Importa ForkJoinPool para gestión de hilos.

// Implementa Runnable para permitir ejecución concurrente.
public class QuickSort_Concurrente implements Runnable {

	// Pool de hilos estático para tareas concurrentes.
	public static final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

	// Array a ordenar y límites de la partición actual.
	final int[] my_array;
	final int start, end;

	// Umbral de tamaño para decidir entre ordenamiento concurrente o secuencial.
	private final int minPartitionSize;

	// Constructor: inicializa el array, rango y umbral.
	public QuickSort_Concurrente(int minPartitionSize, int[] array, int start, int end) {
		this.minPartitionSize = minPartitionSize; // Asigna el umbral.
		this.my_array = array; // Asigna el array.
		this.start = start; // Asigna el inicio del rango.
		this.end = end; // Asigna el fin del rango.
	}

	// Método que se ejecuta al iniciar la tarea en un hilo.
	@Override
	public void run() {
		quicksort(my_array, start, end); // Inicia el QuickSort en el rango dado.
	}

	// Imprime los elementos de un array.
	public static void mostrarArray(int[] arr) {
		System.out.println("\n-----------------------:\n"); // Imprime un separador.
		for (int num : arr) { // Itera sobre cada número.
			System.out.print(num + " - "); // Imprime el número.
		}
	}

	// Implementación de QuickSort con lógica de paralelización.
	private void quicksort(int[] array, int start, int end) {
		int len = end - start + 1; // Calcula la longitud de la partición.

		if (len <= 1) { // Caso base: array de 0 o 1 elemento ya ordenado.
			return;
		}

		// Si la partición es pequeña, ordena secuencialmente.
		if (len <= minPartitionSize) {
			// Lógica de partición y ordenamiento secuencial.
			int pivot_index = medianOfThree(array, start, end); // Selecciona pivote.
			int pivotValue = array[pivot_index]; // Obtiene valor del pivote.
			swap(array, pivot_index, end); // Mueve pivote al final.

			int storeIndex = start; // Índice para elementos menores que el pivote.
			for (int i = start; i < end; i++) { // Itera para particionar.
				if (array[i] <= pivotValue) { // Si el elemento es menor o igual al pivote.
					swap(array, i, storeIndex); // Intercambia.
					storeIndex++; // Incrementa el índice de almacenamiento.
				}
			}
			swap(array, storeIndex, end); // Coloca el pivote en su posición final.

			// Llamadas recursivas secuenciales para las sub-particiones.
			quicksort(array, start, storeIndex - 1); // Ordena sub-array izquierda.
			quicksort(array, storeIndex + 1, end); // Ordena sub-array derecha.

		} else { // Si la partición es grande, paraleliza.
			// Lógica de partición (idéntica al caso secuencial).
			int pivot_index = medianOfThree(array, start, end); // Selecciona pivote.
			int pivotValue = array[pivot_index]; // Obtiene valor del pivote.
			swap(array, pivot_index, end); // Mueve pivote al final.

			int storeIndex = start; // Índice para elementos menores que el pivote.
			for (int i = start; i < end; i++) { // Itera para particionar.
				if (array[i] <= pivotValue) { // Si el elemento es menor o igual al pivote.
					swap(array, i, storeIndex); // Intercambia.
					storeIndex++; // Incrementa el índice de almacenamiento.
				}
			}
			swap(array, storeIndex, end); // Coloca el pivote en su posición final.

			// Crea tareas para las sub-particiones izquierda y derecha.
			QuickSort_Concurrente leftQuick = new QuickSort_Concurrente(minPartitionSize, array, start, storeIndex - 1);
			QuickSort_Concurrente rightQuick = new QuickSort_Concurrente(minPartitionSize, array, storeIndex + 1, end);

			// Envía tareas al ForkJoinPool para ejecución paralela.
			Future<?> leftFuture = forkJoinPool.submit(leftQuick); // Envía tarea izquierda.
			Future<?> rightFuture = forkJoinPool.submit(rightQuick); // Envía tarea derecha.

			try { // Espera a que ambas tareas terminen.
				leftFuture.get(); // Espera tarea izquierda.
				rightFuture.get(); // Espera tarea derecha.
			} catch (Exception ex) { // Maneja excepciones de hilos.
				ex.printStackTrace(); // Imprime error.
				Thread.currentThread().interrupt(); // Restaura estado de interrupción.
			}
		}
	}

	// Elige el pivote usando la "mediana de tres".
	private int medianOfThree(int[] array, int start, int end) {
		int mid = start + (end - start) / 2; // Calcula el índice medio.
		if (array[start] > array[mid]) {
			swap(array, start, mid);
		} // Ordena start y mid.
		if (array[start] > array[end]) {
			swap(array, start, end);
		} // Ordena start y end.
		if (array[mid] > array[end]) {
			swap(array, mid, end);
		} // Ordena mid y end.
		return mid; // Retorna el índice de la mediana.
	}

	// Intercambia dos elementos en el array.
	private void swap(int[] array, int i, int j) {
		int temp = array[i]; // Guarda temporalmente el valor.
		array[i] = array[j]; // Asigna el valor de j a i.
		array[j] = temp; // Asigna el valor temporal (original de i) a j.
	}

	// Apaga el ForkJoinPool de forma segura.
	public static void shutdownPool() {
		forkJoinPool.shutdown(); // Inicia el apagado del pool.
		try {
			// Espera a que las tareas finalicen (máx. 60 segundos).
			if (!forkJoinPool.awaitTermination(60, TimeUnit.SECONDS)) {
				forkJoinPool.shutdownNow(); // Si no terminan, fuerza el apagado.
			}
		} catch (InterruptedException e) { // Maneja interrupciones.
			forkJoinPool.shutdownNow(); // Fuerza el apagado en caso de interrupción.
			Thread.currentThread().interrupt(); // Restaura el estado de interrupción.
		}
	}
}