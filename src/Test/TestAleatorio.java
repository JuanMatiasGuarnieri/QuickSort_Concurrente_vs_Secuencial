package Test; // Define el paquete de la clase.

import Funciones.GeneradorDeArrays; // Importa la clase para generar arrays.
import Funciones.QuickSort_Concurrente; // Importa la implementaci�n concurrente.
import Funciones.QuickSort_Secuencial; // Importa la implementaci�n secuencial.

import java.util.Scanner; // Para leer la entrada del usuario.
import java.util.concurrent.Future; // Para manejar el resultado de tareas as�ncronas.

public class TestAleatorio { // Clase principal para probar QuickSort con arrays aleatorios.
	public static void main(String[] args) { // M�todo principal de ejecuci�n.

		// --- Configuraci�n de Tama�o del Array y tama�o minimo de la particion ---

		Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para entrada.
		int cantidadElementos = 0; // Variable para almacenar la cantidad elegida.
		boolean entradaValida = false; // Bandera para validar la entrada del usuario.

		// --- Men� de Selecci�n de Cantidad de Elementos para Arrays Aleatorios ---
		System.out.println("--- Prueba de QuickSort con Arrays ALEATORIOS ---"); // T�tulo del men�.
		System.out.println("Selecciona la cantidad de elementos:"); // Instrucci�n.
		System.out.println("1. 10 elementos"); // Opci�n 1.
		System.out.println("2. 1,000 elementos"); // Opci�n 2.
		System.out.println("3. 100,000 elementos"); // Opci�n 3.
		System.out.println("4. 1,000,000 elementos"); // Opci�n 4.
		System.out.println("5. 50,000,000 elementos"); // Opci�n 5.
		System.out.println("6. Ingresar cantidad personalizada"); // Opci�n 6.

		while (!entradaValida) { // Bucle hasta que la entrada sea v�lida.
			System.out.print("Elige una opci�n: "); // Pide al usuario que elija.
			try { // Intenta leer la entrada.
				int opcion = scanner.nextInt(); // Lee la opci�n.
				switch (opcion) { // Eval�a la opci�n elegida.
				case 1:
					cantidadElementos = 10;
					entradaValida = true;
					break; // Asigna valor y valida.
				case 2:
					cantidadElementos = 1000;
					entradaValida = true;
					break; // Asigna valor y valida.
				case 3:
					cantidadElementos = 100000;
					entradaValida = true;
					break; // Asigna valor y valida.
				case 4:
					cantidadElementos = 1000000;
					entradaValida = true;
					break; // Asigna valor y valida.
				case 5:
					cantidadElementos = 50000000;
					entradaValida = true;
					break; // Asigna valor y valida.
				case 6: // Opci�n para cantidad personalizada.
					System.out.print("Introduce la cantidad de elementos personalizada: "); // Pide cantidad
																							// personalizada.
					cantidadElementos = scanner.nextInt(); // Lee la cantidad.
					if (cantidadElementos <= 0) { // Valida que sea positiva.
						System.out.println("La cantidad debe ser un n�mero positivo. Intenta de nuevo."); // Mensaje de
																											// error.
					} else {
						entradaValida = true; // Valida la entrada.
					}
					break; // Sale del switch.
				default: // Opci�n no reconocida.
					System.out.println("Opci�n no v�lida. Por favor, selecciona un n�mero del 1 al 6."); // Mensaje de
																											// error.
				}
			} catch (Exception e) { // Captura cualquier error de entrada (ej. no num�rico).
				System.out.println("Entrada inv�lida. Por favor, ingresa un n�mero."); // Mensaje de error.
				scanner.next(); // Limpia el buffer del scanner.
			}
		}

		// Define el umbral de partici�n para decidir si se paraleliza o se ordena
		// secuencialmente.
		int minPartitionSize = 5000; // Umbral fijo para concurrencia.

		System.out.println("Cantidad de Elementos a Ordenar: " + cantidadElementos); // Muestra cantidad elegida.
		System.out.println("Umbral de Paralelizaci�n (minPartitionSize): " + minPartitionSize); // Muestra umbral.
		System.out.println("N�mero de Procesadores Disponibles: " + Runtime.getRuntime().availableProcessors()); // Muestra
																													// n�cleos
																													// de
																													// CPU.

		// --- Prueba de QuickSort Concurrente ---
		System.out.println("\n--- Iniciando QuickSort Concurrente ---"); // Mensaje de inicio.

		// Genera un array aleatorio para la prueba concurrente.
		int[] arrConcurrente = GeneradorDeArrays.generarArrayAleatorio(cantidadElementos, 0, 50000);

		// Crea una instancia de la tarea concurrente de QuickSort.
		QuickSort_Concurrente tareaInicialConcurrente = new QuickSort_Concurrente(minPartitionSize, arrConcurrente, 0,
				arrConcurrente.length - 1);

		long tiempoInicioConcurrente = System.currentTimeMillis(); // Marca el tiempo de inicio.

		// Env�a la tarea al ForkJoinPool y obtiene un Future para esperar.
		Future<?> future = QuickSort_Concurrente.forkJoinPool.submit(tareaInicialConcurrente);

		try { // Intenta esperar la finalizaci�n de la tarea.
			future.get(); // Espera a que la tarea principal (y sus sub-tareas) terminen.
		} catch (Exception ex) { // Captura y maneja excepciones durante la ejecuci�n concurrente.
			System.err.println("Error durante la ejecuci�n concurrente: " + ex.getMessage()); // Muestra mensaje de
																								// error.
			ex.printStackTrace(); // Imprime el rastro de la pila del error.
			Thread.currentThread().interrupt(); // Restablece el estado de interrupci�n del hilo.
		}

		long tiempoFinConcurrente = System.currentTimeMillis(); // Marca el tiempo de fin.
		long duracionConcurrente = (tiempoFinConcurrente - tiempoInicioConcurrente); // Calcula la duraci�n.

		System.out.println("QuickSort Concurrente Terminado. Tiempo transcurrido: " + duracionConcurrente + " ms"); // Muestra
																													// el
																													// resultado.

		// Prueba de QuickSort Secuencial --- y apagado del pool...

		System.out.println("\n--- Iniciando QuickSort Secuencial ---"); // Mensaje de inicio.

		// Genera un nuevo array aleatorio para la prueba secuencial.
		int[] arrSecuencial = GeneradorDeArrays.generarArrayAleatorio(cantidadElementos, 0, 50000);

		long tiempoInicioSecuencial = System.currentTimeMillis(); // Marca el tiempo de inicio.
		// Llama al m�todo de ordenamiento QuickSort secuencial.
		QuickSort_Secuencial.quickSort(arrSecuencial, 0, arrSecuencial.length - 1);
		long tiempoFinSecuencial = System.currentTimeMillis(); // Marca el tiempo de fin.
		long duracionSecuencial = (tiempoFinSecuencial - tiempoInicioSecuencial); // Calcula la duraci�n.

		System.out.println("QuickSort Secuencial Terminado. Tiempo transcurrido: " + duracionSecuencial + " ms"); // Muestra
																													// el
																													// resultado.

		QuickSort_Concurrente.shutdownPool(); // Apaga el ForkJoinPool.
		System.out.println("\nForkJoinPool apagado."); // Confirma el apagado.
		scanner.close(); // Cierra el scanner.
	}
}