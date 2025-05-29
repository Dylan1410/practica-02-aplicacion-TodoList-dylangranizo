package madstodolist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import madstodolist.service.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaRestController {

    @Autowired
    TareaService tareaService;

    // ✅ Actualizar título
    @PutMapping("/{id}/titulo")
    public ResponseEntity<?> actualizarTitulo(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String nuevoTitulo = payload.get("titulo");
        if (nuevoTitulo == null || nuevoTitulo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Título vacío");
        }
        tareaService.modificaTarea(id, nuevoTitulo);
        return ResponseEntity.ok().build();
    }

    // ✅ Nuevo: actualizar estado completada
    @PutMapping("/{id}/completada")
    public ResponseEntity<?> actualizarEstadoCompletada(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        Boolean completada = payload.get("completada");
        if (completada == null) {
            return ResponseEntity.badRequest().body("Estado 'completada' no proporcionado");
        }
        tareaService.actualizarEstadoCompletada(id, completada);
        return ResponseEntity.ok().build();
    }
}
