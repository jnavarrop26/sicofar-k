package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import persistence.converter.EstadoTransaccionConverter;
import persistence.converter.MetodoPagoConverter;
import persistence.enums.EstadoTransaccion;
import persistence.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "transaccion", schema = "sicofar")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "consecutivo", nullable = false, length = 50)
    private String consecutivo;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "peso_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoTotal;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    @ColumnDefault("'PENDIENTE'")
    @Column(name = "estado", columnDefinition = "estado_transaccion not null")
    @Convert(converter = EstadoTransaccionConverter.class)
    private EstadoTransaccion estado;

    @Column(name = "metodo_pago", columnDefinition = "metodo_pago")
    @Convert(converter = MetodoPagoConverter.class)
    private MetodoPago metodoPago;

    @Column(name = "fecha_pago")
    private Instant fechaPago;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "bodega_id", nullable = false)
    private Bodega bodega;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_actualizacion", nullable = false)
    private Instant fechaActualizacion;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @Column(name = "usuario_actualizacion", length = 50)
    private String usuarioActualizacion;



}