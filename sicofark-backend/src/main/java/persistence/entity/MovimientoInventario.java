package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import persistence.converter.TipoOperacionConverter;
import persistence.enums.TipoOperacion;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "movimiento_inventario", schema = "sicofar")
public class MovimientoInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "tipo_movimiento", nullable = false, columnDefinition = "tipo_movimiento")
    @Convert(converter = TipoOperacionConverter.class)
    private TipoOperacion tipoOperacion;

    @Column(name = "cantidad", nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "cantidad_anterior", nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidadAnterior;

    @Column(name = "cantidad_nueva", nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidadNueva;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "inventario_id", nullable = false)
    private Inventario inventario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

}