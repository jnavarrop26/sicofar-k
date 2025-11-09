package presentation.dto;

import lombok.Data;
import persistence.enums.CalidadMaterial;
import persistence.enums.EstadoLote;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class LoteResponseDTO {
    private Long id;
    private String codigo;
    private BigDecimal pesoBruto;
    private BigDecimal tara;
    private BigDecimal pesoNeto;
    private String origen;
    private String observaciones;
    private EstadoLote estado;
    private CalidadMaterial calidad;

    // Información de relaciones (nested DTOs simplificados)
    private Long proveedorId;
    private String proveedorNombre;
    private Long tipoMaterialId;
    private String tipoMaterialNombre;
    private Long bodegaId;
    private String bodegaNombre;
    private Long usuarioRegistroId;
    private String usuarioRegistroNombre;
    private Long lotePadreId;

    private Instant fechaRegistro;
    private Instant fechaActualizacion;
}
