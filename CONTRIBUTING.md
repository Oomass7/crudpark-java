# Guía de Contribución - CrudPark Java

## 🤝 Cómo Contribuir

Gracias por tu interés en contribuir a CrudPark Java. Esta guía te ayudará a entender cómo puedes participar en el desarrollo del proyecto.

## 📋 Código de Conducta

- Sé respetuoso con todos los colaboradores
- Acepta críticas constructivas
- Enfócate en lo mejor para el proyecto
- Mantén un ambiente profesional y amigable

## 🔧 Configuración del Entorno de Desarrollo

### Requisitos

1. Java JDK 17+
2. Maven 3.6+
3. Git
4. IDE (IntelliJ IDEA, Eclipse, o VS Code)
5. PostgreSQL 12+

### Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd crudpark-java
```

### Configurar la Base de Datos

```bash
# Crear base de datos
createdb crudpark

# Ejecutar esquema
psql -d crudpark -f resources/schema.sql
```

### Configurar el Proyecto

```bash
# Copiar configuración
cp config.properties.example config.properties

# Editar con tus credenciales
nano config.properties

# Compilar
mvn clean install
```

## 🌿 Flujo de Trabajo con Git

### Crear una Rama

```bash
# Actualizar main
git checkout main
git pull origin main

# Crear rama para tu feature
git checkout -b feature/nombre-descriptivo
```

### Nomenclatura de Ramas

- `feature/`: Nuevas funcionalidades
- `bugfix/`: Corrección de bugs
- `hotfix/`: Correcciones urgentes
- `refactor/`: Refactorización de código
- `docs/`: Documentación

Ejemplos:
- `feature/agregar-reportes`
- `bugfix/corregir-calculo-tarifa`
- `refactor/optimizar-dao`

### Hacer Commits

```bash
# Agregar cambios
git add .

# Commit con mensaje descriptivo
git commit -m "feat: agregar validación de placas duplicadas"
```

### Convención de Mensajes de Commit

Usar el formato:
```
tipo: descripción breve

Descripción detallada (opcional)
```

**Tipos:**
- `feat`: Nueva funcionalidad
- `fix`: Corrección de bug
- `docs`: Documentación
- `style`: Formato, sin cambios de código
- `refactor`: Refactorización
- `test`: Agregar o modificar tests
- `chore`: Tareas de mantenimiento

**Ejemplos:**
```
feat: agregar exportación de reportes a PDF
fix: corregir cálculo de tiempo de gracia
docs: actualizar README con instrucciones de instalación
refactor: simplificar lógica de cálculo de tarifas
```

### Enviar Cambios

```bash
# Push a tu rama
git push origin feature/nombre-descriptivo
```

## 📝 Estándares de Código

### Convenciones de Java

1. **Nomenclatura:**
   - Clases: `PascalCase` (ej: `ParkingService`)
   - Métodos: `camelCase` (ej: `registerEntry`)
   - Constantes: `UPPER_SNAKE_CASE` (ej: `MAX_ATTEMPTS`)
   - Variables: `camelCase` (ej: `ticketNumber`)

2. **Formato:**
   - Indentación: 4 espacios
   - Llaves: Estilo Java (llave de apertura en la misma línea)
   - Líneas: Máximo 120 caracteres

3. **Comentarios:**
   - Javadoc para clases y métodos públicos
   - Comentarios inline para lógica compleja
   - Evitar comentarios obvios

**Ejemplo:**
```java
/**
 * Registra el ingreso de un vehículo al parqueadero
 * 
 * @param plate Placa del vehículo
 * @param vehicleType Tipo de vehículo
 * @param operatorId ID del operador
 * @return Ticket creado
 * @throws Exception Si hay algún error en el proceso
 */
public Ticket registerEntry(String plate, String vehicleType, Integer operatorId) throws Exception {
    // Validar placa
    if (!Validator.isValidPlate(plate)) {
        throw new IllegalArgumentException("Placa inválida");
    }
    
    // Resto de la lógica...
}
```

### Estructura de Archivos

```
src/main/java/com/crudzaso/crudpark/
├── config/          # Configuración
├── dao/             # Data Access Objects
├── model/           # Modelos de datos
├── service/         # Lógica de negocio
├── util/            # Utilidades
├── view/            # Interfaces gráficas
└── Main.java        # Clase principal
```

### Manejo de Excepciones

```java
// ✅ CORRECTO
try {
    Ticket ticket = parkingService.registerEntry(plate, type, operatorId);
    showSuccess("Ticket creado: " + ticket.getTicketNumber());
} catch (IllegalArgumentException e) {
    showError("Datos inválidos: " + e.getMessage());
} catch (SQLException e) {
    showError("Error de base de datos: " + e.getMessage());
    logger.error("SQL Error", e);
}

