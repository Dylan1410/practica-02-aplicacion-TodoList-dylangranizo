package madstodolist.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;

    @GetMapping("/usuarios/{id}/editar")
    public String editarUsuario(@PathVariable Long id, Model model) {
        UsuarioData usuario = usuarioService.findById(id);
        model.addAttribute("usuario", usuario);
        return "formEditarUsuario";
    }

    @PostMapping("/usuarios/{id}/editar")
    public String guardarCambiosUsuario(@PathVariable Long id,
                                        @ModelAttribute UsuarioData usuarioData,
                                        @RequestParam("foto") MultipartFile archivoFoto,
                                        Model model) {
        try {
            if (!archivoFoto.isEmpty()) {
                String nombreArchivo = archivoFoto.getOriginalFilename();
            
                // Ruta física absoluta en tu sistema
                String rutaFisica = "C:/Users/Dylan/Desktop/Metodologías Agiles/imagperf/" + nombreArchivo;
                Path rutaCompleta = Paths.get(rutaFisica);
            
                // Asegura que los directorios existen
                Files.createDirectories(rutaCompleta.getParent());
            
                // Guarda el archivo físicamente
                archivoFoto.transferTo(rutaCompleta.toFile());
            
                // Asigna la ruta accesible desde el navegador (esto depende de cómo la expongas)
                usuarioData.setFotoUrl("/imagenes/perfiles/" + nombreArchivo);
            }
            

            // Actualiza el usuario y obtiene los datos actualizados
            UsuarioData actualizado = usuarioService.actualizaUsuario(id, usuarioData);

            // Refresca la sesión del usuario con sus datos nuevos
            managerUserSession.logearUsuario(actualizado.getId());

            // Redirige a la lista de tareas
            return "redirect:/usuarios/" + id + "/tareas";

        } catch (Exception e) {
            // En caso de error, vuelve al formulario y muestra un mensaje
            model.addAttribute("usuario", usuarioData);
            model.addAttribute("error", "Error al guardar cambios: " + e.getMessage());
            return "formEditarUsuario";
        }
    }
}
