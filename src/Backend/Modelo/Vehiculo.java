package Backend.Modelo;

/**
 * Clase abstracta base que representa un Vehículo.
 * Aplica ABSTRACCIÓN y ENCAPSULAMIENTO (pilar de POO).
 */

public abstract class Vehiculo {
    
    //Atributos privados - Encapsulamiento
    private String id, placa, marca, modelo;
    private int ano;
    private String propietario, telefono, color;
    private EstadoVehiculo estado;

    public enum EstadoVehiculo{
        EN_ESPERA, EN_REVISION, EN_REPARACION, LISTO, ENTREGADO
    }

    //Constructor
    public Vehiculo(String id, String placa, String marca, String modelo,
                    int ano, String propietario, String telefono, String color){
                        
        this.id = id;
        this.placa = placa.toUpperCase();
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.propietario = propietario;
        this.telefono = telefono;
        this.color = color;
        this.estado = EstadoVehiculo.EN_ESPERA;
    
    }

    //Metodos abtractos
    public abstract String getTipoVehiculo();
    public abstract double calcularCostoBase();

    //Getters y Setters
    public String getId()                        { return id; }
    public void   setId(String id)               { this.id = id; }

    public String getPlaca()                     { return placa; }
    public void   setPlaca(String placa)         { this.placa = placa.toUpperCase(); }

    public String getMarca()                     { return marca; }
    public void   setMarca(String marca)         { this.marca = marca; }

    public String getModelo()                    { return modelo; }
    public void   setModelo(String modelo)       { this.modelo = modelo; }

    public int    getAnio()                      { return ano; }
    public void   setAnio(int anio)              { this.ano = anio; }

    public String getPropietario()               { return propietario; }
    public void   setPropietario(String p)       { this.propietario = p; }

    public String getTelefono()                  { return telefono; }
    public void   setTelefono(String t)          { this.telefono = t; }

    public String getColor()                     { return color; }
    public void   setColor(String color)         { this.color = color; }

    public EstadoVehiculo getEstado()            { return estado; }
    public void           setEstado(EstadoVehiculo e) { this.estado = e; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s %s %d | Color: %s | Propietario: %s | Tel: %s | Estado: %s",
                placa, getTipoVehiculo(), marca, modelo, ano, color,
                propietario, telefono, estado.name());
    }
}
