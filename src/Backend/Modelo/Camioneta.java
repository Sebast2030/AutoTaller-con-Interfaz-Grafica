package Backend.Modelo;

public class Camioneta extends Vehiculo{

    //Atributos unicos de Camioneta
    private double capacidadCarga; //Toneladas
    private boolean traccion4x4;

    //Constructor para Caminoeta
    public Camioneta(String id, String placa, String marca, String modelo, int ano,
                     String propietario, String telefono, String color,
                     double capacidadCarga, boolean traccion4x4){

        super(id, placa, marca, modelo, ano, propietario, telefono, color);

        this.capacidadCarga = capacidadCarga;
        this.traccion4x4 = traccion4x4;
    }

    //Metodos Override
    @Override
    public String getTipoVehiculo() { return "Camioneta"; }

    @Override
    public double calcularCostoBase() { return 120_000.0; } //$120,000 COP base

    //Getter y Setters
    public double getCapacidadCarga() { return capacidadCarga; }
    public void setCapacidadCarga(double capacidadCarga) { this.capacidadCarga = capacidadCarga; }

    public boolean isTraccion4x4() { return traccion4x4; }
    public void setTraccion4x4(boolean traccion4x4) { this.traccion4x4 = traccion4x4; }  
}
