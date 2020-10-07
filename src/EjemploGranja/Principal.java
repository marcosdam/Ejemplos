package EjemploGranja;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Principal {
    static ArrayList<Integer> listaAnimales;
    static BaseDatos bd;
    static Animal a;
    static int animalMostrar = 0;

    public static void main(String[] args) {


        listaAnimales = new ArrayList<>();
        bd = new BaseDatos();
        String opcion="";
        //Si la tabla no existe, la creo y mando un mensaje
        if(bd.crearTablaDatos()){
            System.out.println("Base de datos creada con éxito");
        }
        do {
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("Bienvenido a nuestra granja, que deseas hacer?");
            System.out.println("1. Leer un animal");
            System.out.println("2. Insertar un animal");
            System.out.println("3. Actualizar los datos de un animal");
            System.out.println("4. Borrar un animal");
            System.out.println("5. Ver todos los animales");
            System.out.println("6. Guardar todos los animales en un fichero");
            System.out.println("7. Leer fichero guardado6");
            System.out.println("8. Salir");

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                opcion = br.readLine();
                switch (opcion) {
                    case "1":
                        System.out.println("Dime el id del animal");
                        String ani = br.readLine();
                        a = bd.obtenerAnimal(Integer.parseInt(ani));
                        if (a != null) mostrarAnimal(a);
                        else System.out.println("El animal no está en la base de datos");
                        break;
                    case "2":
                        System.out.println("Dime los datos del animal en el siguiente formato:");
                        System.out.println("tipo,nombre,color,edad,numero de enfermedades");
                        String aux = br.readLine();
                        String[] cadena = aux.split(",");
                        listaAnimales = bd.obtenerId_animal();
                        a = new Animal(listaAnimales.size() + 1, cadena[0], cadena[1], cadena[2], Integer.parseInt(cadena[3]), Integer.parseInt(cadena[4]));
                        bd.insertAnimal(a);
                        break;
                    case "3":
                        System.out.println("Dime la id del animal que quieres actualizar?");
                        String old = br.readLine();
                        System.out.println("Este es el animal que va a actualizar:");
                        mostrarAnimal(bd.obtenerAnimal(Integer.parseInt(old)));
                        System.out.println("*************************");
                        System.out.println("Dime los nuevos datos del animal en el siguiente formato:");
                        System.out.println("tipo,nombre,color,edad,numero de enfermedades");
                        aux = br.readLine();
                        cadena = aux.split(",");
                        a = new Animal(Integer.parseInt(old), cadena[0], cadena[1], cadena[2], Integer.parseInt(cadena[3]), Integer.parseInt(cadena[4]));
                        Animal oldA = bd.obtenerAnimal(Integer.parseInt(old));
                        if (bd.updateAnimal(oldA, a)) {
                            System.out.println("animal actualizado");
                            mostrarAnimal(a);
                        } else System.out.println("No se ha podido actualizar");
                        break;
                    case "4":
                        System.out.println("dime el id del animal que quieres borrar");
                        aux = br.readLine();
                        System.out.println("Este es el animal que quieres borrar? si/no");
                        mostrarAnimal(bd.obtenerAnimal(Integer.parseInt(aux)));
                        String respuesta = br.readLine();
                        if (respuesta.equalsIgnoreCase("si")) {
                            bd.deleteAnimal(bd.obtenerAnimal(Integer.parseInt(aux)));
                            if(bd.obtenerId_animal().size()>0) {
                                listaAnimales = bd.obtenerId_animal();
                                a = bd.obtenerAnimal(listaAnimales.size() + 1);
                                Animal a2 = new Animal(Integer.parseInt(aux), a.getTipo(), a.getNombre(), a.getColor(), a.getEdad(), a.getNum_enfermedades());
                                bd.updateAnimal(a, a2);
                            }
                        }
                        break;
                    case "5":
                        listaAnimales = bd.obtenerId_animal();
                        for (int i = 1; i <= listaAnimales.size(); i++) {
                            if (bd.obtenerAnimal(i) != null)
                                mostrarAnimal(bd.obtenerAnimal(i));
                            System.out.println("******************");
                        }
                        break;
                    case "6":
                        BACKUPSerializeAnimales();
                        break;
                    case "7":
                        LeerBackupSerialize();
                        break;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }while(!opcion.equalsIgnoreCase("8"));

    }

    private static void mostrarAnimal(Animal animal) {
        System.out.println("Id: "+animal.getId_animal());
        System.out.println("Tipo: "+animal.getTipo());
        System.out.println("Nombre: "+animal.getNombre());
        System.out.println("Color: "+animal.getColor());
        System.out.println("Edad: "+animal.getEdad());
        System.out.println("Número de enfermedades: "+animal.getNum_enfermedades());

    }


    private static void BACKUPSerializeAnimales(){
        File f= new File("animales.dat");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            ArrayList<Animal> animales = bd.obtenerAnimal();
            for (Animal a:animales) {
                oos.writeObject(a);
            }
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void LeerBackupSerialize(){
        File f = new File("animales.dat");
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while(true){
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

    private static void BACKUPAnimales(){

        FileWriter fw=null;
        try {
            File fichero = new File("animales.txt");
            fw = new FileWriter(fichero);
            PrintWriter pw = new PrintWriter(fw);
            ArrayList<Animal> listado = bd.obtenerAnimal();

            for (Animal a : listado)
            {
                String animalito = a.getId_animal()+";"+ a.getTipo()
                        +";"+a.getNombre()+";"+a.getColor()
                        +";"+a.getEdad()+";"+a.getNum_enfermedades();
                pw.println(animalito);
            }

        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}