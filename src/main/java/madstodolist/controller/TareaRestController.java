package madstodolist.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import madstodolist.service.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaRestController {

    @Autowired
    TareaService tareaService;

    @PutMapping("/{id}/titulo")
    public ResponseEntity<?> actualizarTitulo(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String nuevoTitulo = payload.get("titulo");
        if (nuevoTitulo == null || nuevoTitulo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Título vacío");
        }
        tareaService.modificaTarea(id, nuevoTitulo);
        return ResponseEntity.ok().build();
    }

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

    @PutMapping("/{id}/descripcion")
    public ResponseEntity<?> actualizarDescripcion(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String descripcion = payload.get("descripcion");
        if (descripcion == null) {
            return ResponseEntity.badRequest().body("Descripción vacía");
        }
        tareaService.actualizarDescripcion(id, descripcion);
        return ResponseEntity.ok().build();
    }

        @PostMapping("/{id}/completada")  // ✅ ruta relativa correcta
        @ResponseBody
        public ResponseEntity<?> actualizarCompletada(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
            boolean completada = payload.get("completada");
            tareaService.actualizarEstadoCompletada(id, completada);
            return ResponseEntity.ok().build();
        }


}
