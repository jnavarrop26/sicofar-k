package persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import persistence.entity.EtapaProcesamiento;
import persistence.enums.TipoEtapa;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Repositorio para la gestión de Etapas de Procesamiento.
 * MÓDULO 2: Control de Material Procesado
 *
 * Funcionalidades:
 * - Seguimiento de procesamiento de lotes (clasificación, lavado, triturado, etc.)
 * - Control de merma en cada etapa
 * - Trazabilidad completa del proceso productivo
 * - Estadísticas de eficiencia por etapa
 */
@Repository
public interface EtapaProcesamientoRepository extends JpaRepository<EtapaProcesamiento, Long> {

    // ============ CONSULTAS PARA MÓDULO 2 - PROCESAMIENTO ============

    /**
     * Listar todas las etapas de un lote ordenadas cronológicamente.
     * TRAZABILIDAD: Historia completa del procesamiento de un lote.
     */
    @Query("SELECT e FROM EtapaProcesamiento e WHERE e.lote.id = :loteId " +
           "ORDER BY e.fechaInicio ASC")
    List<EtapaProcesamiento> findByLoteIdOrderByFechaInicio(@Param("loteId") Long loteId);

    /**
     * Listar etapas por tipo.
     * Tipos: CLASIFICACION, LAVADO, SECADO, TRITURADO, PELETIZADO.
     */
    @Query("SELECT e FROM EtapaProcesamiento e WHERE e.tipoEtapa = :tipoEtapa " +
           "ORDER BY e.fechaInicio DESC")
    List<EtapaProcesamiento> findByTipoEtapa(@Param("tipoEtapa") TipoEtapa tipoEtapa);

    /**
     * Obtener merma total acumulada de un lote.
     * IMPORTANTE: La última etapa tiene la merma acumulada total del proceso.
     */
    @Query("SELECT COALESCE(MAX(e.mermaAcumulada), 0) FROM EtapaProcesamiento e " +
           "WHERE e.lote.id = :loteId")
    BigDecimal getMermaAcumuladaByLote(@Param("loteId") Long loteId);

    /**
     * Listar etapas por rango de fechas.
     * Reportes de producción en un período.
     */
    @Query("SELECT e FROM EtapaProcesamiento e " +
           "WHERE e.fechaInicio BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY e.fechaInicio DESC")
    List<EtapaProcesamiento> findByFechaBetween(
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    /**
     * Obtener promedio de merma parcial por tipo de etapa.
     * ESTADÍSTICA: Identificar qué etapas generan más merma.
     */
    @Query("SELECT AVG(e.mermaParcial) FROM EtapaProcesamiento e " +
           "WHERE e.tipoEtapa = :tipoEtapa")
    BigDecimal getPromedioMermaByTipoEtapa(@Param("tipoEtapa") TipoEtapa tipoEtapa);

    /**
     * Contar etapas completadas por tipo.
     * Control de volumen procesado por tipo de etapa.
     */
    @Query("SELECT COUNT(e) FROM EtapaProcesamiento e " +
           "WHERE e.tipoEtapa = :tipoEtapa")
    Long countByTipoEtapa(@Param("tipoEtapa") TipoEtapa tipoEtapa);

    /**
     * Listar etapas por usuario que las registró.
     * Auditoría de operaciones por operador.
     */
    @Query("SELECT e FROM EtapaProcesamiento e WHERE e.usuarioRegistro.id = :usuarioId " +
           "ORDER BY e.fechaInicio DESC")
    List<EtapaProcesamiento> findByUsuarioRegistroId(@Param("usuarioId") Long usuarioId);

    /**
     * Obtener peso total procesado (peso salida) por tipo de etapa en un período.
     * Producción total por tipo de proceso.
     */
    @Query("SELECT COALESCE(SUM(e.pesoSalida), 0) FROM EtapaProcesamiento e " +
           "WHERE e.tipoEtapa = :tipoEtapa " +
           "AND e.fechaInicio BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal getPesoTotalProcesadoByTipoInPeriod(
        @Param("tipoEtapa") TipoEtapa tipoEtapa,
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    /**
     * Listar etapas con merma parcial superior a un umbral.
     * ALERTAS: Etapas con pérdida excesiva de material.
     */
    @Query("SELECT e FROM EtapaProcesamiento e WHERE e.mermaParcial > :umbralMerma " +
           "ORDER BY e.mermaParcial DESC")
    List<EtapaProcesamiento> findEtapasConMermaExcesiva(@Param("umbralMerma") BigDecimal umbralMerma);
}
