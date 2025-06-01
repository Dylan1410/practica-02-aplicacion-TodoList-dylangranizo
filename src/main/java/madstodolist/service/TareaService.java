package madstodolist.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import madstodolist.dto.TareaData;
import madstodolist.model.Prioridad;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.repository.TareaRepository;
import madstodolist.repository.UsuarioRepository;

@Service
public class TareaService {

    Logger logger = LoggerFactory.getLogger(TareaService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public TareaData nuevaTareaUsuario(Long idUsuario, String tituloTarea) {
        logger.debug("Añadiendo tarea " + tituloTarea + " al usuario " + idUsuario);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al crear tarea " + tituloTarea);
        }
        Tarea tarea = new Tarea(usuario, tituloTarea);
        tarea.setPrioridad(Prioridad.MEDIA);
        tarea.setCompletada(false);
        tareaRepository.save(tarea);
        return modelMapper.map(tarea, TareaData.class);
    }

    @Transactional
public TareaData nuevaTareaUsuario(Long idUsuario, TareaData tareaData) {
    logger.debug("Añadiendo tarea con DTO al usuario " + idUsuario);
    Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
    if (usuario == null) {
        throw new TareaServiceException("Usuario " + idUsuario + " no existe al crear tarea " + tareaData.getTitulo());
    }
    Tarea tarea = new Tarea(usuario, tareaData.getTitulo());
    tarea.setPrioridad(tareaData.getPrioridad());
    tarea.setFechaLimite(tareaData.getFechaLimite());
    tarea.setDescripcion(tareaData.getDescripcion());
    tarea.setCompletada(false);
    tareaRepository.save(tarea);
    return modelMapper.map(tarea, TareaData.class);
}

    @Transactional(readOnly = true)
    public List<TareaData> allTareasUsuario(Long idUsuario) {
        logger.debug("Devolviendo todas las tareas del usuario " + idUsuario);
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new TareaServiceException("Usuario " + idUsuario + " no existe al listar tareas ");
        }
    
        List<TareaData> tareas = usuario.getTareas().stream()
                .map(tarea -> {
                    TareaData dto = modelMapper.map(tarea, TareaData.class);
                    dto.setCompletada(tarea.isCompletada());
                    return dto;
                })
                .collect(Collectors.toList());
    
        Collections.sort(tareas, (a, b) -> a.getId() < b.getId() ? -1 : a.getId().equals(b.getId()) ? 0 : 1);
    
        return tareas;
    }
    

    @Transactional(readOnly = true)
    public TareaData findById(Long tareaId) {
        logger.debug("Buscando tarea " + tareaId);
        Tarea tarea = tareaRepository.findById(tareaId).orElse(null);
        if (tarea == null) return null;
        return modelMapper.map(tarea, TareaData.class);
    }

    @Transactional
    public TareaData modificaTarea(Long idTarea, String nuevoTitulo) {
        logger.debug("Modificando tarea " + idTarea + " - " + nuevoTitulo);
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setTitulo(nuevoTitulo);
        tarea = tareaRepository.save(tarea);
        return modelMapper.map(tarea, TareaData.class);
    }

    @Transactional
    public TareaData modificaTareaDesdeDTO(Long idTarea, TareaData tareaData) {
        logger.debug("Modificando tarea desde DTO " + idTarea);
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setTitulo(tareaData.getTitulo());
        tarea.setPrioridad(tareaData.getPrioridad());
        tarea.setFechaLimite(tareaData.getFechaLimite());
        tarea.setDescripcion(tareaData.getDescripcion());
        tarea = tareaRepository.save(tarea);
        return modelMapper.map(tarea, TareaData.class);
    }

    @Transactional
    public void borraTarea(Long idTarea) {
        logger.debug("Borrando tarea " + idTarea);
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tareaRepository.delete(tarea);
    }

    @Transactional
    public boolean usuarioContieneTarea(Long usuarioId, Long tareaId) {
        Tarea tarea = tareaRepository.findById(tareaId).orElse(null);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (tarea == null || usuario == null) {
            throw new TareaServiceException("No existe tarea o usuario id");
        }
        return usuario.getTareas().contains(tarea);
    }

    // ✅ NUEVOS MÉTODOS

    @Transactional
    public void actualizarPrioridad(Long idTarea, String prioridad) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setPrioridad(Prioridad.valueOf(prioridad));
        tareaRepository.save(tarea);
    }

    @Transactional
    public void actualizarFechaLimite(Long idTarea, LocalDate fechaLimite) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setFechaLimite(fechaLimite);
        tareaRepository.save(tarea);
    }
    @Transactional
    public void actualizarDescripcion(Long idTarea, String descripcion) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setDescripcion(descripcion);
        tareaRepository.save(tarea);
    }
    @Transactional
public void actualizarEstadoCompletada(Long idTarea, boolean completada) {
    Tarea tarea = tareaRepository.findById(idTarea)
            .orElseThrow(() -> new TareaServiceException("Tarea no encontrada"));
    tarea.setCompletada(completada);
    tareaRepository.save(tarea);
    }
}
