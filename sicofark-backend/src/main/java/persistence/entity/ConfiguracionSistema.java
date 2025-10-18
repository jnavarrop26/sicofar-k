package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import persistence.converter.TipoDatoConfiguracionConverter;
import persistence.enums.TipoDatoConfiguracion;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "configuracion_sistema", schema = "sicofar")
public class ConfiguracionSistema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "clave", nullable = false, length = 100)
    private String clave;

    @Column(name = "valor", nullable = false, length = Integer.MAX_VALUE)
    private String valor;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "modulo", length = 50)
    private String modulo;

    @Column(name = "tipo_dato", nullable = false, columnDefinition = "tipo_dato_configuracion not null")
    @Convert(converter = TipoDatoConfiguracionConverter.class)
    private TipoDatoConfiguracion tipoDato;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_actualizacion", nullable = false)
    private Instant fechaActualizacion;

    @Column(name = "usuario_actualizacion", length = 50)
    private String usuarioActualizacion;

}