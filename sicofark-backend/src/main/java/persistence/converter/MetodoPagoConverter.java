package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.MetodoPago;

/**
 * Converter para mapear el enum MetodoPago de Java al tipo metodo_pago de PostgreSQL.
 */
@Converter(autoApply = true)
public class MetodoPagoConverter implements AttributeConverter<MetodoPago, String> {

    @Override
    public String convertToDatabaseColumn(MetodoPago attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public MetodoPago convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return MetodoPago.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de metodo_pago en la base de datos: " + dbData, e
            );
        }
    }
}
