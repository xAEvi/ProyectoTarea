package org.example.controller;

import org.example.model.Proyecto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator; // Importar Iterator
import java.util.List;
import java.util.Objects; // Importar Objects para comparación segura

public class GestorProyectos {
    private List<Proyecto> proyectos;

    public GestorProyectos() {
        this.proyectos = new ArrayList<>();
    }

    /**
     * Agrega un proyecto a la lista si la nota es válida y el nombre no está vacío.
     * @param proyecto El proyecto a agregar.
     * @throws IllegalArgumentException Si la nota está fuera del rango [0, 10] o el nombre es inválido.
     */
    public void agregarProyecto(Proyecto proyecto) throws IllegalArgumentException {
        // Validar nombre primero
        if (proyecto.getNombre() == null || proyecto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proyecto no puede estar vacío.");
        }
        // Validar nota
        if (proyecto.getNota() < 0 || proyecto.getNota() > 10) {
            throw new IllegalArgumentException("La nota debe estar entre 0 y 10.");
        }

        if (buscarProyectoPorNombre(proyecto.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe un proyecto con ese nombre.");
        }

        this.proyectos.add(proyecto);
    }

    /**
     * Elimina un proyecto de la lista buscando por su nombre (sensible a mayúsculas/minúsculas).
     * @param nombreProyecto El nombre exacto del proyecto a eliminar.
     * @return true si el proyecto fue encontrado y eliminado, false en caso contrario.
     */
    public boolean eliminarProyectoPorNombre(String nombreProyecto) {
        if (nombreProyecto == null || nombreProyecto.trim().isEmpty()) {
            return false; // No se puede eliminar un proyecto sin nombre válido
        }
        // Usar Iterator para eliminar de forma segura mientras se itera
        Iterator<Proyecto> iterator = this.proyectos.iterator();
        while (iterator.hasNext()) {
            Proyecto p = iterator.next();
            // Comparación exacta del nombre (sensible a mayúsculas/minúsculas)
            if (Objects.equals(p.getNombre(), nombreProyecto)) {
                iterator.remove(); // Elimina el elemento actual de forma segura
                return true; // Proyecto encontrado y eliminado
            }
        }
        return false; // Proyecto no encontrado
    }

    /**
     * Busca un proyecto por su nombre.
     * @param nombreProyecto Nombre a buscar.
     * @return El objeto Proyecto si se encuentra, null si no.
     */
    public Proyecto buscarProyectoPorNombre(String nombreProyecto) {
        if (nombreProyecto == null) return null;
        for (Proyecto p : this.proyectos) {
            if (Objects.equals(p.getNombre(), nombreProyecto)) {
                return p;
            }
        }
        return null;
    }


    /**
     * Devuelve una copia inmutable de la lista de proyectos para evitar modificaciones externas.
     * @return Lista inmutable de proyectos.
     */
    public List<Proyecto> getProyectos() {
        // Es importante devolver una copia inmutable para mantener la encapsulación
        return Collections.unmodifiableList(proyectos);
    }

    /**
     * Obtiene el número total de proyectos registrados.
     * @return Cantidad de proyectos.
     */
    public int getNumeroProyectos() {
        return this.proyectos.size();
    }

    /**
     * Limpia la lista de proyectos.
     */
    public void limpiarProyectos() {
        this.proyectos.clear();
    }
}