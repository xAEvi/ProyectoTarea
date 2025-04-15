package org.example.model;

public class Proyecto {
    private String nombre;
    private double nota;

    public Proyecto(String nombre, double nota) {
        this.nombre = nombre;
        this.nota = nota;
    }

    public String getNombre() {
        return nombre;
    }

    public double getNota() {
        return nota;
    }

    @Override
    public String toString() {
        return String.format("Nombre: %s, Nota: %.2f", nombre, nota);
    }
}