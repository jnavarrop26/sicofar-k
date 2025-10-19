package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import persistence.converter.CategoriaMaterialConverter;
import persistence.converter.UnidadMedidaConverter;
import persistence.enums.CategoriaMaterial;
import persistence.enums.UnidadMedida;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tipo_material", schema = "sicofar")
public class TipoMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "categoria", nullable = false, columnDefinition = "categoria_material")
    @Convert(converter = CategoriaMaterialConverter.class)
    private CategoriaMaterial categoriaMaterial;

    @ColumnDefault("'KILOGRAMO'")
    @Column(name = "unidad_medida", columnDefinition = "unidad_medida not null")
    @Convert(converter = UnidadMedidaConverter.class)
    private UnidadMedida unidadMedida;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;

    @ColumnDefault("1.00")
    @Column(name = "factor_calidad", nullable = false, precision = 5, scale = 2)
    private BigDecimal factorCalidad;

    @ColumnDefault("5.00")
    @Column(name = "umbral_merma", precision = 5, scale = 2)
    private BigDecimal umbralMerma;

    @ColumnDefault("true")
    @Column(name = "activo", nullable = false)
    private Boolean activo = false;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @Column(name = "usuario_creacion", length = 50)
    private String usuarioCreacion;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_actualizacion", nullable = false)
    private Instant fechaActualizacion;

    @Column(name = "usuario_actualizacion", length = 50)
    private String usuarioActualizacion;


}