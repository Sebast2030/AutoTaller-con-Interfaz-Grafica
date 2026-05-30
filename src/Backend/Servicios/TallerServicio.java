package Backend.Servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import Backend.Modelo.*;
import Backend.Modelo.Vehiculo.EstadoVehiculo;

/**
 * Servicio principal del Taller Mecánico.
 * Implementa CRUD completo usando HashMap y Queue como estructuras dinámicas.
 */

public class TallerServicio {

    //HashMap - almacenamiento principal de vehiculos
    private final Map<String, Vehiculo> vehiculos = new HashMap<>();

    //HashMap - ordenes de servicio
    private final Map<String, OrdenServicio> ordenes = new HashMap<>();

    //Queue - cola de vehiculos en espera (FIFO)
    private final Queue<String> colaEspera = new LinkedList<>();

    //Contador de orden
    private int contadorOrden = 1;


    //CRUD

    //Crear solicitud (CREATE)
    public boolean registrarVehiculo(Vehiculo v){
        if (vehiculos.containsKey(v.getPlaca())) return false;
        vehiculos.put(v.getPlaca(), v);
        colaEspera.offer(v.getPlaca()); //Agrega la solicuitud a la cola de espera
        return true;
    }

    //Buscar vehiculo (READ)
    public Optional<Vehiculo> buscarVehiculo(String placa){
        return Optional.ofNullable(vehiculos.get(placa.toUpperCase()));
    }

    //Listar los vehiculos (READ)
    public List<Vehiculo> listarVehiculos(){
        return new ArrayList<>(vehiculos.values());
    }

    //Listar los vehiculos (READ)
    public List<Vehiculo> listarPorEstado(EstadoVehiculo estado){
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : vehiculos.values()){
            if(v.getEstado() == estado) resultado.add(v);
        }
        return resultado;
    }

    //Actualizar vehiculo (UPDATE)
    public boolean actualizarVehiculo(String placa, String propietario, String telefono, EstadoVehiculo nuevoEstado){
        Vehiculo v = vehiculos.get(placa.toUpperCase());

        if (v == null) return false;
        if(propietario != null && !propietario.isBlank()) v.setPropietario(propietario);
        if(telefono != null && !telefono.isBlank()) v.setTelefono(telefono);
        if(nuevoEstado   != null) v.setEstado(nuevoEstado);

        return true;
    }

    //Eliminar un vehiculo (DELETE)
    public boolean eliminarVehiculo(String placa){
        String key = placa.toUpperCase();
        if (!vehiculos.containsKey(key)) return false;
        vehiculos.remove(key);
        colaEspera.remove(key);
        return false;
    }

    //Ordenes del Servicio
    public OrdenServicio crearOrden(String placa, String probelma, String mecanico){
        String idOrden = String.format("ORD-%04d", contadorOrden++);
        OrdenServicio orden = new OrdenServicio(idOrden, placa.toUpperCase(), probelma, mecanico);
        ordenes.put(idOrden, orden);
        actualizarVehiculo(placa, null, null, EstadoVehiculo.EN_REVISION);
        return orden;
    }

    public Optional<OrdenServicio> buscarOrden(String idOrden){
        return Optional.ofNullable(ordenes.get(idOrden));
    }

    public List<OrdenServicio> listarOrdenes() {
        return new ArrayList<>(ordenes.values());
    }

    public List<OrdenServicio> ordenesPorVehiculo(String placa) {
        List<OrdenServicio> resultado = new ArrayList<>();
        for (OrdenServicio o : ordenes.values()) {
            if (o.getPlacaVehiculo().equalsIgnoreCase(placa)) resultado.add(o);
        }
        return resultado;
    }

    public boolean cerrarOrden(String idOrden) {
        OrdenServicio o = ordenes.get(idOrden);
        if (o == null) return false;
        o.cerarOrden();
        actualizarVehiculo(o.getPlacaVehiculo(), null, null, EstadoVehiculo.LISTO);
        return true;
    }

    public boolean eliminarOrden(String idOrden) {
        return ordenes.remove(idOrden) != null;
    }

    // ─────────────────────────────────────────────────────────────
    // COLA DE ATENCIÓN
    // ─────────────────────────────────────────────────────────────

    /** Retorna el siguiente vehículo en la cola sin removerlo */
    public Optional<String> verSiguienteEnCola() {
        return Optional.ofNullable(colaEspera.peek());
    }

    /** Atiende el siguiente vehículo de la cola */
    public Optional<String> atenderSiguiente() {
        return Optional.ofNullable(colaEspera.poll());
    }

    public int tamanoColaEspera() { return colaEspera.size(); }

    // ─────────────────────────────────────────────────────────────
    // ESTADÍSTICAS
    // ─────────────────────────────────────────────────────────────

    public int totalVehiculos()  { return vehiculos.size(); }
    public int totalOrdenes()    { return ordenes.size(); }

    public double ingresosTotales() {
        return ordenes.values().stream()
                .filter(o -> "CERRADA".equals(o.getEstado()))
                .mapToDouble(OrdenServicio::getCostoTotal)
                .sum();
    }
}
