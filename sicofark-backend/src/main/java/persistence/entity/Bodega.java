package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import persistence.converter.EstadoBodegaConverter;
import persistence.converter.TipoOrganizacionConverter;
import persistence.enums.EstadoBodega;
import persistence.enums.TipoOrganizacion;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "bodega", schema = "sicofar")
public class Bodega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "nit", length = 20)
    private String nit;

    @Column(name = "tipo_organizacion", nullable = false, columnDefinition = "tipo_organizacion")
    @Convert(converter = TipoOrganizacionConverter.class)
    private TipoOrganizacion tipoOrganizacion;

    @Column(name = "coordenadas_latitud", precision = 10, scale = 7)
    private BigDecimal coordenadasLatitud;

    @Column(name = "coordenadas_longitud", precision = 10, scale = 7)
    private BigDecimal coordenadasLongitud;

    @Column(name = "estado", nullable = false, columnDefinition = "estado_bodega")
    @Convert(converter = EstadoBodegaConverter.class)
    private EstadoBodega estado;

    @Column(name = "capacidad_maxima", nullable = false, precision = 12, scale = 2)
    private BigDecimal capacidadMaxima;

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