package persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import persistence.converter.TipoEtapaConverter;
import persistence.enums.TipoEtapa;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "etapa_procesamiento", schema = "sicofar")
public class EtapaProcesamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tipo_etapa", nullable = false, columnDefinition = "tipo_etapa")
    @Convert(converter = TipoEtapaConverter.class)
    private TipoEtapa tipoEtapa;

    @Column(name = "fecha_inicio", nullable = false)
    private Instant fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private Instant fechaFin;

    @Column(name = "peso_entrada", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoEntrada;

    @Column(name = "peso_salida", nullable = false, precision = 12, scale = 2)
    private BigDecimal pesoSalida;

    @Column(name = "merma_parcial", nullable = false, precision = 5, scale = 2)
    private BigDecimal mermaParcial;

    @Column(name = "merma_acumulada", nullable = false, precision = 5, scale = 2)
    private BigDecimal mermaAcumulada;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "usuario_registro_id", nullable = false)
    private Usuario usuarioRegistro;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

}