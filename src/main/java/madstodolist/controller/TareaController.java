package madstodolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import madstodolist.authentication.ManagerUserSession;
import madstodolist.controller.exception.TareaNotFoundException;
import madstodolist.controller.exception.UsuarioNoLogeadoException;
import madstodolist.dto.TareaData;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Prioridad;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;

@Controller
public class TareaController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Autowired
    ManagerUserSession managerUserSession;

    private void comprobarUsuarioLogeado(Long idUsuario) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (!idUsuario.equals(idUsuarioLogeado))
            throw new UsuarioNoLogeadoException();
    }

    @GetMapping("/usuarios/{id}/tareas/nueva")
    public String formNuevaTarea(@PathVariable(value="id") Long idUsuario,
                                 @ModelAttribute TareaData tareaData,
                                 Model model,
                                 HttpSession session) {
    
        comprobarUsuarioLogeado(idUsuario);
    
        UsuarioData usuario = usuarioService.findById(idUsuario);
        model.addAttribute("usuario", usuario);
        model.addAttribute("prioridades", Prioridad.values());
        model.addAttribute("tareaData", tareaData);
    
        return "formNuevaTarea";
    }

    @PostMapping("/usuarios/{id}/tareas/nueva")
    public String nuevaTarea(@PathVariable Long id,
                             @ModelAttribute("tareaData") TareaData tareaData,
                             RedirectAttributes flash) {
    
        // Asegura que se use el método que recibe el DTO con prioridad y fecha
        tareaService.nuevaTareaUsuario(id, tareaData);
    
        flash.addFlashAttribute("mensaje", "Tarea creada con éxito.");
        return "redirect:/usuarios/" + id + "/tareas";
    }
    
    @GetMapping("/usuarios/{id}/tareas")
    public String listadoTareas(@PathVariable(value="id") Long idUsuario, Model model, HttpSession session) {
    
        comprobarUsuarioLogeado(idUsuario);
    
        UsuarioData usuario = usuarioService.findById(idUsuario);
        List<TareaData> tareas = tareaService.allTareasUsuario(idUsuario);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);
        
        // NUEVOS ATRIBUTOS PARA EL FORMULARIO RÁPIDO
        model.addAttribute("tareaData", new TareaData());
        model.addAttribute("prioridades", Prioridad.values());
    
        return "listaTareas";
    }
    

    @GetMapping("/tareas/{id}/editar")
    public String formEditaTarea(@PathVariable(value="id") Long idTarea, @ModelAttribute TareaData tareaData,
                                 Model model, HttpSession session) {

        TareaData tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        comprobarUsuarioLogeado(tarea.getUsuarioId());

        model.addAttribute("tarea", tarea);
        model.addAttribute("prioridades", Prioridad.values());
        tareaData.setTitulo(tarea.getTitulo());
        tareaData.setPrioridad(tarea.getPrioridad());
        tareaData.setFechaLimite(tarea.getFechaLimite());  // <- Nuevo
        return "formEditarTarea";
    }

    @PostMapping("/tareas/{id}/editar")
    public String grabaTareaModificada(@PathVariable(value="id") Long idTarea, @ModelAttribute TareaData tareaData,
                                       Model model, RedirectAttributes flash, HttpSession session) {

        TareaData tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        Long idUsuario = tarea.getUsuarioId();
        comprobarUsuarioLogeado(idUsuario);

        tareaData.setId(idTarea);
        tareaData.setUsuarioId(idUsuario);
        tareaService.modificaTareaDesdeDTO(idTarea, tareaData);

        flash.addFlashAttribute("mensaje", "Tarea modificada correctamente");
        return "redirect:/usuarios/" + tarea.getUsuarioId() + "/tareas";
    }

    @DeleteMapping("/tareas/{id}")
    @ResponseBody
    public String borrarTarea(@PathVariable(value="id") Long idTarea, RedirectAttributes flash, HttpSession session) {
        TareaData tarea = tareaService.findById(idTarea);
        if (tarea == null) {
            throw new TareaNotFoundException();
        }

        comprobarUsuarioLogeado(tarea.getUsuarioId());

        tareaService.borraTarea(idTarea);
        return "";
    }
}
