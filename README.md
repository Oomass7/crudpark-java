# CrudPark - Aplicación Java (Swing + JDBC)

Aplicación de escritorio para la gestión operativa de un parqueadero, desarrollada en Java con Swing para la interfaz gráfica y JDBC para la conexión a PostgreSQL.

## 📋 Descripción del Proyecto

CrudPark es un sistema de parqueadero que permite a los operadores gestionar el ingreso y salida de vehículos, calcular tarifas automáticamente, generar tickets con códigos QR y procesar pagos. El sistema se integra con una base de datos PostgreSQL compartida con la aplicación administrativa desarrollada en C#.

## 🎯 Funcionalidades Principales

### ✅ Inicio de Sesión de Operadores
- Autenticación por documento de identidad
- Validación de operadores activos
- Interfaz moderna y amigable

### ✅ Ingreso de Vehículos
- Registro de placa y tipo de vehículo
- Detección automática de mensualidades vigentes
- Generación de ticket con código QR
- Impresión de ticket de ingreso

### ✅ Salida de Vehículos
- Búsqueda por placa
- Cálculo automático de tiempo de estadía
- Aplicación de tiempo de gracia (30 minutos)
- Cálculo de tarifa según tipo de vehículo
- Salida sin cobro para mensualidades vigentes
- Generación de ticket de salida

### ✅ Procesamiento de Pagos
- Registro de pagos (Efectivo, Tarjeta, Transferencia)
- Asociación de pago con ticket
- Registro del operador que procesa el pago

### ✅ Visualización de Vehículos Activos
- Lista en tiempo real de vehículos dentro del parqueadero
- Información detallada de cada ticket abierto

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas:

```
src/main/java/com/crudzaso/crudpark/
├── config/          # Configuración de base de datos
├── dao/             # Data Access Objects (acceso a datos)
├── model/           # Modelos de datos
├── service/         # Lógica de negocio
├── util/            # Utilidades (QR, validaciones, formateo)
├── view/            # Interfaces gráficas (Swing)
└── Main.java        # Clase principal
```

### Capas del Sistema

#### 1. **Config** - Configuración
- `DatabaseConfig.java`: Gestión de conexiones a PostgreSQL

#### 2. **Model** - Modelos de Datos
- `Operator.java`: Operadores del parqueadero
- `Vehicle.java`: Vehículos
- `Ticket.java`: Tickets de ingreso/salida
- `Rate.java`: Tarifas de parqueadero
- `Subscription.java`: Mensualidades
- `Payment.java`: Pagos

#### 3. **DAO** - Acceso a Datos
- `OperatorDAO.java`: Operaciones con operadores
- `VehicleDAO.java`: Operaciones con vehículos
- `TicketDAO.java`: Operaciones con tickets
- `RateDAO.java`: Operaciones con tarifas
- `SubscriptionDAO.java`: Operaciones con mensualidades
- `PaymentDAO.java`: Operaciones con pagos

#### 4. **Service** - Lógica de Negocio
- `AuthService.java`: Autenticación de operadores
- `ParkingService.java`: Lógica principal del parqueadero

#### 5. **Util** - Utilidades
- `QRCodeGenerator.java`: Generación de códigos QR
- `Validator.java`: Validaciones de datos
- `DateTimeFormatter.java`: Formateo de fechas
- `CurrencyFormatter.java`: Formateo de moneda

#### 6. **View** - Interfaces Gráficas
- `LoginFrame.java`: Ventana de inicio de sesión
- `MainFrame.java`: Ventana principal
- `TicketDialog.java`: Diálogo de tickets
- `PaymentDialog.java`: Diálogo de pagos

## 🛠️ Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación
- **Maven**: Gestor de dependencias
- **Swing**: Framework para interfaz gráfica
- **JDBC**: Conexión a base de datos
- **PostgreSQL**: Base de datos relacional
- **ZXing**: Generación de códigos QR
- **FlatLaf**: Look and Feel moderno

## 📦 Dependencias

```xml
- PostgreSQL JDBC Driver 42.7.3
- ZXing Core 3.5.3
- ZXing JavaSE 3.5.3
- FlatLaf 3.4.1
```

