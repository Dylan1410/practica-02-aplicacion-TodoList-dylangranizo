package madstodolist.controller;

import java.time.LocalDate;
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

    @PutMapping("/{id}/prioridad")
    public ResponseEntity<?> actualizarPrioridad(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String prioridad = payload.get("prioridad");
        if (prioridad == null || prioridad.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Prioridad vacía");
        }
        tareaService.actualizarPrioridad(id, prioridad);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/fechaLimite")
    public ResponseEntity<?> actualizarFecha(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String fechaLimite = payload.get("fechaLimite");
        if (fechaLimite == null || fechaLimite.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Fecha límite vacía");
        }
        tareaService.actualizarFechaLimite(id, LocalDate.parse(fechaLimite));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/campos")
    public ResponseEntity<?> actualizarTituloYDescripcion(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String nuevoTitulo = payload.get("titulo");
        String nuevaDescripcion = payload.get("descripcion");

        if (nuevoTitulo == null || nuevoTitulo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Título vacío");
        }
        if (nuevaDescripcion == null) {
            return ResponseEntity.badRequest().body("Descripción vacía");
        }

        tareaService.actualizarTituloYDescripcion(id, nuevoTitulo, nuevaDescripcion);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/completada")
    public ResponseEntity<?> actualizarCompletada(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        boolean completada = payload.get("completada");
        tareaService.actualizarEstadoCompletada(id, completada);
        return ResponseEntity.ok().build();
    }
}
