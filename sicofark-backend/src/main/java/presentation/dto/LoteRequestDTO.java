package presentation.dto;

import lombok.Data;
import persistence.enums.CalidadMaterial;

import java.math.BigDecimal;

@Data
public class LoteRequestDTO {
    private BigDecimal pesoBruto;
    private BigDecimal tara;
    private String origen;
    private String observaciones;
    private CalidadMaterial calidad;
    private Long proveedorId;
    private Long tipoMaterialId;
    private Long bodegaId;
    private Long lotePadreId; // Opcional: para lotes subdivididos
}