## ⚙️ Instalación y Configuración

### Requisitos Previos

1. **Java JDK 17 o superior**
   ```bash
   java -version
   ```

2. **Maven 3.6 o superior**
   ```bash
   mvn -version
   ```

3. **PostgreSQL 12 o superior**
   - Base de datos `crudpark` creada
   - Esquema ejecutado (ver `resources/schema.sql`)

### Pasos de Instalación

1. **Clonar el repositorio**
   ```bash
   git clone <url-del-repositorio>
   cd crudpark-java
   ```

2. **Configurar la base de datos**
   
   Copiar el archivo de ejemplo:
   ```bash
   copy config.properties.example config.properties
   ```
   
   Editar `config.properties` con tus credenciales:
   ```properties
   db.host=localhost
   db.port=5432
   db.name=crudpark
   db.user=postgres
   db.password=tu_contraseña
   ```

3. **Ejecutar el esquema de base de datos**
   
   Desde PostgreSQL o tu gestor de BD favorito, ejecuta:
   ```bash
   psql -U postgres -d crudpark -f resources/schema.sql
   ```

4. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

5. **Ejecutar la aplicación**
   ```bash
   mvn exec:java -Dexec.mainClass="com.crudzaso.crudpark.Main"
   ```

## 📦 Generar Ejecutable

Para generar un JAR ejecutable:

```bash
mvn clean package
```

El archivo JAR se generará en `target/crudpark-1.0.0.jar`

Para ejecutarlo:
```bash
java -jar target/crudpark-1.0.0.jar
```

**Nota:** Asegúrate de que el archivo `config.properties` esté en la misma carpeta que el JAR.

## 🗄️ Estructura de la Base de Datos

### Tablas Principales

- **Operators**: Operadores del parqueadero
- **Vehicles**: Vehículos registrados
- **Rates**: Tarifas por tipo de vehículo
- **Subscriptions**: Mensualidades activas
- **Tickets**: Tickets de ingreso/salida
- **Payments**: Pagos realizados

### Nomenclatura

