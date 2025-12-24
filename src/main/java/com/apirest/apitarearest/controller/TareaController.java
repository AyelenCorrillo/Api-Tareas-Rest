package com.apirest.apitarearest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apirest.apitarearest.exception.RecursoNoEncontradoException;
import com.apirest.apitarearest.model.Tarea;
import com.apirest.apitarearest.repository.TareaRepository;


@Controller
@RequestMapping("/tareas")
public class TareaController {

    @Autowired
    private TareaRepository tareaRepository;
    
    @GetMapping
    public String listarTareas(Model model) {
        List<Tarea> tareas = tareaRepository.findAll();
        long pendientes = tareas.stream().filter(t -> !t.isCompletada()).count();
        model.addAttribute("tareas", tareas);
        model.addAttribute("total", tareas.size());
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("nuevaTarea", new Tarea()); // Para el formulario
        return "index"; // Busca el archivo src/main/resources/templates/index.html
    }

    
    @PostMapping("/guardar")
    public String guardarTarea(@ModelAttribute Tarea tarea) {
        tareaRepository.save(tarea);
        return "redirect:/tareas";
    }

    
    @GetMapping("/toggle/{id}")
    public String toggleTarea(@PathVariable Long id) {
        Tarea tarea = tareaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada"));
        tarea.setCompletada(!tarea.isCompletada());
        tareaRepository.save(tarea);
        return "redirect:/tareas";
    }

    
    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id) {
        tareaRepository.deleteById(id);
        return "redirect:/tareas";
    }

    // 1. Mostrar el formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Tarea tarea = tareaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada"));
        model.addAttribute("tarea", tarea);
        return "editar-tarea"; 
    }

    // 2. Procesar la actualización
    @PostMapping("/actualizar/{id}")
    public String actualizarTarea(@PathVariable Long id, @ModelAttribute Tarea tareaDetalles) {
        Tarea tarea = tareaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada"));
        
        tarea.setDescripcion(tareaDetalles.getDescripcion());
        
        tareaRepository.save(tarea);
        return "redirect:/tareas";
    }

    @Transactional // Importante para operaciones de borrado personalizadas
    @GetMapping("/limpiar-completadas")
    public String limpiarCompletadas() {
        tareaRepository.deleteByCompletada(true);
        return "redirect:/tareas";
    }

    
}
