package org.example.controller; // Asegúrate que el paquete sea el correcto

import org.example.model.Proyecto;
import org.example.view.VistaProyectos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControladorVista {

    private GestorProyectos modelo;
    private VistaProyectos vista;

    public ControladorVista(GestorProyectos modelo, VistaProyectos vista) {
        this.modelo = modelo;
        this.vista = vista;

        // Configurar listeners para los botones en la vista
        this.vista.addRegistrarListener(new RegistrarListener());
        this.vista.addEliminarListener(new EliminarListener());     // Añadir listener para Eliminar
        this.vista.addLimpiarTodoListener(new LimpiarTodoListener());// Añadir listener para Limpiar Todo

        // Inicializar la vista
        actualizarListaVista();
    }

    /**
     * Inicia la aplicación haciendo visible la ventana.
     */
    public void iniciar() {
        vista.setVisible(true);
        vista.setStatus("Aplicación iniciada. Ingrese un proyecto.");
    }

    /**
     * Procesa la acción de registrar un nuevo proyecto.
     */
    public void registrarProyecto() {
        String nombre = vista.getNombreProyecto();
        String notaStr = vista.getNotaProyecto();

        // La validación de nombre vacío ahora la hace el modelo, pero podemos
        // mantener una verificación rápida aquí para evitar llamadas innecesarias.
        if (nombre.isEmpty()) {
            vista.mostrarMensajeError("El nombre del proyecto no puede estar vacío.");
            return;
        }

        double nota;
        try {
            nota = Double.parseDouble(notaStr);
            Proyecto nuevoProyecto = new Proyecto(nombre, nota);
            modelo.agregarProyecto(nuevoProyecto); // Modelo valida nombre y nota

            vista.mostrarMensajeInfo("Proyecto '" + nombre + "' registrado con éxito.");
            vista.limpiarCampos();
            actualizarListaVista();

        } catch (NumberFormatException e) {
            vista.mostrarMensajeError("La nota debe ser un número válido (ej: 7.5).");
        } catch (IllegalArgumentException e) {
            // Captura errores de validación del modelo (nombre o nota)
            vista.mostrarMensajeError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarMensajeError("Ocurrió un error inesperado al registrar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Procesa la acción de eliminar un proyecto.
     */
    public void eliminarProyecto() {
        // 1. Pedir a la vista el nombre del proyecto a eliminar
        String nombreAEliminar = vista.pedirNombreParaEliminar();

        // 2. Verificar si el usuario ingresó un nombre o canceló
        if (nombreAEliminar == null) {
            vista.setStatus("Eliminación cancelada por el usuario.");
            return; // El usuario presionó Cancelar o cerró el diálogo
        }
        if (nombreAEliminar.trim().isEmpty()) {
            vista.mostrarMensajeError("Debe ingresar un nombre de proyecto para eliminar.");
            return; // Nombre vacío ingresado
        }

        // 3. Intentar eliminar el proyecto a través del modelo
        boolean eliminado = modelo.eliminarProyectoPorNombre(nombreAEliminar.trim());

        // 4. Informar al usuario y actualizar la vista
        if (eliminado) {
            vista.mostrarMensajeInfo("Proyecto '" + nombreAEliminar.trim() + "' eliminado con éxito.");
            actualizarListaVista(); // Actualiza la lista en la vista
        } else {
            vista.mostrarMensajeError("No se encontró ningún proyecto con el nombre '" + nombreAEliminar.trim() + "'.");
        }
    }


    /**
     * Actualiza el contenido del JTextArea en la vista
     * con la lista actual de proyectos del modelo.
     */
    public void actualizarListaVista() {
        List<Proyecto> proyectos = modelo.getProyectos();
        StringBuilder sb = new StringBuilder();

        if (proyectos.isEmpty()) {
            sb.append("No hay proyectos registrados.");
        } else {
            for (int i = 0; i < proyectos.size(); i++) {
                // Añadir un índice podría ser útil para la selección visual si se cambia a JList
                // sb.append(i + 1).append(". ");
                sb.append(proyectos.get(i).toString()).append("\n");
            }
            // Quitar el último salto de línea si existe
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
        }
        vista.setListaProyectos(sb.toString());
        vista.setStatus("Lista actualizada. Total: " + obtenerNumeroProyectos());
    }

    /**
     * Obtiene y devuelve la cantidad de proyectos registrados.
     * @return Número de proyectos.
     */
    public int obtenerNumeroProyectos() {
        return modelo.getNumeroProyectos();
    }

    /**
     * Limpia todos los proyectos registrados y actualiza la vista,
     * pidiendo confirmación al usuario.
     */
    public void limpiarTodosLosProyectos() {
        // La confirmación se hace directamente aquí (podría estar en la vista también)
        int confirm = JOptionPane.showConfirmDialog(
                vista, // El componente padre es la vista principal
                "¿Está seguro de que desea eliminar TODOS los proyectos registrados?\nEsta acción no se puede deshacer.",
                "Confirmar Limpieza Total",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            modelo.limpiarProyectos(); // Llama al método del modelo
            actualizarListaVista();   // Actualiza la vista para mostrarla vacía
            vista.mostrarMensajeInfo("Todos los proyectos han sido eliminados.");
        } else {
            vista.setStatus("Limpieza total cancelada por el usuario.");
        }
    }


    // --- Clases internas para manejar los eventos de los botones ---

    // Listener para el botón Registrar
    class RegistrarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            registrarProyecto();
        }
    }

    // Listener para el NUEVO botón Eliminar
    class EliminarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            eliminarProyecto(); // Llama al nuevo método del controlador
        }
    }

    // Listener para el NUEVO botón Limpiar Todo
    class LimpiarTodoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            limpiarTodosLosProyectos(); // Llama al método existente del controlador
        }
    }
}