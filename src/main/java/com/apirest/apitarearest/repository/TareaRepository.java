package com.apirest.apitarearest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apirest.apitarearest.model.Tarea;


public interface TareaRepository extends JpaRepository<Tarea, Long> {
    
    // Método para borrar todas las tareas que tengan completada = true
    void deleteByCompletada(boolean completada);
    
}
