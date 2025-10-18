package persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import persistence.entity.TipoMaterial;
import persistence.enums.CategoriaMaterial;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión del Catálogo de Tipos de Material.
 * MÓDULO 1: Control de Material Ingresado
 *
 * Funcionalidades:
 * - Catálogo de materiales reciclables (PET, PEAD, PP, PVC, etc.)
 * - Gestión de precios base por tipo de material
 * - Clasificación por categoría
 * - Control de materiales activos/inactivos
 */
@Repository
public interface TipoMaterialRepository extends JpaRepository<TipoMaterial, Long> {

    // ============ CONSULTAS BÁSICAS ============

    /**
     * Buscar tipo de material por nombre.
     * Útil para validar duplicados y búsqueda directa.
     */
    Optional<TipoMaterial> findByNombre(String nombre);

    /**
     * Listar tipos de material activos ordenados alfabéticamente.
     * Para dropdowns y selección de materiales en formularios.
     */
    List<TipoMaterial> findByActivoTrueOrderByNombreAsc();

    /**
     * Listar tipos de material por categoría.
     * Clasificación: PLASTICO_RIGIDO, PLASTICO_FLEXIBLE, MIXTO.
     */
    List<TipoMaterial> findByCategoriaMaterial(CategoriaMaterial categoria);

    /**
     * Listar tipos de material activos por categoría.
     * Filtro combinado de categoría y estado activo.
     */
    @Query("SELECT tm FROM TipoMaterial tm WHERE tm.categoriaMaterial = :categoria " +
           "AND tm.activo = true ORDER BY tm.nombre ASC")
    List<TipoMaterial> findActivosByCategoria(@Param("categoria") CategoriaMaterial categoria);

    /**
     * Verificar si existe un tipo de material por nombre.
     * Validación antes de crear un nuevo tipo.
     */
    boolean existsByNombre(String nombre);

    // ============ CONSULTAS PARA MÓDULO 1 - INGRESO DE MATERIAL ============

    /**
     * Buscar tipos de material con precio base mayor o igual a un valor.
     * Útil para reportes de materiales de alto valor.
     */
    @Query("SELECT tm FROM TipoMaterial tm WHERE tm.precioBase >= :precioMinimo " +
           "AND tm.activo = true ORDER BY tm.precioBase DESC")
    List<TipoMaterial> findByPrecioBaseGreaterThanEqual(@Param("precioMinimo") BigDecimal precioMinimo);

    /**
     * Listar todos los tipos de material (incluyendo inactivos).
     * Para administración y reportes históricos.
     */
    @Query("SELECT tm FROM TipoMaterial tm ORDER BY tm.nombre ASC")
    List<TipoMaterial> findAllOrderByNombre();
}
