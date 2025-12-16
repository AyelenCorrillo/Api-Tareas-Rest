package com.apirest.apitarearest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.apitarearest.exception.RecursoNoEncontradoException;
import com.apirest.apitarearest.model.Tarea;
import com.apirest.apitarearest.repository.TareaRepository;


@RestController
@RequestMapping("/api/v1/tareas") // URI base para todos los endpoints de tareas
public class TareaController {

    @Autowired
    private TareaRepository tareaRepository;

    // 1. OBTENER TODAS las tareas (READ - All)
    // Método: GET /api/v1/tareas
    @GetMapping
    public List<Tarea> obtenerTodas() {
        return tareaRepository.findAll();
    }

    // 2. CREAR una nueva tarea (CREATE)
    // Método: POST /api/v1/tareas
    @PostMapping
    public Tarea crearTarea(@RequestBody Tarea tarea) {
        // @RequestBody mapea el JSON entrante al objeto Tarea
        return tareaRepository.save(tarea);
    }
    
    // 3. OBTENER una tarea por ID (READ - Single)
    // Método: GET /api/v1/tareas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable Long id) {
        // findById devuelve un Optional
        Tarea tarea = tareaRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada"));
        return ResponseEntity.ok(tarea);
    }

    // 4. ACTUALIZAR una tarea (UPDATE)
    // Método: PUT /api/v1/tareas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizarTarea(@PathVariable Long id, @RequestBody Tarea detallesTarea) {
        Tarea tarea = tareaRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada"));
        
        // Actualizamos solo los campos necesarios
        tarea.setDescripcion(detallesTarea.getDescripcion());
        tarea.setCompletada(detallesTarea.isCompletada());
        
        final Tarea tareaActualizada = tareaRepository.save(tarea);
        return ResponseEntity.ok(tareaActualizada);
    }

    // 5. ELIMINAR una tarea (DELETE)
    // Método: DELETE /api/v1/tareas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTarea(@PathVariable Long id) {
        Tarea tarea = tareaRepository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada"));
        tareaRepository.delete(tarea);
        // Devolvemos una respuesta vacía, indicando éxito
        return ResponseEntity.ok().build(); 
    }
    
}