// ❌ INCORRECTO
try {
    // código...
} catch (Exception e) {
    // Ignorar silenciosamente
}
```

## 🧪 Testing

### Ejecutar Tests

```bash
mvn test
```

### Escribir Tests

Aunque el proyecto actual no tiene tests unitarios, si agregas funcionalidad nueva, considera agregar tests:

```java
@Test
public void testValidPlate() {
    assertTrue(Validator.isValidPlate("ABC123"));
    assertFalse(Validator.isValidPlate(""));
    assertFalse(Validator.isValidPlate("AB"));
}
```

## 📦 Pull Requests

### Antes de Crear un PR

1. **Actualizar tu rama:**
   ```bash
   git checkout main
   git pull origin main
   git checkout feature/tu-rama
   git merge main
   ```

2. **Verificar que compila:**
   ```bash
   mvn clean compile
   ```

3. **Probar la funcionalidad:**
   - Ejecutar la aplicación
   - Probar el flujo completo
   - Verificar que no rompe funcionalidad existente

### Crear el Pull Request

1. Push de tu rama
2. Ir al repositorio en GitHub
3. Crear Pull Request
4. Completar la plantilla:

```markdown
## Descripción
Breve descripción de los cambios

## Tipo de Cambio
- [ ] Nueva funcionalidad
- [ ] Corrección de bug
- [ ] Refactorización
- [ ] Documentación

## Checklist
- [ ] El código compila sin errores
- [ ] He probado los cambios localmente
- [ ] He actualizado la documentación
- [ ] Los commits siguen la convención
```

### Revisión de Código

- Espera la revisión de al menos un colaborador
- Atiende los comentarios y sugerencias
- Realiza los cambios solicitados
- Una vez aprobado, se hará merge a main

## 🐛 Reportar Bugs

### Crear un Issue

1. Ir a la sección de Issues
2. Crear nuevo issue
3. Usar la plantilla de bug:

```markdown
## Descripción del Bug
Descripción clara del problema

## Pasos para Reproducir
1. Ir a '...'
2. Hacer clic en '...'
3. Ver error

## Comportamiento Esperado
Lo que debería suceder

## Comportamiento Actual
Lo que realmente sucede

## Screenshots
Si aplica, agregar capturas

## Entorno
- OS: Windows 10
- Java: 17.0.2
- PostgreSQL: 14.2
```

## 💡 Sugerir Mejoras

Para sugerir nuevas funcionalidades:

1. Crear un issue con la etiqueta `enhancement`
2. Describir la funcionalidad propuesta
3. Explicar el caso de uso
4. Discutir con el equipo antes de implementar

## 📚 Documentación

### Actualizar Documentación

Si tus cambios afectan:
- Funcionalidad: Actualizar README.md
- Instalación: Actualizar INSTALL.md
- Base de datos: Actualizar schema.sql y documentar

### Escribir Javadoc

```java
/**
 * Calcula el monto a cobrar según la tarifa y el tiempo
 * 
 * <p>Aplica las siguientes reglas:
 * <ul>
 *   <li>Tiempo de gracia: primeros 30 minutos sin cobro</li>
 *   <li>Tarifa por hora completa</li>
 *   <li>Fracción adicional si hay minutos extra</li>
 *   <li>Tope diario máximo</li>
 * </ul>
 * 
 * @param totalMinutes Minutos totales de estadía
 * @param rate Tarifa a aplicar
 * @return Monto a cobrar
 */
private BigDecimal calculateAmount(long totalMinutes, Rate rate) {
    // Implementación...
}
```

## 🎯 Áreas de Contribución

### Funcionalidades Pendientes

- [ ] Reportes y estadísticas
- [ ] Exportación a Excel/PDF
- [ ] Búsqueda avanzada de tickets
- [ ] Gestión de turnos de operadores
- [ ] Dashboard con métricas en tiempo real

### Mejoras Sugeridas

- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Optimización de consultas SQL
- [ ] Caché de datos frecuentes
- [ ] Internacionalización (i18n)

## 🏆 Reconocimientos

Los contribuidores serán reconocidos en:
- README.md
- Sección de créditos en la aplicación
- Release notes

## 📞 Contacto

Para preguntas sobre contribuciones:
- Crear un issue con la etiqueta `question`
- Contactar al equipo de desarrollo

---

**¡Gracias por contribuir a CrudPark Java!** 🚀
