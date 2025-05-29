package madstodolist.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import madstodolist.model.Prioridad;

public class TareaData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String titulo;
    private Long usuarioId;
    private Prioridad prioridad;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaLimite;

    // ===== NUEVO MÉTODO =====
    public boolean isFechaLimiteProxima() {
        if (fechaLimite == null) return false;
    
        LocalDate hoy = LocalDate.now();
        long dias = ChronoUnit.DAYS.between(hoy, fechaLimite);
    
        return dias >= 0 && dias <= 2;
    }
    // Getters y Setters
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

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    // equals y hashCode por ID
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
