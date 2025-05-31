package madstodolist.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // ✅ IMPORT CORRECTO
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
                                        @RequestParam("foto") MultipartFile archivoFoto) throws IOException {

        if (!archivoFoto.isEmpty()) {
            String ruta = "/img/usuarios/" + archivoFoto.getOriginalFilename();
            Path rutaCompleta = Paths.get("src/main/resources/static" + ruta);
            Files.createDirectories(rutaCompleta.getParent());
            archivoFoto.transferTo(rutaCompleta.toFile());
            usuarioData.setFotoUrl(ruta);
        }

        usuarioService.actualizaUsuario(id, usuarioData);
        managerUserSession.logearUsuario(usuarioData.getId());
        return "redirect:/usuarios/" + id + "/tareas";
    }
}
