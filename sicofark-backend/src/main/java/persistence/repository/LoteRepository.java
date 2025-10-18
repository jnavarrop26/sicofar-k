package persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import persistence.entity.Lote;
import persistence.enums.CalidadMaterial;
import persistence.enums.EstadoLote;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de Lotes de Material.
 * MÓDULO 1: Control de Material Ingresado
 * MÓDULO 2: Control de Material Procesado
 *
 * Funcionalidades:
 * - Trazabilidad completa de material (desde ingreso hasta procesamiento)
 * - Control de stock por lote
 * - Gestión de calidad de material
 * - Jerarquía de lotes (lote padre → lotes hijos)
 */
@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {

    // ============ CONSULTAS BÁSICAS ============

    /**
     * Buscar lote por código único.
     * El código identifica de forma única cada lote en el sistema.
     */
    Optional<Lote> findByCodigo(String codigo);

    /**
     * Verificar si existe un código de lote.
     * Validación antes de crear un nuevo lote.
     */
    boolean existsByCodigo(String codigo);

    /**
     * Listar lotes por estado.
     * Estados: DISPONIBLE, EN_PROCESO, PROCESADO, VENDIDO.
     */
    List<Lote> findByEstado(EstadoLote estado);

    // ============ CONSULTAS PARA MÓDULO 1 - INGRESO DE MATERIAL ============

    /**
     * Listar lotes por proveedor.
     * Trazabilidad de material por origen (proveedor).
     */
    @Query("SELECT l FROM Lote l WHERE l.proveedor.id = :proveedorId " +
           "ORDER BY l.fechaRegistro DESC")
    List<Lote> findByProveedorId(@Param("proveedorId") Long proveedorId);

    /**
     * Listar lotes por bodega.
     * Inventario de lotes almacenados en una bodega específica.
     */
    @Query("SELECT l FROM Lote l WHERE l.bodega.id = :bodegaId " +
           "ORDER BY l.fechaRegistro DESC")
    List<Lote> findByBodegaId(@Param("bodegaId") Long bodegaId);

    /**
     * Listar lotes por tipo de material.
     * Filtrar material por tipo (PET, PEAD, PP, etc.).
     */
    @Query("SELECT l FROM Lote l WHERE l.tipoMaterial.id = :tipoMaterialId " +
           "ORDER BY l.fechaRegistro DESC")
    List<Lote> findByTipoMaterialId(@Param("tipoMaterialId") Long tipoMaterialId);

    /**
     * Listar lotes disponibles en bodega por tipo de material.
     * CRÍTICO: Consulta principal para saber qué material hay disponible.
     * Ordena por FIFO (First In, First Out).
     */
    @Query("SELECT l FROM Lote l WHERE l.bodega.id = :bodegaId " +
           "AND l.tipoMaterial.id = :tipoMaterialId " +
           "AND l.estado = :estado " +
           "ORDER BY l.fechaRegistro ASC")
    List<Lote> findLotesDisponiblesEnBodega(
        @Param("bodegaId") Long bodegaId,
        @Param("tipoMaterialId") Long tipoMaterialId,
        @Param("estado") EstadoLote estado
    );

    /**
     * Listar lotes por calidad y estado.
     * Filtrar por calidad del material (ALTA, MEDIA, BAJA).
     */
    @Query("SELECT l FROM Lote l WHERE l.calidad = :calidad " +
           "AND l.estado = :estado " +
           "ORDER BY l.fechaRegistro DESC")
    List<Lote> findByCalidadAndEstado(
        @Param("calidad") CalidadMaterial calidad,
        @Param("estado") EstadoLote estado
    );

    /**
     * Obtener peso total disponible por tipo de material en una bodega.
     * Control de capacidad y disponibilidad de stock.
     */
    @Query("SELECT COALESCE(SUM(l.pesoNeto), 0) FROM Lote l " +
           "WHERE l.bodega.id = :bodegaId " +
           "AND l.tipoMaterial.id = :tipoMaterialId " +
           "AND l.estado = 'DISPONIBLE'")
    BigDecimal getPesoTotalDisponibleByBodegaAndTipo(
        @Param("bodegaId") Long bodegaId,
        @Param("tipoMaterialId") Long tipoMaterialId
    );

    /**
     * Listar lotes hijos de un lote padre.
     * MÓDULO 2: Trazabilidad de procesamiento (descomposición de lotes).
     */
    @Query("SELECT l FROM Lote l WHERE l.lotePadre.id = :lotePadreId " +
           "ORDER BY l.fechaRegistro ASC")
    List<Lote> findLotesHijos(@Param("lotePadreId") Long lotePadreId);

    /**
     * Listar lotes por rango de fechas de registro.
     * Reportes históricos de ingresos.
     */
    @Query("SELECT l FROM Lote l WHERE l.fechaRegistro BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY l.fechaRegistro DESC")
    List<Lote> findByFechaRegistroBetween(
        @Param("fechaInicio") Instant fechaInicio,
        @Param("fechaFin") Instant fechaFin
    );
}
