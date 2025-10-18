package persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import persistence.entity.Inventario;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de Inventarios.
 * MÓDULO 1: Control de Material Ingresado
 *
 * Funcionalidades:
 * - Control de stock por bodega y tipo de material
 * - Alertas de stock mínimo y máximo
 * - Seguimiento de capacidad utilizada
 * - Estadísticas de inventario
 *
 * IMPORTANTE: Cada bodega tiene un registro de inventario por tipo de material.
 * No puede haber duplicados de (bodega + tipo_material).
 */
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // ============ CONSULTAS CRÍTICAS PARA MÓDULO 1 ============

    /**
     * Buscar inventario por bodega y tipo de material.
     * CRÍTICO: Esta es la consulta principal del sistema de inventarios.
     * Cada bodega tiene un único registro por tipo de material.
     *
     * @return Optional con el inventario si existe
     */
    @Query("SELECT i FROM Inventario i WHERE i.bodega.id = :bodegaId " +
           "AND i.tipoMaterial.id = :tipoMaterialId")
    Optional<Inventario> findByBodegaAndTipoMaterial(
        @Param("bodegaId") Long bodegaId,
        @Param("tipoMaterialId") Long tipoMaterialId
    );

    /**
     * Listar todos los inventarios de una bodega.
     * Vista completa del stock disponible en una bodega.
     */
    @Query("SELECT i FROM Inventario i WHERE i.bodega.id = :bodegaId " +
           "ORDER BY i.tipoMaterial.nombre ASC")
    List<Inventario> findByBodegaId(@Param("bodegaId") Long bodegaId);

    /**
     * Listar inventarios con stock por debajo del mínimo.
     * ALERTAS: Material que requiere reposición.
     */
    @Query("SELECT i FROM Inventario i WHERE i.stockActual < i.stockMinimo")
    List<Inventario> findInventariosBajoMinimo();

    /**
     * Listar inventarios que exceden el stock máximo.
     * ALERTAS: Material que excede capacidad recomendada.
     */
    @Query("SELECT i FROM Inventario i WHERE i.stockActual > i.stockMaximo")
    List<Inventario> findInventariosSobreMaximo();

    /**
     * Listar inventarios con stock bajo mínimo en una bodega específica.
     * Alertas localizadas por bodega.
     */
    @Query("SELECT i FROM Inventario i WHERE i.bodega.id = :bodegaId " +
           "AND i.stockActual < i.stockMinimo")
    List<Inventario> findInventariosBajoMinimoByBodega(@Param("bodegaId") Long bodegaId);

    /**
     * Obtener stock total de un tipo de material en todas las bodegas.
     * Vista global de disponibilidad de un material específico.
     */
    @Query("SELECT COALESCE(SUM(i.stockActual), 0) FROM Inventario i " +
           "WHERE i.tipoMaterial.id = :tipoMaterialId")
    BigDecimal getStockTotalByTipoMaterial(@Param("tipoMaterialId") Long tipoMaterialId);

    /**
     * Obtener capacidad total utilizada en una bodega.
     * Suma del stock actual de todos los materiales en la bodega.
     * Se usa junto con Bodega.capacidadMaxima para calcular % de ocupación.
     */
    @Query("SELECT COALESCE(SUM(i.stockActual), 0) FROM Inventario i " +
           "WHERE i.bodega.id = :bodegaId")
    BigDecimal getCapacidadUtilizadaByBodega(@Param("bodegaId") Long bodegaId);

    /**
     * Contar tipos de material diferentes en una bodega.
     * Diversidad de material almacenado.
     */
    @Query("SELECT COUNT(i) FROM Inventario i WHERE i.bodega.id = :bodegaId " +
           "AND i.stockActual > 0")
    Long countTiposMaterialConStockByBodega(@Param("bodegaId") Long bodegaId);

    /**
     * Listar inventarios con stock disponible (> 0).
     * Materiales que pueden ser procesados o vendidos.
     */
    @Query("SELECT i FROM Inventario i WHERE i.stockActual > 0 " +
           "ORDER BY i.bodega.nombre ASC, i.tipoMaterial.nombre ASC")
    List<Inventario> findInventariosConStock();
}
