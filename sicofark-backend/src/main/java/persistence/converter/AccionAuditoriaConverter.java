package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.AccionAuditoria;

/**
 * Converter para mapear el enum AccionAuditoria de Java al tipo accion_auditoria de PostgreSQL.
 */
@Converter(autoApply = true)
public class AccionAuditoriaConverter implements AttributeConverter<AccionAuditoria, String> {

    @Override
    public String convertToDatabaseColumn(AccionAuditoria attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public AccionAuditoria convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return AccionAuditoria.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de accion_auditoria en la base de datos: " + dbData, e
            );
        }
    }
}
