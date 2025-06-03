package Funciones; // Define el paquete de la clase.

// Clase para algoritmos de ordenamiento secuenciales.
public class QuickSort_Secuencial {

	// Muestra los elementos de un array.
	public static void mostrarArray(int[] arr) {
		System.out.println("\n-----------------------:\n"); // Imprime un separador.
		for (int num : arr) { // Itera sobre cada número.
			System.out.print(num + " - "); // Imprime el número.
		}
	}

	// Implementa el algoritmo Quick Sort secuencial.
	public static void quickSort(int[] arr, int low, int high) {
		if (low < high) { // Caso base de recursión: si hay más de un elemento.
			int pi = partition(arr, low, high); // Particiona el array y obtiene el índice del pivote.
			quickSort(arr, low, pi - 1); // Ordena recursivamente la sub-array izquierda.
			quickSort(arr, pi + 1, high); // Ordena recursivamente la sub-array derecha.
		}
	}

	// Particiona el array alrededor de un pivote.
	private static int partition(int[] arr, int low, int high) {
		// Usa mediana de tres para seleccionar el pivote.
		int pivotIndex = medianOfThree(arr, low, high); // Calcula el índice del pivote.
		swap(arr, pivotIndex, high); // Mueve el pivote al final.

		int pivot = arr[high]; // El pivote es el último elemento.
		int i = low - 1; // 'i' rastrea el último elemento menor o igual al pivote.

		for (int j = low; j < high; j++) { // Recorre el array hasta el pivote.
			if (arr[j] < pivot) { // Si el elemento actual es menor que el pivote.
				i++; // Incrementa 'i'.
				swap(arr, i, j); // Intercambia elementos.
			}
		}

		swap(arr, i + 1, high); // Coloca el pivote en su posición final.
		return i + 1; // Retorna el índice final del pivote.
	}

	// Selecciona la mediana de tres como pivote y la coloca en 'mid'.
	private static int medianOfThree(int[] arr, int low, int high) {
		int mid = low + (high - low) / 2; // Calcula el punto medio para mediana.

		// Ordena los elementos en low, mid, high para encontrar la mediana.
		if (arr[low] > arr[mid]) {
			swap(arr, low, mid);
		} // Asegura arr[low] <= arr[mid].
		if (arr[low] > arr[high]) {
			swap(arr, low, high);
		} // Asegura arr[low] <= arr[high].
		if (arr[mid] > arr[high]) {
			swap(arr, mid, high);
		} // Asegura arr[mid] <= arr[high].

		return mid; // Retorna el índice donde se encuentra la mediana (que será el pivote).
	}

	// Intercambia dos elementos en el array.
	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i]; // Guarda el valor de arr[i].
		arr[i] = arr[j]; // Asigna el valor de arr[j] a arr[i].
		arr[j] = temp; // Asigna el valor guardado a arr[j].
	}
}