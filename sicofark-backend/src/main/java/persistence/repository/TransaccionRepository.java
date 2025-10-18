package persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import persistence.entity.Transaccion;
import persistence.enums.EstadoTransaccion;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de Transacciones (Ingresos de Material).
 * MÓDULO 1: Control de Material Ingresado
 *
 * Funcionalidades:
 * - Registro y seguimiento de ingresos de material
 * - Control de pagos a proveedores
 * - Reportes y estadísticas de compras
 * - Trazabilidad de material ingresado por bodega
 */
@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    // ============ CONSULTAS BÁSICAS ============

    /**
     * Buscar transacción por consecutivo único.
     * El consecutivo es el identificador de negocio de la transacción.
     */
    Optional<Transaccion> findByConsecutivo(String consecutivo);

    /**
     * Verificar si existe un consecutivo.
     * Validación antes de crear una nueva transacción.
     */
    boolean existsByConsecutivo(String consecutivo);

    /**
     * Listar transacciones por estado.
     * Permite filtrar por PENDIENTE, COMPLETADA, CANCELADA.
     */
    List<Transaccion> findByEstado(EstadoTransaccion estado);

    // ============ CONSULTAS PARA MÓDULO 1 - INGRESO DE MATERIAL ============

    /**
     * Listar transacciones por proveedor.
     * Historial completo de ingresos de un proveedor específico.
     */
    @Query("SELECT t FROM Transaccion t WHERE t.proveedor.id = :proveedorId " +
           "ORDER BY t.fecha DESC")
    List<Transaccion> findByProveedorId(@Param("proveedorId") Long proveedorId);

    /**
     * Listar transacciones por bodega.
     * Historial de ingresos en una bodega específica.
     */
    @Query("SELECT t FROM Transaccion t WHERE t.bodega.id = :bodegaId " +
           "ORDER BY t.fecha DESC")
    List<Transaccion> findByBodegaId(@Param("bodegaId") Long bodegaId);

    /**
     * Listar transacciones por usuario (quien registró).
     * Auditoría de operaciones por usuario.
     */
    @Query("SELECT t FROM Transaccion t WHERE t.usuario.id = :usuarioId " +
           "ORDER BY t.fecha DESC")
    List<Transaccion> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Listar transacciones por rango de fechas.
     * Reportes periódicos de ingresos.
     */
    @Query("SELECT t FROM Transaccion t WHERE t.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY t.fecha DESC")
    List<Transaccion> findByFechaBetween(
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    /**
     * Listar transacciones pendientes de pago.
     * Control de cuentas por pagar a proveedores.
     */
    @Query("SELECT t FROM Transaccion t WHERE t.estado = 'PENDIENTE' " +
           "AND t.fechaPago IS NULL ORDER BY t.fecha ASC")
    List<Transaccion> findTransaccionesPendientesPago();

    /**
     * Obtener total de compras por proveedor en un período.
     * Estadística financiera para evaluación de proveedores.
     */
    @Query("SELECT COALESCE(SUM(t.valorTotal), 0) FROM Transaccion t " +
           "WHERE t.proveedor.id = :proveedorId " +
           "AND t.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "AND t.estado = 'COMPLETADA'")
    BigDecimal getTotalComprasByProveedorInPeriod(
        @Param("proveedorId") Long proveedorId,
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );


    /**
     * Obtener peso total ingresado por bodega en un período.
     * Control de volumen de material recibido.
     */
    @Query("SELECT COALESCE(SUM(t.pesoTotal), 0) FROM Transaccion t " +
           "WHERE t.bodega.id = :bodegaId " +
           "AND t.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal getPesoTotalIngresadoByBodegaInPeriod(
        @Param("bodegaId") Long bodegaId,
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );

    /**
     * Contar transacciones por estado en una bodega.
     * Estadística operacional de la bodega.
     */
    @Query("SELECT COUNT(t) FROM Transaccion t " +
           "WHERE t.bodega.id = :bodegaId AND t.estado = :estado")
    Long countByBodegaAndEstado(
        @Param("bodegaId") Long bodegaId,
        @Param("estado") EstadoTransaccion estado
    );
}
