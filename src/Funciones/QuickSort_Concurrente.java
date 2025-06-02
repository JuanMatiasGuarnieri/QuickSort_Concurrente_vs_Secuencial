package Funciones;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// Clase que implementa la interfaz Runnable para poder ser ejecutada en un hilo
public class QuickSort_Concurrente implements Runnable {

	// Constante que define el n�mero m�ximo de hilos disponibles seg�n el n�mero de
	// procesadores
	public static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

	// Pool de hilos para la ejecuci�n concurrente
	public static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

	// Variables de instancia para el array a ordenar y los �ndices de inicio y fin
	final int[] my_array;
	final int start, end;

	// Tama�o m�nimo de la partici�n para paralelizar
	private final int minParitionSize;

	// Constructor de la clase
	public QuickSort_Concurrente(int minParitionSize, int[] array, int start, int end) {
		this.minParitionSize = minParitionSize;
		this.my_array = array;
		this.start = start;
		this.end = end;
	}

	// M�todo run que se ejecutar� cuando el hilo inicie
	public void run() {
		quicksort(my_array, start, end);
	}

	public static int[] generarArrayAleatorio(int n, int min, int max) {
		// Declaraci�n del array
		int[] arr = new int[n];

		// Generaci�n de n�meros aleatorios
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			arr[i] = random.nextInt(max - min + 1) + min;
		}

		return arr;
	}

	public static void mostrarArray(int[] arr) {
		System.out.println("\n-----------------------:\n");
		for (int num : arr) {
			System.out.print(num + " - ");
		}
	}

	// M�todo que implementa el algoritmo QuickSort
	public void quicksort(int[] array, int start, int end) {
		int len = end - start + 1;

		// Si la longitud de la partici�n es 1 o menos, no se necesita ordenar
		if (len <= 1)
			return;

		// Selecci�n del pivote usando la t�cnica de la mediana de tres
		int pivot_index = medianOfThree(array, start, end);
		int pivotValue = array[pivot_index];

		// Intercambio del pivote con el elemento al final del array
		swap(array, pivot_index, end);

		int storeIndex = start;
		for (int i = start; i < end; i++) {
			if (array[i] <= pivotValue) {
				swap(array, i, storeIndex);
				storeIndex++;
			}
		}

		// Colocar el pivote en su posici�n correcta
		swap(array, storeIndex, end);

		// Si la partici�n es lo suficientemente grande, paralelizar
		if (len > minParitionSize) {
			/*
			 * QuickSort_Concurrente quick = new QuickSort_Concurrente(minParitionSize,
			 * array, start, storeIndex - 1); Future<?> future = executor.submit(quick);
			 * quicksort(array, storeIndex + 1, end);
			 */
			QuickSort_Concurrente leftQuick = new QuickSort_Concurrente(minParitionSize, array, start, storeIndex - 1);
			QuickSort_Concurrente rightQuick = new QuickSort_Concurrente(minParitionSize, array, storeIndex + 1, end);
			Future<?> leftFuture = executor.submit(leftQuick);
			Future<?> rightFuture = executor.submit(rightQuick);

			// Esperar a que el subproceso finalice
			try {
				// future.get(1000, TimeUnit.SECONDS);
				leftFuture.get(1000, TimeUnit.SECONDS);
				rightFuture.get(1000, TimeUnit.SECONDS);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			// Si no, ordenar de forma secuencial
			quicksort(array, start, storeIndex - 1);
			quicksort(array, storeIndex + 1, end);
		}
	}

// M�todo que selecciona la mediana de tres elementos como pivote
	private int medianOfThree(int[] array, int start, int end) {
		int mid = (start + end) / 2;
		if (array[start] > array[mid]) {
			swap(array, start, mid);
		}
		if (array[start] > array[end]) {
			swap(array, start, end);
		}
		if (array[mid] > array[end]) {
			swap(array, mid, end);
		}
		return mid;
	}

// M�todo que intercambia dos elementos en el array
	private void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

}
