package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.EstadoProveedor;

/**
 * Converter para mapear el enum EstadoProveedor de Java al tipo estado_proveedor de PostgreSQL.
 */
@Converter(autoApply = true)
public class EstadoProveedorConverter implements AttributeConverter<EstadoProveedor, String> {

    @Override
    public String convertToDatabaseColumn(EstadoProveedor attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EstadoProveedor convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return EstadoProveedor.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de estado_proveedor en la base de datos: " + dbData, e
            );
        }
    }
}
