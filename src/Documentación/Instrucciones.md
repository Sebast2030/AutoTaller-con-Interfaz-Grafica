# 🔧 AutoTaller Con interfaz grafica — Sistema de Gestión de Vehículos

Proyecto Java con interfaz web (HTML/JS) para la gestión de un taller mecánico.

---

## ✅ Pilares de POO aplicados

| Pilar              | Dónde se aplica                                               |
|--------------------|---------------------------------------------------------------|
| **Abstracción**    | `Vehiculo.java` — clase abstracta con métodos abstractos      |
| **Encapsulamiento**| Todos los atributos son `private` con getters/setters         |
| **Herencia**       | `Carro`, `Moto`, `Camioneta` heredan de `Vehiculo`            |
| **Polimorfismo**   | `getTipoVehiculo()` y `calcularCostoBase()` sobreescritos     |

---

## 🗄️ Estructuras dinámicas

| Estructura                       | Uso                                          |
|----------------------------------|----------------------------------------------|
| `HashMap<String, Vehiculo>`      | Almacén principal — búsqueda O(1) por placa  |
| `HashMap<String, OrdenServicio>` | Almacén de órdenes por ID                    |
| `Queue<String>` (LinkedList)     | Cola FIFO de vehículos en espera             |
| `ArrayList<String>`              | Lista de servicios dentro de cada orden      |

---

## 🖥️ Interfaz gráfica

La interfaz se sirve desde `web/index.html` directamente en el navegador.

### Módulos:
- **Dashboard** — estadísticas en tiempo real (vehículos, órdenes, ingresos, cola)
- **Vehículos** — CRUD completo: registrar, buscar/filtrar, editar, eliminar
- **Órdenes de Servicio** — crear, cerrar y eliminar órdenes
- **Cola de Espera** — visualización FIFO y botón "Atender"

---

## 🚀 Cómo ejecutar

### Solo interfaz web (sin compilar Java)
1. Abrir `web/index.html` directamente en cualquier navegador.
2. El sistema carga datos de demostración automáticamente.

# Ejecutar el archivo HTML
Se abrira una ventana en Visual Studio Code, que es la ejucion del proyecto

---

## ✔️ Validaciones implementadas

- Placa en formato colombiano:Para carro `ABC123`, o para moto `ABC12A`
- Teléfono: 7 a 10 dígitos, en formato colombiano
- Año: entre 1950 y el año actual
- Nombre/propietario: mínimo 3 caracteres
- Placa no duplicada al registrar
- Cilindraje de moto: 50–2000 cc
- Placa debe existir al crear una orden

---

## 📁 Repositorio

https://github.com/Sebast2030/AutoTaller-con-Interfaz-Grafica.git
hecho por: juan Sebastian Quintero

---