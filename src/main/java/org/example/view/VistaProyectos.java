package org.example.view; // Asegúrate que el paquete sea el correcto

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VistaProyectos extends JFrame {

    private JTextField nombreTextField;
    private JTextField notaTextField;
    private JButton registrarButton;
    private JButton eliminarButton; // Nuevo botón para eliminar
    private JButton limpiarTodoButton; // Nuevo botón para limpiar todo
    private JTextArea listaTextArea;
    private JLabel statusLabel;

    public VistaProyectos() {
        super("Registro de Proyectos de Asignatura");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 450); // Ajustar tamaño si es necesario
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel de Entrada (Norte) ---
        JPanel panelEntrada = new JPanel(new GridBagLayout());
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panelEntrada.add(new JLabel("Nombre Proyecto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        nombreTextField = new JTextField(20);
        panelEntrada.add(nombreTextField, gbc);

        // Nota
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0; gbc.anchor = GridBagConstraints.EAST;
        panelEntrada.add(new JLabel("Nota (0-10):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        notaTextField = new JTextField(5);
        panelEntrada.add(notaTextField, gbc);

        // --- Panel de Botones ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Usar FlowLayout para los botones
        registrarButton = new JButton("Registrar Proyecto");
        eliminarButton = new JButton("Eliminar Proyecto");   // Crear botón
        limpiarTodoButton = new JButton("Limpiar Todo");     // Crear botón Limpiar

        panelBotones.add(registrarButton);
        panelBotones.add(eliminarButton); // Añadir botón al panel
        panelBotones.add(limpiarTodoButton); // Añadir botón Limpiar al panel

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER; gbc.weightx = 0.0;
        panelEntrada.add(panelBotones, gbc); // Añadir panel de botones al GridBagLayout

        add(panelEntrada, BorderLayout.NORTH);

        // --- Área de Lista (Centro) ---
        listaTextArea = new JTextArea();
        listaTextArea.setEditable(false);
        listaTextArea.setMargin(new Insets(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(listaTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Proyectos Registrados"));
        add(scrollPane, BorderLayout.CENTER);

        // --- Barra de Estado (Sur) ---
        statusLabel = new JLabel("Listo.");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);
    }

    // --- Métodos para interactuar con la vista desde el controlador ---

    public String getNombreProyecto() {
        return nombreTextField.getText().trim();
    }

    public String getNotaProyecto() {
        return notaTextField.getText().trim();
    }

    public void setListaProyectos(String texto) {
        listaTextArea.setText(texto);
    }

    // Listener para el botón Registrar
    public void addRegistrarListener(ActionListener listener) {
        registrarButton.addActionListener(listener);
    }

    // Listener para el NUEVO botón Eliminar
    public void addEliminarListener(ActionListener listener) {
        eliminarButton.addActionListener(listener);
    }

    // Listener para el NUEVO botón Limpiar Todo
    public void addLimpiarTodoListener(ActionListener listener) {
        limpiarTodoButton.addActionListener(listener);
    }

    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        // Actualizar estado, limitando longitud del mensaje si es muy largo
        setStatus("Error: " + mensaje.substring(0, Math.min(mensaje.length(), 60)) + (mensaje.length() > 60 ? "..." : ""));
    }

    public void mostrarMensajeInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
        setStatus(mensaje);
    }

    // Método para pedir al usuario el nombre del proyecto a eliminar
    public String pedirNombreParaEliminar() {
        return JOptionPane.showInputDialog(
                this, // Componente padre
                "Ingrese el nombre exacto del proyecto a eliminar:", // Mensaje
                "Eliminar Proyecto", // Título del diálogo
                JOptionPane.QUESTION_MESSAGE // Tipo de mensaje
        );
    }

    public void limpiarCampos() {
        nombreTextField.setText("");
        notaTextField.setText("");
        nombreTextField.requestFocus();
    }

    public void setStatus(String mensaje) {
        statusLabel.setText(mensaje);
    }
}