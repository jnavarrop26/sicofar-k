package persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import persistence.enums.EstadoUsuario;

/**
 * Converter para mapear el enum EstadoUsuario de Java al tipo estado_usuario de PostgreSQL.
 * autoApply = true hace que se aplique automáticamente a todos los campos de tipo EstadoUsuario.
 */
@Converter(autoApply = true)
public class EstadoUsuarioConverter implements AttributeConverter<EstadoUsuario, String> {

    /**
     * Convierte el enum Java a String para almacenar en la base de datos.
     * @param attribute el valor del enum EstadoUsuario
     * @return el nombre del enum como String (ej: "ACTIVO")
     */
    @Override
    public String convertToDatabaseColumn(EstadoUsuario attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    /**
     * Convierte el String de la base de datos al enum Java.
     * @param dbData el valor almacenado en PostgreSQL
     * @return el enum EstadoUsuario correspondiente
     * @throws IllegalStateException si el valor no existe en el enum
     */
    @Override
    public EstadoUsuario convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return EstadoUsuario.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                "Valor desconocido de estado_usuario en la base de datos: " + dbData, e
            );
        }
    }
}
