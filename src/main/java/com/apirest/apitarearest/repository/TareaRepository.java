package com.apirest.apitarearest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apirest.apitarearest.model.Tarea;


public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // Si queremos buscar tareas por estado, lo definimos aquí:
    // List<Tarea> findByCompletada(boolean completada);
    
}
