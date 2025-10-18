package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import persistence.converter.CalidadMaterialConverter;
import persistence.converter.EstadoLoteConverter;
import persistence.enums.CalidadMaterial;
import persistence.enums.EstadoLote;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "lote", schema = "sicofar")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "peso_bruto", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoBruto;

    @Column(name = "tara", nullable = false, precision = 12, scale = 2)
    private BigDecimal tara;

    @Column(name = "peso_neto", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoNeto;

    @Column(name = "origen", length = 100)
    private String origen;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    @Column(name = "estado", nullable = false, columnDefinition = "estado_lote")
    @Convert(converter = EstadoLoteConverter.class)
    private EstadoLote estado;

    @Column(name = "calidad", nullable = false, columnDefinition = "calidad_material")
    @Convert(converter = CalidadMaterialConverter.class)
    private CalidadMaterial calidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "tipo_material_id", nullable = false)
    private TipoMaterial tipoMaterial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "bodega_id", nullable = false)
    private Bodega bodega;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "usuario_registro_id", nullable = false)
    private Usuario usuarioRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "lote_padre_id")
    private Lote lotePadre;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_actualizacion", nullable = false)
    private Instant fechaActualizacion;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

    @Column(name = "usuario_actualizacion", length = 50)
    private String usuarioActualizacion;

}