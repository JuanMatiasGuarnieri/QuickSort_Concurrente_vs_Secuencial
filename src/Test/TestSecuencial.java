package Test;

import Funciones.QuickSort_Secuencial;

public class TestSecuencial
{
		public static void main(String[] args) 
		{
			int cantidadElementos = 1000;

			// Generar un array aleatorio
			int[] arr2 = QuickSort_Secuencial.generarArrayAleatorio(cantidadElementos, 0, cantidadElementos*10);

			// Mostrar el array antes de ordenar
			System.out.println("Array antes de ordenar:");
			QuickSort_Secuencial.mostrarArray(arr2);
			
			//Secuencial
			
			// Iniciar el contador de tiempo
			long startTime = System.nanoTime();
			System.out.println("\nQuicksort Secuencial Iniciado");
			QuickSort_Secuencial.quickSort(arr2,0,arr2.length-1);
			
			// Detener el contador de tiempo
			long endTime = System.nanoTime();
						
			// Calcular el tiempo transcurrido en nanosegundos
			long duration = (endTime - startTime);

			// Mostrar el array después de ordenar
			System.out.println("\nArray Después de Ordenar:");
			QuickSort_Secuencial.mostrarArray(arr2);

			// Mostrar el tiempo transcurrido
			System.out.println("\nQuicksort Secuencial Termimado. Tiempo Transcurrido: " + duration + " ns");
		}
}
