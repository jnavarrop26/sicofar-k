package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import persistence.converter.EstadoAlertaConverter;
import persistence.converter.SeveridadAlertaConverter;
import persistence.converter.TipoAlertaConverter;
import persistence.enums.EstadoAlerta;
import persistence.enums.SeveridadAlerta;
import persistence.enums.TipoAlerta;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "alerta", schema = "sicofar")
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "tipo", columnDefinition = "tipo_alerta not null")
    @Convert(converter = TipoAlertaConverter.class)
    private TipoAlerta tipo;

    @ColumnDefault("'BAJA'")
    @Column(name = "severidad", columnDefinition = "severidad_alerta not null")
    @Convert(converter = SeveridadAlertaConverter.class)
    private SeveridadAlerta severidad;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "descripcion", nullable = false, length = Integer.MAX_VALUE)
    private String descripcion;

    @ColumnDefault("'CERRADA'")
    @Column(name = "estado", columnDefinition = "estado_alerta not null")
    @Convert(converter = EstadoAlertaConverter.class)
    private EstadoAlerta estado;

    @Column(name = "entidad_tipo", length = 50)
    private String entidadTipo;

    @Column(name = "entidad_id")
    private Long entidadId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_generacion", nullable = false)
    private Instant fechaGeneracion;

    @Column(name = "fecha_reconocimiento")
    private Instant fechaReconocimiento;

    @Column(name = "fecha_cierre")
    private Instant fechaCierre;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "usuario_reconocimiento_id")
    private Usuario usuarioReconocimientoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "usuario_cierre_id")
    private Usuario usuarioCierreId;


    @ColumnDefault("1")
    @Column(name = "contador", nullable = false)
    private Integer contador;


}




