package org.example;

import org.example.controller.ControladorVista;
import org.example.controller.GestorProyectos;
import org.example.view.VistaProyectos;

import javax.swing.*; // Para SwingUtilities

public class Main {

    public static void main(String[] args) {
        // Es buena práctica ejecutar el código de Swing en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Crear el Modelo
                GestorProyectos modelo = new GestorProyectos();

                // 2. Crear la Vista
                VistaProyectos vista = new VistaProyectos();

                // 3. Crear el Controlador (pasando Modelo y Vista)
                ControladorVista controlador = new ControladorVista(modelo, vista);

                // 4. Iniciar la aplicación (hacer visible la ventana)
                controlador.iniciar();
            }
        });
    }
}