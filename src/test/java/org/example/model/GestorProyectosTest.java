package org.example.model;

import static org.junit.jupiter.api.Assertions.*;

import org.example.controller.GestorProyectos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class GestorProyectosTest {

    private GestorProyectos gestor;
    private Proyecto proyectoValido1;
    private Proyecto proyectoValido2;
    private Proyecto proyectoValido3;

    @BeforeEach
    void setUp() {
        gestor = new GestorProyectos(); // Instancia limpia para cada test
        proyectoValido1 = new Proyecto("Cálculo Integral", 8.5);
        proyectoValido2 = new Proyecto("Bases de Datos", 10.0);
        proyectoValido3 = new Proyecto("Intro Programación", 7.0);
    }

    // --- Pruebas para agregarProyecto(Proyecto proyecto) ---

    @Test
    @DisplayName("[agregarProyecto] Agregar proyecto con nota válida")
    void testAgregarProyectoValido() {
        assertDoesNotThrow(() -> gestor.agregarProyecto(proyectoValido1));
        assertEquals(1, gestor.getNumeroProyectos());
        assertTrue(gestor.getProyectos().contains(proyectoValido1));
    }

    @Test
    @DisplayName("[agregarProyecto] Agregar proyecto con nota límite inferior (0)")
    void testAgregarProyectoNotaLimiteInferior() {
        Proyecto pLimiteInf = new Proyecto("Ética", 0.0);
        assertDoesNotThrow(() -> gestor.agregarProyecto(pLimiteInf));
        assertEquals(1, gestor.getNumeroProyectos());
    }

    @Test
    @DisplayName("[agregarProyecto] Agregar proyecto con nota límite superior (10)")
    void testAgregarProyectoNotaLimiteSuperior() {
        // proyectoValido2 ya tiene nota 10
        assertDoesNotThrow(() -> gestor.agregarProyecto(proyectoValido2));
        assertEquals(1, gestor.getNumeroProyectos());
    }

    @Test
    @DisplayName("[agregarProyecto] Lanzar excepción si la nota es negativa")
    void testAgregarProyectoNotaInvalidaNegativa() {
        Proyecto pInvalido = new Proyecto("Física I", -1.0);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gestor.agregarProyecto(pInvalido)
        );
        assertEquals("La nota debe estar entre 0 y 10.", exception.getMessage());
        assertEquals(0, gestor.getNumeroProyectos()); // No se agregó
    }

    @Test
    @DisplayName("[agregarProyecto] Lanzar excepción si la nota es mayor a 10")
    void testAgregarProyectoNotaInvalidaMayorA10() {
        Proyecto pInvalido = new Proyecto("Algoritmos Avanzados", 10.1);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gestor.agregarProyecto(pInvalido)
        );
        assertTrue(exception.getMessage().contains("La nota debe estar entre 0 y 10"));
        assertEquals(0, gestor.getNumeroProyectos());
    }

    @Test
    @DisplayName("[agregarProyecto] Lanzar excepción si el nombre es nulo")
    void testAgregarProyectoNombreNulo() {
        Proyecto pInvalido = new Proyecto(null, 5.0);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gestor.agregarProyecto(pInvalido)
        );
        assertEquals("El nombre del proyecto no puede estar vacío.", exception.getMessage());
        assertEquals(0, gestor.getNumeroProyectos());
    }

    @Test
    @DisplayName("[agregarProyecto] Lanzar excepción si el nombre está vacío o solo espacios")
    void testAgregarProyectoNombreVacio() {
        Proyecto pInvalido = new Proyecto("   ", 7.0);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gestor.agregarProyecto(pInvalido)
        );
        assertEquals("El nombre del proyecto no puede estar vacío.", exception.getMessage());
        assertEquals(0, gestor.getNumeroProyectos());
    }

    // --- Pruebas para eliminarProyectoPorNombre(String nombreProyecto) ---

    @Test
    @DisplayName("[eliminarProyecto] Eliminar un proyecto existente de una lista con un elemento")
    void testEliminarProyectoExistenteUnico() {
        gestor.agregarProyecto(proyectoValido1);
        assertEquals(1, gestor.getNumeroProyectos());

        boolean eliminado = gestor.eliminarProyectoPorNombre("Cálculo Integral");

        assertTrue(eliminado, "Debería retornar true si el proyecto fue eliminado.");
        assertEquals(0, gestor.getNumeroProyectos(), "El número de proyectos debería ser 0 después de eliminar el único.");
        assertFalse(gestor.getProyectos().contains(proyectoValido1), "La lista no debería contener el proyecto eliminado.");
    }

    @Test
    @DisplayName("[eliminarProyecto] Eliminar un proyecto existente de una lista con múltiples elementos")
    void testEliminarProyectoExistenteMultiples() {
        gestor.agregarProyecto(proyectoValido1);
        gestor.agregarProyecto(proyectoValido2);
        gestor.agregarProyecto(proyectoValido3);
        assertEquals(3, gestor.getNumeroProyectos());

        boolean eliminado = gestor.eliminarProyectoPorNombre("Bases de Datos"); // Eliminar el del medio

        assertTrue(eliminado);
        assertEquals(2, gestor.getNumeroProyectos(), "El número de proyectos debería ser 2.");
        assertFalse(gestor.getProyectos().contains(proyectoValido2), "El proyecto eliminado (Bases de Datos) no debería estar en la lista.");
        assertTrue(gestor.getProyectos().contains(proyectoValido1), "El proyecto 'Cálculo Integral' debería permanecer.");
        assertTrue(gestor.getProyectos().contains(proyectoValido3), "El proyecto 'Intro Programación' debería permanecer.");
    }

    @Test
    @DisplayName("[eliminarProyecto] Intentar eliminar un proyecto que no existe")
    void testEliminarProyectoNoExistente() {
        gestor.agregarProyecto(proyectoValido1);
        gestor.agregarProyecto(proyectoValido3);
        assertEquals(2, gestor.getNumeroProyectos());

        boolean eliminado = gestor.eliminarProyectoPorNombre("Proyecto Inexistente");

        assertFalse(eliminado, "Debería retornar false si el proyecto no se encontró.");
        assertEquals(2, gestor.getNumeroProyectos(), "El número de proyectos no debería cambiar.");
    }

    @Test
    @DisplayName("[eliminarProyecto] Intentar eliminar un proyecto de una lista vacía")
    void testEliminarProyectoListaVacia() {
        assertEquals(0, gestor.getNumeroProyectos());
        boolean eliminado = gestor.eliminarProyectoPorNombre("Cualquier Proyecto");
        assertFalse(eliminado);
        assertEquals(0, gestor.getNumeroProyectos());
    }

    @Test
    @DisplayName("[eliminarProyecto] Intentar eliminar con nombre nulo")
    void testEliminarProyectoNombreNulo() {
        gestor.agregarProyecto(proyectoValido1);
        boolean eliminado = gestor.eliminarProyectoPorNombre(null);
        assertFalse(eliminado, "No se puede eliminar con nombre null.");
        assertEquals(1, gestor.getNumeroProyectos()); // No debe cambiar
    }

    @Test
    @DisplayName("[eliminarProyecto] Intentar eliminar con nombre vacío")
    void testEliminarProyectoNombreVacio() {
        gestor.agregarProyecto(proyectoValido1);
        boolean eliminado = gestor.eliminarProyectoPorNombre("   ");
        assertFalse(eliminado, "No se puede eliminar con nombre vacío.");
        assertEquals(1, gestor.getNumeroProyectos()); // No debe cambiar
    }

    @Test
    @DisplayName("[eliminarProyecto] Verificar sensibilidad a mayúsculas/minúsculas")
    void testEliminarProyectoCaseSensitive() {
        gestor.agregarProyecto(proyectoValido1); // Nombre: "Cálculo Integral"
        boolean eliminado = gestor.eliminarProyectoPorNombre("cálculo integral"); // Nombre en minúsculas

        assertFalse(eliminado, "La eliminación debe ser sensible a mayúsculas/minúsculas.");
        assertEquals(1, gestor.getNumeroProyectos());
    }


    // --- Pruebas para getProyectos() ---

    @Test
    @DisplayName("[getProyectos] Obtener lista vacía inicialmente")
    void testGetProyectosListaVacia() {
        List<Proyecto> lista = gestor.getProyectos();
        assertNotNull(lista, "La lista devuelta no debe ser nula.");
        assertTrue(lista.isEmpty(), "La lista inicial debe estar vacía.");
    }

    @Test
    @DisplayName("[getProyectos] Obtener lista con un elemento")
    void testGetProyectosConUnElemento() {
        gestor.agregarProyecto(proyectoValido1);
        List<Proyecto> lista = gestor.getProyectos();
        assertEquals(1, lista.size());
        assertEquals(proyectoValido1, lista.get(0));
    }

    @Test
    @DisplayName("[getProyectos] Obtener lista con múltiples elementos")
    void testGetProyectosConMultiplesElementos() {
        gestor.agregarProyecto(proyectoValido1);
        gestor.agregarProyecto(proyectoValido2);
        List<Proyecto> lista = gestor.getProyectos();
        assertEquals(2, lista.size());
        assertTrue(lista.contains(proyectoValido1));
        assertTrue(lista.contains(proyectoValido2));
    }

    @Test
    @DisplayName("[getProyectos] Verificar que la lista devuelta es inmutable")
    void testGetProyectosEsInmutable() {
        gestor.agregarProyecto(proyectoValido1);
        List<Proyecto> lista = gestor.getProyectos();

        // Intentar modificar la lista devuelta
        assertThrows(UnsupportedOperationException.class, () -> {
            lista.add(proyectoValido2); // Intentar añadir
        }, "La lista devuelta por getProyectos() debe ser inmutable.");
        assertThrows(UnsupportedOperationException.class, () -> {
            lista.remove(0); // Intentar eliminar
        }, "La lista devuelta por getProyectos() debe ser inmutable.");
        assertThrows(UnsupportedOperationException.class, () -> {
            lista.clear(); // Intentar limpiar
        }, "La lista devuelta por getProyectos() debe ser inmutable.");

        // Asegurar que la lista original no cambió
        assertEquals(1, gestor.getNumeroProyectos());
    }


    // --- Pruebas para getNumeroProyectos() ---

    @Test
    @DisplayName("[getNumeroProyectos] Obtener número inicial (0)")
    void testGetNumeroProyectosInicial() {
        assertEquals(0, gestor.getNumeroProyectos());
    }

    @Test
    @DisplayName("[getNumeroProyectos] Obtener número después de agregar uno")
    void testGetNumeroProyectosDespuesDeAgregarUno() {
        gestor.agregarProyecto(proyectoValido1);
        assertEquals(1, gestor.getNumeroProyectos());
    }

    @Test
    @DisplayName("[getNumeroProyectos] Obtener número después de agregar varios")
    void testGetNumeroProyectosDespuesDeAgregarVarios() {
        gestor.agregarProyecto(proyectoValido1);
        gestor.agregarProyecto(proyectoValido2);
        gestor.agregarProyecto(proyectoValido3);
        assertEquals(3, gestor.getNumeroProyectos());
    }

    @Test
    @DisplayName("[getNumeroProyectos] Obtener número después de agregar y eliminar")
    void testGetNumeroProyectosDespuesDeEliminar() {
        gestor.agregarProyecto(proyectoValido1);
        gestor.agregarProyecto(proyectoValido2);
        gestor.eliminarProyectoPorNombre(proyectoValido1.getNombre());
        assertEquals(1, gestor.getNumeroProyectos());
    }


    // --- Pruebas para limpiarProyectos() ---

    @Test
    @DisplayName("[limpiarProyectos] Limpiar una lista vacía")
    void testLimpiarProyectosListaVacia() {
        assertDoesNotThrow(() -> gestor.limpiarProyectos());
        assertEquals(0, gestor.getNumeroProyectos());
        assertTrue(gestor.getProyectos().isEmpty());
    }

    @Test
    @DisplayName("[limpiarProyectos] Limpiar una lista con un elemento")
    void testLimpiarProyectosConUnElemento() {
        gestor.agregarProyecto(proyectoValido1);
        assertEquals(1, gestor.getNumeroProyectos()); // Pre-condición
        gestor.limpiarProyectos();
        assertEquals(0, gestor.getNumeroProyectos());
        assertTrue(gestor.getProyectos().isEmpty());
    }

    @Test
    @DisplayName("[limpiarProyectos] Limpiar una lista con múltiples elementos")
    void testLimpiarProyectosConElementos() {
        gestor.agregarProyecto(proyectoValido1);
        gestor.agregarProyecto(proyectoValido2);
        assertNotEquals(0, gestor.getNumeroProyectos()); // Pre-condición
        gestor.limpiarProyectos();
        assertEquals(0, gestor.getNumeroProyectos());
        assertTrue(gestor.getProyectos().isEmpty());
    }

    @Test
    @DisplayName("[limpiarProyectos] Agregar proyecto después de limpiar")
    void testLimpiarYAgragarDeNuevo() {
        gestor.agregarProyecto(proyectoValido1);
        gestor.limpiarProyectos();
        assertEquals(0, gestor.getNumeroProyectos());
        gestor.agregarProyecto(proyectoValido2);
        assertEquals(1, gestor.getNumeroProyectos());
        assertTrue(gestor.getProyectos().contains(proyectoValido2));
    }


    // --- Pruebas para buscarProyectoPorNombre(String nombreProyecto) ---
    // (Método auxiliar público añadido, bueno probarlo también)

    @Test
    @DisplayName("[buscarProyecto] Encontrar proyecto existente en lista con uno")
    void testBuscarProyectoExistenteUnico() {
        gestor.agregarProyecto(proyectoValido1);
        Proyecto encontrado = gestor.buscarProyectoPorNombre("Cálculo Integral");
        assertNotNull(encontrado);
        assertEquals(proyectoValido1, encontrado);
    }

    @Test
    @DisplayName("[buscarProyecto] Encontrar proyecto existente en lista con varios")
    void testBuscarProyectoExistenteMultiple() {
        gestor.agregarProyecto(proyectoValido1);
        gestor.agregarProyecto(proyectoValido2);
        gestor.agregarProyecto(proyectoValido3);
        Proyecto encontrado = gestor.buscarProyectoPorNombre("Bases de Datos");
        assertNotNull(encontrado);
        assertEquals(proyectoValido2.getNombre(), encontrado.getNombre());
        assertEquals(proyectoValido2.getNota(), encontrado.getNota());
    }

    @Test
    @DisplayName("[buscarProyecto] No encontrar proyecto inexistente")
    void testBuscarProyectoNoExistente() {
        gestor.agregarProyecto(proyectoValido1);
        Proyecto encontrado = gestor.buscarProyectoPorNombre("Proyecto X");
        assertNull(encontrado, "No debería encontrar un proyecto inexistente.");
    }

    @Test
    @DisplayName("[buscarProyecto] Buscar en lista vacía")
    void testBuscarProyectoListaVacia() {
        Proyecto encontrado = gestor.buscarProyectoPorNombre("Cualquiera");
        assertNull(encontrado, "Buscar en lista vacía debe devolver null.");
    }

    @Test
    @DisplayName("[buscarProyecto] Buscar con nombre nulo")
    void testBuscarProyectoNombreNulo() {
        gestor.agregarProyecto(proyectoValido1);
        Proyecto encontrado = gestor.buscarProyectoPorNombre(null);
        assertNull(encontrado, "Buscar con nombre null debe devolver null.");
    }

    @Test
    @DisplayName("[buscarProyecto] Verificar sensibilidad a mayúsculas/minúsculas")
    void testBuscarProyectoCaseSensitive() {
        gestor.agregarProyecto(proyectoValido1); // Nombre: "Cálculo Integral"
        Proyecto encontrado = gestor.buscarProyectoPorNombre("cálculo integral"); // Buscar con minúsculas
        assertNull(encontrado, "La búsqueda debe ser sensible a mayúsculas/minúsculas.");
    }

}