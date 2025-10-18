package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "\"monitoreo_tamaño\"", schema = "sicofar")
public class MonitoreoTamaño {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

    @Column(name = "tabla", nullable = false, length = 100)
    private String tabla;

    @Column(name = "\"tamaño_bytes\"", nullable = false)
    private Long tamañoBytes;

    @Column(name = "cantidad_filas", nullable = false)
    private Long cantidadFilas;

}