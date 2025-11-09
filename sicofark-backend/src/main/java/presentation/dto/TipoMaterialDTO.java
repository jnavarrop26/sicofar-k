package presentation.dto;

import lombok.Data;
import persistence.enums.CategoriaMaterial;
import persistence.enums.UnidadMedida;

import java.math.BigDecimal;

@Data
public class TipoMaterialDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private CategoriaMaterial categoriaMaterial;
    private UnidadMedida unidadMedida;
    private BigDecimal precioBase;
    private BigDecimal factorCalidad;
    private BigDecimal umbralMerma;
    private Boolean activo;
}
