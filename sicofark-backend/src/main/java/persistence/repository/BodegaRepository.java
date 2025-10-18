package persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import persistence.entity.Bodega;
import persistence.enums.EstadoBodega;
import persistence.enums.TipoOrganizacion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de Bodegas (Centros de Acopio).
 * MÓDULO 1: Control de Material Ingresado
 *
 * Funcionalidades:
 * - Gestión de bodegas y centros de acopio
 * - Control de capacidad de almacenamiento
 * - Clasificación por tipo de organización
 * - Geolocalización de bodegas
 *
 * @author Jose Navarro
 */
@Repository
public interface BodegaRepository extends JpaRepository<Bodega, Long> {

    // ============ CONSULTAS BÁSICAS ============

    /**
     * Buscar bodega por nombre.
     * Útil para validación y búsqueda directa.
     */
    Optional<Bodega> findByNombre(String nombre);

    /**
     * Buscar bodega por NIT.
     * Identificación fiscal única de la bodega.
     */
    Optional<Bodega> findByNit(String nit);

    /**
     * Listar bodegas por estado.
     * Estados: ACTIVA, INACTIVA, MANTENIMIENTO.
     */
    List<Bodega> findByEstado(EstadoBodega estado);

    /**
     * Listar bodegas activas ordenadas por nombre.
     * Para dropdowns y selección de bodegas.
     */
    List<Bodega> findByEstadoOrderByNombreAsc(EstadoBodega estado);

    /**
     * Listar bodegas por tipo de organización.
     * Tipos: PUBLICA, PRIVADA, MIXTA, ONG.
     */
    List<Bodega> findByTipoOrganizacion(TipoOrganizacion tipo);

    /**
     * Verificar existencia por NIT.
     * Validación antes de crear una nueva bodega.
     */
    boolean existsByNit(String nit);

    // ============ CONSULTAS PARA MÓDULO 1 - CONTROL DE CAPACIDAD ============

    /**
     * Obtener capacidad disponible de una bodega.
     * Capacidad Disponible = Capacidad Máxima - Stock Actual Total
     *
     * IMPORTANTE: Esta consulta calcula en tiempo real cuánto espacio libre tiene la bodega.
     */
    @Query("SELECT (b.capacidadMaxima - COALESCE(SUM(i.stockActual), 0)) " +
           "FROM Bodega b " +
           "LEFT JOIN Inventario i ON i.bodega.id = b.id " +
           "WHERE b.id = :bodegaId " +
           "GROUP BY b.capacidadMaxima")
    BigDecimal getCapacidadDisponible(@Param("bodegaId") Long bodegaId);

    /**
     * Listar bodegas con capacidad disponible mayor o igual a un valor.
     * Útil para encontrar dónde almacenar una nueva transacción.
     *
     * @param capacidadRequerida kg de material que se necesita almacenar
     * @return Lista de bodegas activas con capacidad suficiente
     */
    @Query("SELECT b FROM Bodega b WHERE " +
           "(b.capacidadMaxima - COALESCE(" +
           "   (SELECT SUM(i.stockActual) FROM Inventario i WHERE i.bodega.id = b.id), 0" +
           ")) >= :capacidadRequerida " +
           "AND b.estado = 'ACTIVA'")
    List<Bodega> findBodegasConCapacidadDisponible(@Param("capacidadRequerida") BigDecimal capacidadRequerida);

    /**
     * Obtener porcentaje de ocupación de una bodega.
     * % Ocupación = (Stock Actual Total / Capacidad Máxima) * 100
     */
    @Query("SELECT (COALESCE(SUM(i.stockActual), 0) / b.capacidadMaxima) * 100 " +
           "FROM Bodega b " +
           "LEFT JOIN Inventario i ON i.bodega.id = b.id " +
           "WHERE b.id = :bodegaId " +
           "GROUP BY b.capacidadMaxima")
    BigDecimal getPorcentajeOcupacion(@Param("bodegaId") Long bodegaId);

    /**
     * Listar bodegas por estado y con coordenadas registradas.
     * Para mapas y visualización geográfica.
     */
    @Query("SELECT b FROM Bodega b WHERE b.estado = :estado " +
           "AND b.coordenadasLatitud IS NOT NULL " +
           "AND b.coordenadasLongitud IS NOT NULL")
    List<Bodega> findBodegasConCoordenadas(@Param("estado") EstadoBodega estado);
}
