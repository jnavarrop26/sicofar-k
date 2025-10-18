package persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import persistence.entity.MovimientoInventario;
import persistence.enums.TipoOperacion;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Repositorio para la gestión de Movimientos de Inventario.
 * MÓDULO 1: Control de Material Ingresado
 * MÓDULO 2: Control de Material Procesado
 *
 * Funcionalidades:
 * - Auditoría completa de movimientos de stock (ENTRADA, SALIDA, AJUSTE)
 * - Trazabilidad de cambios en inventario
 * - Historial de operaciones por usuario
 * - Control de lotes asociados a movimientos
 */
@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    // ============ CONSULTAS PARA MÓDULO 1 Y 2 - AUDITORÍA DE INVENTARIO ============

    /**
     * Listar movimientos por inventario específico.
     * Historial completo de cambios en un inventario (bodega + tipo material).
     */
    @Query("SELECT m FROM MovimientoInventario m WHERE m.inventario.id = :inventarioId " +
           "ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByInventarioId(@Param("inventarioId") Long inventarioId);

    /**
     * Listar movimientos por tipo de operación.
     * Tipos: ENTRADA (compra), SALIDA (venta/proceso), AJUSTE (corrección).
     */
    @Query("SELECT m FROM MovimientoInventario m WHERE m.tipoOperacion = :tipoOperacion " +
           "ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByTipoOperacion(@Param("tipoOperacion") TipoOperacion tipoOperacion);

    /**
     * Listar movimientos por usuario.
     * Auditoría de operaciones realizadas por un usuario específico.
     */
    @Query("SELECT m FROM MovimientoInventario m WHERE m.usuario.id = :usuarioId " +
           "ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Listar movimientos por lote.
     * Trazabilidad de movimientos asociados a un lote específico.
     */
    @Query("SELECT m FROM MovimientoInventario m WHERE m.lote.id = :loteId " +
           "ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByLoteId(@Param("loteId") Long loteId);

    /**
     * Listar movimientos por rango de fechas.
     * Reportes periódicos de actividad de inventario.
     */
    @Query("SELECT m FROM MovimientoInventario m " +
           "WHERE m.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByFechaBetween(
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    /**
     * Listar movimientos por inventario y rango de fechas.
     * Historial específico de un inventario en un período.
     */
    @Query("SELECT m FROM MovimientoInventario m " +
           "WHERE m.inventario.id = :inventarioId " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY m.fecha DESC")
    List<MovimientoInventario> findByInventarioIdAndFechaBetween(
        @Param("inventarioId") Long inventarioId,
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    /**
     * Obtener suma de entradas en un inventario en un período.
     * Total de material ingresado.
     */
    @Query("SELECT COALESCE(SUM(m.cantidad), 0) FROM MovimientoInventario m " +
           "WHERE m.inventario.id = :inventarioId " +
           "AND m.tipoOperacion = 'ENTRADA' " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal getSumaEntradasByInventarioInPeriod(
        @Param("inventarioId") Long inventarioId,
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    /**
     * Obtener suma de salidas en un inventario en un período.
     * Total de material que ha salido (ventas o procesamiento).
     */
    @Query("SELECT COALESCE(SUM(m.cantidad), 0) FROM MovimientoInventario m " +
           "WHERE m.inventario.id = :inventarioId " +
           "AND m.tipoOperacion = 'SALIDA' " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal getSumaSalidasByInventarioInPeriod(
        @Param("inventarioId") Long inventarioId,
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    /**
     * Contar movimientos por tipo de operación en un período.
     * Estadística de actividad operacional.
     */
    @Query("SELECT COUNT(m) FROM MovimientoInventario m " +
           "WHERE m.tipoOperacion = :tipoOperacion " +
           "AND m.fecha BETWEEN :fechaInicio AND :fechaFin")
    Long countByTipoOperacionInPeriod(
        @Param("tipoOperacion") TipoOperacion tipoOperacion,
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    /**
     * Listar últimos N movimientos globales.
     * Vista reciente de actividad del sistema.
     */
    @Query("SELECT m FROM MovimientoInventario m ORDER BY m.fecha DESC")
    List<MovimientoInventario> findTopMovimientosRecientes();
}
