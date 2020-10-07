package EjemploGranja;

import java.io.Serializable;

public class Animal implements Serializable {
    private int id_animal;
    private String tipo;
    private String nombre;
    private String color;
    private int edad;
    private int num_enfermedades;

    public Animal(int id_animal, String tipo, String nombre, String color, int edad, int num_enfermedades) {
        this.id_animal = id_animal;
        this.tipo = tipo;
        this.nombre = nombre;
        this.color = color;
        this.edad = edad;
        this.num_enfermedades = num_enfermedades;
    }

    public int getId_animal() {
        return id_animal;
    }

    public void setId_animal(int id_animal) {
        this.id_animal = id_animal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getNum_enfermedades() {
        return num_enfermedades;
    }

    public void setNum_enfermedades(int num_enfermedades) {
        this.num_enfermedades = num_enfermedades;
    }

    @Override
    public String toString() {
        return "Id: "+id_animal+"\n"+
                "Tipo: "+tipo+"\n"+
                "Nombre: "+nombre+"\n"+
                "Color: "+color+"\n"+
                "Edad: "+edad+"\n"+
                "Número de enfermedades: "+num_enfermedades;

    }
}
