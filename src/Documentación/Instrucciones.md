# 🔧 AutoTaller Pro — Sistema de Gestión de Vehículos

Proyecto Java con interfaz web (HTML/JS) para la gestión de un taller mecánico.
Cumple todos los requisitos de Programación Orientada a Objetos.

---

## 📁 Estructura del proyecto

```
taller/
├── src/main/java/com/taller/
│   ├── Main.java                  ← Punto de entrada + servidor HTTP embebido
│   ├── modelo/
│   │   ├── Vehiculo.java          ← Clase abstracta (ABSTRACCIÓN)
│   │   ├── Carro.java             ← Hereda de Vehiculo (HERENCIA + POLIMORFISMO)
│   │   ├── Moto.java              ← Hereda de Vehiculo (HERENCIA + POLIMORFISMO)
│   │   ├── Camioneta.java         ← Hereda de Vehiculo (HERENCIA + POLIMORFISMO)
│   │   └── OrdenServicio.java     ← Modelo de orden con ArrayList
│   ├── servicio/
│   │   └── TallerServicio.java    ← CRUD completo con HashMap y Queue
│   └── util/
│       └── Validador.java         ← Validación de datos
└── web/
    └── index.html                 ← Interfaz gráfica (HTML + JS)
```

---

## ✅ Pilares de POO aplicados

| Pilar              | Dónde se aplica                                               |
|--------------------|---------------------------------------------------------------|
| **Abstracción**    | `Vehiculo.java` — clase abstracta con métodos abstractos      |
| **Encapsulamiento**| Todos los atributos son `private` con getters/setters         |
| **Herencia**       | `Carro`, `Moto`, `Camioneta` heredan de `Vehiculo`            |
| **Polimorfismo**   | `getTipoVehiculo()` y `calcularCostoBase()` sobreescritos      |

---

## 🗄️ Estructuras dinámicas

| Estructura             | Uso                                          |
|------------------------|----------------------------------------------|
| `HashMap<String, Vehiculo>` | Almacén principal — búsqueda O(1) por placa |
| `HashMap<String, OrdenServicio>` | Almacén de órdenes por ID             |
| `Queue<String>` (LinkedList) | Cola FIFO de vehículos en espera       |
| `ArrayList<String>`    | Lista de servicios dentro de cada orden      |

---

## 🖥️ Interfaz gráfica

La interfaz se sirve desde `web/index.html` directamente en el navegador.

### Módulos:
- **Dashboard** — estadísticas en tiempo real (vehículos, órdenes, ingresos, cola)
- **Vehículos** — CRUD completo: registrar, buscar/filtrar, editar, eliminar
- **Órdenes de Servicio** — crear, cerrar y eliminar órdenes
- **Cola de Espera** — visualización FIFO y botón "Atender siguiente"

---

## 🚀 Cómo ejecutar

### Opción A — Solo interfaz web (sin compilar Java)
1. Abrir `web/index.html` directamente en cualquier navegador.
2. El sistema carga datos de demostración automáticamente.

# Abrir en el navegador
http://localhost:8080
```

---

## ✔️ Validaciones implementadas

- Placa en formato colombiano:Para carro `ABC123`, o para moto `ABC12A`
- Teléfono: 7 a 10 dígitos, en formato colombiano
- Año: entre 1950 y el año actual
- Nombre/propietario: mínimo 3 caracteres
- Placa no duplicada al registrar
- Cilindraje de moto: 50–2000 cc
- Placa debe existir al crear una orden