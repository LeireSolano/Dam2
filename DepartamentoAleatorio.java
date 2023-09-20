package Departamentos;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.util.Scanner;

public class DepartamentoAleatorio {

	static File fichero = new File("DepartamentoAleatorio.dat");
	static int longitud = 4 + 24 + 30;
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// crear fichero departamentos con datos leidos de teclado hasta que el
		// departamento sea <=0
		// longitud registro-> 60bytes
		// numdept 4 id del registro
		// nombre 12*2=24
		// localidad 15*2=30

		//EliminarFichero();
		CrearDepartamentos();
		System.out.println("===========================");
		mostrarFichero();

	}

	private static void CrearDepartamentos() throws IOException {

		RandomAccessFile file = new RandomAccessFile(fichero, "rw");
		StringBuffer buffer = null;

		// para situarnos al principio

		int numDept;

		String nombre;
		String localidad;

		while (true) {

			System.out.print("Numero de departamento: ");
			numDept = sc.nextInt();

			if (numDept <= 0) {

				break;
			}

			if (!ExisteDepartamento(numDept)) {

				System.out.println("El departamento no existe, introduce los datos");

				/*
				 * System.out.print("Numero de departamento: "); numDept = sc.nextInt();
				 */

				if (numDept <= 0) {

					break;
				}

				long posicion = (numDept - 1) * 58;
				file.seek(posicion);

				System.out.print("Nombre de departamento: ");
				sc.nextLine();
				nombre = sc.nextLine();

				System.out.print("Localidad: ");
				// sc.nextLine();
				localidad = sc.nextLine();

				file.writeInt(numDept);
				buffer = new StringBuffer(nombre);
				buffer.setLength(12);
				file.writeChars(buffer.toString());

				buffer = new StringBuffer(localidad);
				buffer.setLength(15);
				file.writeChars(buffer.toString());

				System.out.println("Departamento " + numDept + " insertado");
				
				System.out.println("-----");
			} else {

				System.out.println("Ya existe el departamento " + numDept + "...");
			}
		}

		file.close();
	}

	private static boolean ExisteDepartamento(int numDept) throws IOException {

		RandomAccessFile file = new RandomAccessFile(fichero, "r");

		boolean valor = false;
		long posicion = (numDept - 1) * longitud;

		if (posicion >= file.length()) {

			file.close();
			return valor;

		} else {

			file.seek(posicion);
			int idDept = file.readInt();

			if (idDept == numDept) {

				valor = true;
			}
		}

		file.close();

		return valor;
	}

	public static void mostrarFichero() throws IOException {

		RandomAccessFile file = new RandomAccessFile(fichero, "r");

		int id;
		char nom[] = new char[12], aux;
		char loc[] = new char[15];
		int posicion = 0;

		for (;;) {

			file.seek(posicion);
			System.out.println("Posicion: " + posicion + ", puntero: " + file.getFilePointer());

			id = file.readInt();

			for (int i = 0; i < nom.length; i++) {

				aux = file.readChar();
				nom[i] = aux;

			}

			String nombreDep = new String(nom);

			for (int i = 0; i < loc.length; i++) {

				aux = file.readChar();
				loc[i] = aux;
			}

			String localidad = new String(loc);

			if (id > 0) {

				System.out.printf("ID %d, Nombre departamento: %s, Localidad: %s%n", id, nombreDep, localidad);
			}

			if (file.getFilePointer() == file.length()) {

				break;
			}

			posicion = posicion + 58;
		}

		file.close();

	}

	private static void EliminarFichero() {

		if (fichero.delete()) {
			
			System.out.println("Fichero eliminado");
			
		} else {
			
			System.out.println("No se puede eliminar el fichero");
			
		}
	}

}
