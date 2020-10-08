package Ejercicio01JDBC;

import java.util.ArrayList;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Principal {
    /*
    * Insertar un empleado
    - Modificar un empleado pidiendo el DNI.
    - Eliminar un empleado pidiendo DNI.
    - Recuperar un empleado pidiendo DNI.
    - Recuperar todos los empleados.
    - Guardar todos los empleados como objetos en un archivo de texto binario.
    - Recuperar todos los objetos del archivo de texto binario y guardarlos en la base de
    datos si no existen(Por DNI).*/
    static ArrayList<String> listaEmpleados;
    static BaseDatos baseDatos;
    static Empleado empleado;
    static int empleadoMostrar = 0;

    public static void main(String[] args) {

        listaEmpleados = new ArrayList<>();
        baseDatos = new Ejercicio01JDBC.BaseDatos();
        String opcion = "";
        //Si la tabla no existe, la creo y mando un mensaje
        if (baseDatos.crearTablaEmpleado()) {
            System.out.println("Base de datos creada con éxito");
        }
        do {
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("Bienvenido a nuestra empresa, que deseas hacer?");
            System.out.println("1. Insertar un empleado");
            System.out.println("2. Modificar los datos de un empleado");
            System.out.println("3. Eliminar un empleado");
            System.out.println("4. Leer los datos de un empleado");
            System.out.println("5. Ver todos los empleados");
            System.out.println("6. Guardar todos los empleados en un fichero");
            System.out.println("7. Leer fichero guardado");
            System.out.println("8. Salir");

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                opcion = br.readLine();
                switch (opcion) {
                    case "1":
                        System.out.println("Dime los datos del empleado en el siguiente formato:");
                        System.out.println("dni,empleo,nombre,horas semanales");
                        String aux = br.readLine();
                        String[] cadena = aux.split(",");
                        listaEmpleados = baseDatos.obtenerDNI_empleado();
                        empleado = new Empleado(cadena[0], cadena[1], cadena[2], Integer.parseInt(cadena[3]));
                        baseDatos.insertEmpleado(empleado);
                        break;
                    case "2":
                        System.out.println("Dime el DNI del empleado que quieres modificar?");
                        String old = br.readLine();
                        System.out.println("Este es el empleado que va a modificar:");
                        mostrarEmpleado(baseDatos.obtenerEmpleado(old));
                        System.out.println("*************************");
                        System.out.println("Dime los nuevos datos del empleado en el siguiente formato:");
                        System.out.println("dni,empleo,nombre,horas semanales");
                        aux = br.readLine();
                        cadena = aux.split(",");
                        empleado = new Empleado(cadena[0], cadena[1], cadena[2], Integer.parseInt(cadena[3]));
                        Empleado oldE = baseDatos.obtenerEmpleado(old);
                        if (baseDatos.updateEmpleado(oldE, empleado)) {
                            System.out.println("Empleado actualizado");
                            mostrarEmpleado(empleado);
                        } else System.out.println("No se ha podido modificar");
                        break;
                    case "3":
                        System.out.println("Dime el DNI del empleado que quieres borrar");
                        aux = br.readLine();
                        System.out.println("Este es el empleado que quieres borrar? si/no");
                        mostrarEmpleado(baseDatos.obtenerEmpleado(aux));
                        String respuesta = br.readLine();
                        if (respuesta.equalsIgnoreCase("si")) {
                            baseDatos.deleteEmpleado(baseDatos.obtenerEmpleado(aux));
                            if (baseDatos.obtenerDNI_empleado().size() > 0) {
                                listaEmpleados = baseDatos.obtenerDNI_empleado();
                                empleado = baseDatos.obtenerEmpleado(aux);
                                Empleado e2 = new Empleado(aux, empleado.getEmpleo(), empleado.getNombre(), empleado.getHoras_semanales());
                                baseDatos.updateEmpleado(empleado, e2);
                            }
                        }
                        break;
                    case "4":
                        System.out.println("Dime el DNI del empleado");
                        String empl = br.readLine();
                        empleado = baseDatos.obtenerEmpleado(empl);
                        if (empleado != null) mostrarEmpleado(empleado);
                        else System.out.println("El empleado no está en la base de datos");
                        break;
                    case "5":
                        listaEmpleados = baseDatos.obtenerDNI_empleado();
                        for (int i = 1; i <= listaEmpleados.size() + 1; i++) {
                            if (baseDatos.obtenerEmpleado() != null)
                                mostrarEmpleado(baseDatos.obtenerEmpleado(empleado.getDni()));
                            System.out.println("******************");
                        }
                        break;
                    case "6":
                        BACKUPSerializeEmpleados();
                        BACKUPEmpleados();
                        break;
                    case "7":
                        LeerBackupSerialize();
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (!opcion.equalsIgnoreCase("8"));

    }

    private static void mostrarEmpleado(Empleado empleado) {
        System.out.println("DNI: " + empleado.getDni());
        System.out.println("Empleo: " + empleado.getEmpleo());
        System.out.println("Nombre: " + empleado.getNombre());
        System.out.println("Horas semanales: " + empleado.getHoras_semanales());
    }


    private static void BACKUPSerializeEmpleados() {
        File f = new File("empleados.dat");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            ArrayList<Empleado> empleados = baseDatos.obtenerEmpleado();
            for (Empleado e : empleados) {
                oos.writeObject(e);
            }
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void LeerBackupSerialize() {
        File f = new File("empleados.dat");
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                System.out.println(ois.readObject().toString());
                System.out.println("******************");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fin del fichero");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void BACKUPEmpleados() {

        FileWriter fw = null;
        try {
            File fichero = new File("empleados.txt");
            fw = new FileWriter(fichero);
            PrintWriter pw = new PrintWriter(fw);
            ArrayList<Empleado> listado = baseDatos.obtenerEmpleado();

            for (Empleado e : listado) {
                String empleadito = e.getDni() + ";" + e.getEmpleo()
                        + ";" + e.getNombre() + ";" + e.getHoras_semanales();
                pw.println(empleadito);
            }

        } catch (IOException ex) {
            Logger.getLogger(Ejercicio01JDBC.Principal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Ejercicio01JDBC.Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
