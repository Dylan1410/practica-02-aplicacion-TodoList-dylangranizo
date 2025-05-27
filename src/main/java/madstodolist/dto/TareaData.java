package madstodolist.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import madstodolist.model.Prioridad;

// Data Transfer Object para la clase Tarea
public class TareaData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String titulo;
    private Long usuarioId; // ID del usuario asociado
    private Prioridad prioridad; // NUEVO CAMPO

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    // equals y hashCode basados en id

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TareaData)) return false;
        TareaData tareaData = (TareaData) o;
        return Objects.equals(id, tareaData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
