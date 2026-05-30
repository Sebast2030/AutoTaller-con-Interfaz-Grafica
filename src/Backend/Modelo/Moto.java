package Backend.Modelo;

/**
 * Clase Moto - hereda de Vehiculo.
 * Aplica HERENCIA y POLIMORFISMO.
 */

public class Moto extends Vehiculo{

    //Atributos unicos de moto
    private String tipoMoto; //DEPORTIVA / SCOOTER (NORMAL) / TODOTERRENO / CLASICA
    private int cilindraje;

    //Constructor para Motos
    public Moto(String id, String placa, String marca, String modelo,
                int ano, String propietario, String telefono, String color,
                int cilindraje, String tipoMoto) {

        super(id, placa, marca, modelo, ano, propietario, telefono, color);
        
        this.tipoMoto = tipoMoto;
        this.cilindraje = cilindraje;
    }

    //Metodos Override para sobre escribir las funciones
    @Override
    public String getTipoVehiculo() { return "Moto"; }

    @Override
    public double calcularCostoBase() { return 45_000.0; } //$45,000 COP base

    //Getters y Setters
    public String getTipoMoto() { return tipoMoto; }
    public void setTipoMoto(String tipoMoto) { this.tipoMoto = tipoMoto; }

    public int getCilindraje() { return cilindraje; }
    public void setCilindraje(int cilindraje) { this.cilindraje = cilindraje; }
}
