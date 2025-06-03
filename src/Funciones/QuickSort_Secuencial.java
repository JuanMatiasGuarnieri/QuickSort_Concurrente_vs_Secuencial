package Funciones;

// Clase para algoritmos de ordenamiento secuenciales.
public class QuickSort_Secuencial {

    // Muestra los elementos de un array.
    public static void mostrarArray(int[] arr) {
        System.out.println("\n-----------------------:\n");
        for (int num : arr) {
            System.out.print(num + " - ");
        }
    }

    // Implementa el algoritmo Quick Sort secuencial.
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) { // Caso base de recursión.
            int pi = partition(arr, low, high); // Particiona el array.
            quickSort(arr, low, pi - 1); // Ordena sub-array izquierda.
            quickSort(arr, pi + 1, high); // Ordena sub-array derecha.
        }
    }

    // Particiona el array alrededor de un pivote.
    private static int partition(int[] arr, int low, int high) {
        // Usa mediana de tres para seleccionar el pivote.
        int pivotIndex = medianOfThree(arr, low, high);
        swap(arr, pivotIndex, high); // Mueve el pivote al final.

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high); // Coloca el pivote en su posición final.
        return i + 1; // Retorna el índice del pivote.
    }

    // Selecciona la mediana de tres como pivote y la coloca en 'mid'.
    private static int medianOfThree(int[] arr, int low, int high) {
        int mid = low + (high - low) / 2; // Calcula el punto medio.

        // Ordena low, mid, high.
        if (arr[low] > arr[mid]) { swap(arr, low, mid); }
        if (arr[low] > arr[high]) { swap(arr, low, high); }
        if (arr[mid] > arr[high]) { swap(arr, mid, high); }
        
        return mid; // Retorna el índice del pivote (la mediana).
    }

    // Intercambia dos elementos en el array.
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}