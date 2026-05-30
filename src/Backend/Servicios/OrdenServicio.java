package Backend.Servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase OrdenServicio - representa el trabajo a realizar sobre un vehículo.
 * Usa ArrayList como estructura dinámica.
 */

public class OrdenServicio {

    //Atributos necesarios para las ordenes
    private String idOrden, placaVehiculo;
    private LocalDate fechaIngreso, fechaEstimadaEntrga;
    private String descripcionProblema;
    private List<String> serviciosRealizados;
    private double costoTotal;
    private String mecanico, estado;

    //Constructores
    public OrdenServicio(String idOrden, String placaVehiculo, String descripcionProblema, String mecanico) {

        this.idOrden = idOrden;
        this.placaVehiculo = placaVehiculo;
        this.descripcionProblema = descripcionProblema;
        this.mecanico = mecanico;
        this.fechaIngreso = LocalDate.now();
        this.fechaEstimadaEntrga = LocalDate.now().plusDays(3);
        this.serviciosRealizados = new ArrayList<>();
        this.costoTotal = 0.0;
        this.estado = "ABIERTA";
    }
    
    public void agregarServicio(String servicio, double costo){
        serviciosRealizados.add(servicio);
        costoTotal += costo;
    }

    //Metodos para cerrar y cancelar ordenes
    public void cerarOrden(){ this.estado = "CERRADA"; }
    public void cancelarOrden(){ this.estado = "CANCELADA"; }

    //Getters y Setters
    public String getIdOrden() { return idOrden; }

    public String getPlacaVehiculo() { return placaVehiculo; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }

    public LocalDate getFechaEstimadaEntrga() { return fechaEstimadaEntrga; }
    public void setFechaEstimadaEntrga(LocalDate f) { this.fechaEstimadaEntrga = f; }

    public String getDescripcionProblema() { return descripcionProblema; }
    public void setDescripcionProblema(String d) { this.descripcionProblema = d; }

    public List<String> getServiciosRealizados() { return serviciosRealizados; }
    
    public double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(double c) { this.costoTotal = c; }

    public String getMecanico() { return mecanico; }
    public void setMecanico(String m) { this.mecanico = m; }

    public String getEstado() { return estado; }
    public void setEstado(String e) { this.estado = e; }
}
