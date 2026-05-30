package Backend.Modelo;

/**
 * Clase Carro - hereda de Vehiculo.
 * Aplica HERENCIA y POLIMORFISMO.
 */

public class Carro extends Vehiculo{
    
    //atributos unicos de Carro
    private int numeroPuertas;
    private String tipoTransmision; //MANUAL O AUTOMATICA

    //Constructor para Carro
    public Carro(String id, String placa, String marca, String modelo, int ano,
                 String propietario, String telefono, String color,
                 int numeroPuertas, String tipoTransmision){

        super(id, placa, marca, modelo, ano, propietario, telefono, color);
        
        this.numeroPuertas = numeroPuertas;
        this.tipoTransmision = tipoTransmision;
    }

    //Metodos Override para sobre escribir las funciones
    @Override
    public String getTipoVehiculo() { return "Carro"; }

    @Override
    public double calcularCostoBase() { return 80_000.0; } //Precio base de $80.000 COP

    //Getters y Setters
    public int    getNumeroPuertas()              { return numeroPuertas; }
    public void   setNumeroPuertas(int n)         { this.numeroPuertas = n; }

    public String getTipoTransmision()            { return tipoTransmision; }
    public void   setTipoTransmision(String t)    { this.tipoTransmision = t; }
}
