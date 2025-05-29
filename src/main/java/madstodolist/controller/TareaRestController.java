package madstodolist.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
