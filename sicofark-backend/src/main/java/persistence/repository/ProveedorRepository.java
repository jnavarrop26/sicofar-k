package persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import persistence.entity.Proveedor;
import persistence.enums.EstadoProveedor;
import persistence.enums.TipoDocumento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de Proveedores.
 * MÓDULO 1: Control de Material Ingresado
 *
 * Funcionalidades:
 * - Gestión de proveedores (búsqueda, validación)
 * - Consultas para calificación y evaluación
 * - Estadísticas de transacciones por proveedor
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // ============ CONSULTAS BÁSICAS ============

    /**
     * Buscar proveedor por número de documento.
     * Útil para validar duplicados y búsqueda rápida.
     */
    Optional<Proveedor> findByNumeroDocumento(String numeroDocumento);

    /**
     * Buscar proveedor por tipo y número de documento.
     * Validación más estricta considerando el tipo de documento.
     */
    Optional<Proveedor> findByTipoDocumentoAndNumeroDocumento(
        TipoDocumento tipoDocumento,
        String numeroDocumento
    );

    /**
     * Listar proveedores por estado.
     * Permite filtrar proveedores activos, inactivos o suspendidos.
     */
    List<Proveedor> findByEstado(EstadoProveedor estado);

    /**
     * Listar proveedores activos ordenados por nombre.
     * Útil para dropdowns y selección de proveedores.
     */
    List<Proveedor> findByEstadoOrderByNombresAsc(EstadoProveedor estado);

    /**
     * Verificar si existe un proveedor por documento.
     * Validación rápida antes de crear un nuevo proveedor.
     */
    boolean existsByNumeroDocumento(String numeroDocumento);

    /**
     * Buscar proveedores por nombre o apellido (búsqueda parcial).
     * Permite búsqueda flexible ignorando mayúsculas/minúsculas.
     */
    @Query("SELECT p FROM Proveedor p WHERE " +
           "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Proveedor> searchByNombreOrApellido(@Param("search") String search);

    // ============ CONSULTAS PARA MÓDULO 1 - INGRESO DE MATERIAL ============

    /**
     * Listar proveedores con calificación superior a un valor.
     * Útil para identificar mejores proveedores y toma de decisiones.
     */
    @Query("SELECT p FROM Proveedor p WHERE p.calificacion >= :minCalificacion " +
           "AND p.estado = 'ACTIVO' ORDER BY p.calificacion DESC")
    List<Proveedor> findTopRatedProveedores(@Param("minCalificacion") BigDecimal minCalificacion);

    /**
     * Contar transacciones por proveedor.
     * Estadística para evaluar frecuencia de entregas.
     */
    @Query("SELECT COUNT(t) FROM Transaccion t WHERE t.proveedor.id = :proveedorId")
    Long countTransaccionesByProveedor(@Param("proveedorId") Long proveedorId);

    /**
     * Obtener peso total entregado por proveedor.
     * Estadística de volumen total entregado (solo transacciones completadas).
     */
    @Query("SELECT COALESCE(SUM(t.pesoTotal), 0) FROM Transaccion t " +
           "WHERE t.proveedor.id = :proveedorId AND t.estado = 'COMPLETADA'")
    BigDecimal getTotalPesoEntregadoByProveedor(@Param("proveedorId") Long proveedorId);

    /**
     * Obtener proveedores con transacciones pendientes.
     * Útil para seguimiento de entregas pendientes.
     */
    @Query("SELECT DISTINCT p FROM Proveedor p " +
           "JOIN Transaccion t ON t.proveedor.id = p.id " +
           "WHERE t.estado = 'PENDIENTE'")
    List<Proveedor> findProveedoresConTransaccionesPendientes();
}
