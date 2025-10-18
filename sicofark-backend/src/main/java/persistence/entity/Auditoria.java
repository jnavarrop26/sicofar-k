package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import persistence.converter.AccionAuditoriaConverter;
import persistence.enums.AccionAuditoria;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "auditoria", schema = "sicofar")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "accion", nullable = false, columnDefinition = "accion_auditoria")
    @Convert(converter = AccionAuditoriaConverter.class)
    private AccionAuditoria accion;

    @Column(name = "tabla_afectada", nullable = false, length = 50)
    private String tablaAfectada;

    @Column(name = "registro_id", nullable = false)
    private Long registroId;

    @Column(name = "valores_anteriores", length = Integer.MAX_VALUE)
    private String valoresAnteriores;

    @Column(name = "valores_nuevos", length = Integer.MAX_VALUE)
    private String valoresNuevos;

    @Column(name = "motivo_cambio")
    private String motivoCambio;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "hash_integridad", length = 64)
    private String hashIntegridad;

}