La base de datos utiliza nomenclatura **PascalCase** (generada desde Entity Framework C#):
- Tablas: `"Operators"`, `"Vehicles"`, etc.
- Columnas: `"Id"`, `"FullName"`, `"IsActive"`, etc.

## 🔐 Inicio de Sesión

El sistema permite login por **documento de identidad**. Los operadores deben estar:
- Registrados en la tabla `Operators`
- Con estado `IsActive = true`

### Operadores de Prueba

Si ejecutaste el esquema SQL, ya tienes operadores de ejemplo. Consulta la base de datos:

```sql
SELECT "Document", "FullName" FROM public."Operators" WHERE "IsActive" = true;
```

## 📝 Reglas de Negocio Implementadas

### 1. Tiempo de Gracia
- Los primeros 30 minutos (configurable) no generan cobro
- Aplica solo para vehículos invitados

### 2. Un Ticket por Vehículo
- No se permite registrar un nuevo ingreso si el vehículo ya tiene un ticket abierto
- Validación automática por placa

### 3. Mensualidades Vigentes
- El sistema detecta automáticamente si un vehículo tiene mensualidad activa
- Entrada y salida sin cobro para mensualidades vigentes
- Validación por rango de fechas

### 4. Cálculo de Tarifas
- Tarifa base por hora
- Tarifa adicional por fracción
- Tope diario máximo (si está configurado)
- Diferentes tarifas según tipo de vehículo

### 5. Validaciones
- Placas: 4-10 caracteres alfanuméricos
- Documentos: 6-15 dígitos
- No se permiten campos vacíos
- Formato automático de placas (mayúsculas)

## 🎫 Formato del Ticket

### Ticket de Ingreso
```
==============================
  CrudPark - Crudzaso
==============================
Ticket #: 20251021012345001
Placa: ABC123
Tipo: Invitado
Tipo Vehículo: Sedán
Ingreso: 21/10/2025 01:23
Operador: Juan Pérez
------------------------------
QR: TICKET:20251021012345001|PLATE:ABC123|DATE:1729478580
------------------------------
Gracias por su visita
==============================
```

### Formato del Código QR
```
TICKET:{número}|PLATE:{placa}|DATE:{timestamp}
```

## 🖨️ Impresión de Tickets

La aplicación incluye funcionalidad de impresión:
- Compatible con impresoras térmicas
- Formato optimizado para tickets de 80mm
- Incluye código QR
- Impresión desde cualquier diálogo de ticket

## 🚨 Manejo de Errores

El sistema incluye manejo robusto de excepciones:

- **Validaciones de entrada**: Mensajes claros al usuario
- **Errores de BD**: Captura y muestra de errores SQL
- **Conexión perdida**: Detección y notificación
- **Datos inconsistentes**: Validación antes de guardar

## 🔄 Comunicación con la Aplicación C#

Ambas aplicaciones (Java y C#) comparten la **misma base de datos PostgreSQL**:

- **No hay APIs ni endpoints HTTP**
- La comunicación es a través de la base de datos
- Cambios en tiempo real mediante consultas directas
- Importante: Mantener consistencia en los datos

## 📊 Flujo de Uso

### Flujo de Ingreso
1. Operador inicia sesión con su documento
2. Selecciona "Ingreso de Vehículo"
3. Ingresa placa y tipo de vehículo
4. Sistema verifica mensualidad
5. Genera ticket con QR
6. Imprime ticket
7. Entrega ticket al cliente

### Flujo de Salida
1. Cliente presenta ticket o placa
2. Operador ingresa placa en "Salida de Vehículo"
3. Sistema calcula tiempo y monto
4. Si requiere pago:
   - Muestra monto a cobrar
   - Operador selecciona método de pago
   - Registra pago
5. Imprime ticket de salida
6. Vehículo sale del parqueadero

## 🐛 Solución de Problemas

### Error de Conexión a BD
```
✗ Error de conexión
```
**Solución:**
- Verificar que PostgreSQL esté ejecutándose
- Revisar credenciales en `config.properties`
- Confirmar que la base de datos `crudpark` existe

### Error: "No hay tarifas configuradas"
**Solución:**
- Ejecutar el esquema SQL completo
- Insertar tarifas manualmente:
```sql
INSERT INTO public."Rates" ("TypeVehicle", "HourPrice", "AddPrice", "MaxPrice", "GraceTime", "IsActive", "CreatedAt")
VALUES ('Sedán', 5000.00, 1500.00, 25000.00, 30, true, CURRENT_TIMESTAMP);
```

### Error: "Operador no encontrado"
**Solución:**
- Verificar que existan operadores activos en la BD
- Insertar operador de prueba:
```sql
INSERT INTO public."Operators" ("FullName", "Document", "Email", "Status", "IsActive", "CreatedAt")
VALUES ('Operador Test', '12345678', 'test@crudpark.com', 'Active', true, CURRENT_TIMESTAMP);
```

## 👥 Equipo de Desarrollo

Este proyecto fue desarrollado como parte del reto **CrudPark - Parking Ops & Admin Challenge**.

**Equipo:**
- [Tu nombre] - Desarrollo Java (Berners-Lee)
- [Compañero 1] - Desarrollo C# (Van Rossum)
- [Compañero 2] - Desarrollo C# (Van Rossum)

**Registro del equipo:** https://teams.crudzaso.com

## 📄 Licencia

Este proyecto es parte de un ejercicio académico para Crudzaso.

## 🔗 Enlaces Útiles

- [Repositorio Java](https://github.com/usuario/crudpark-java)
- [Repositorio C# Frontend](https://github.com/usuario/crudpark-csharp-front)
- [Repositorio C# Backend](https://github.com/usuario/crudpark-csharp-back)
- [Registro de Equipos](https://teams.crudzaso.com)

## 📞 Soporte

Para problemas o preguntas:
1. Revisar la sección de Solución de Problemas
2. Verificar la documentación del esquema SQL
3. Contactar al equipo de desarrollo

---

**Desarrollado con ☕ y Java por el equipo CrudPark**
