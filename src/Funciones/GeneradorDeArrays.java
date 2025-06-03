package Funciones; // Define el paquete de la clase.

import java.util.Random; // Importa la clase Random para generar números aleatorios.

// Clase para generar diferentes tipos de arrays.
public class GeneradorDeArrays {

    // Genera un array con 'n' números aleatorios entre 'min' y 'max'.
    public static int[] generarArrayAleatorio(int n, int min, int max) {
        int[] arr = new int[n]; // Declara el array.
        Random random = new Random(); // Crea un generador de aleatorios.
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(max - min + 1) + min; // Rellena con números aleatorios.
        }
        return arr; // Devuelve el array.
    }

    // Genera un array de 'n' enteros en orden descendente (de n-1 a 0).
    public static int[] generarArrayDescendente(int n) {
        int[] arr = new int[n]; // Declara el array.
        for (int i = 0; i < n; i++) {
            arr[i] = n - 1 - i; // Asigna valores descendentes.
        }
        return arr; // Devuelve el array.
    }
}