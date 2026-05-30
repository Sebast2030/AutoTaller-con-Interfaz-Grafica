package Backend.Servicios;

/**
 * Clase utilizada para validación de datos en los metodos.
 * Centraliza todas las validaciones del sistema.
 */

public class Validador {
    
    private Validador() {} //Constructor privado que utilizara la clase

    //Valida placa de vehiculo de 4 ruedas (Carro y camioneta) (XXX000)
    public static boolean placaCarroValida(String placa){
        if (placa == null || placa.isBlank()) return false;
        String limpia = placa.replaceAll("[\\s-]", "").toUpperCase();
        return limpia.matches("[A-Z]{3}[0-9]{3}");
    }

    /**
     * Valida placa de moto (XXX00X)
     * Valida que la ultima letra este entre A - I
     */
    public static boolean placaMotoValida(String placa){
        if (placa == null || placa.isBlank()) return false;
        String limpia = placa.replaceAll("[\\s-]", "").toUpperCase();
        return limpia.matches("[A-Z]{3}[0-9]{2}[A-I]");
    }

    //Valida la placa segun el tipo de vehiculo
    public static boolean placaValida(String placa, String tipoVehiculo){
        if("Moto".equalsIgnoreCase(tipoVehiculo)) return placaMotoValida(placa);
        return placaCarroValida(placa);
    }

    //Valida que el telefono tenga 10 numeros
    public static boolean telefonoValido(String telefono) {
    if (telefono == null || telefono.isBlank()) return false;
    
    String limpio = telefono.replaceAll("[\\s()-]", ""); //Se limpia el texto
    if (!limpio.matches("[0-9]+")) return false; //Verificamos que solo tenga números (usando regex) 
    long telLong = Long.parseLong(limpio); //Se convierte a long
    
    return telLong >= 3000000000L && telLong <= 3500000000L;
    }

    //Valida el año del vehiculo
    public static boolean anoValido(int ano){
        int actual = java.time.LocalDate.now().getYear();
        return ano >= 1970 && ano <= actual + 1;
    }

    //Valida que un texto no esté vacío y tenga longitud mínima
    public static boolean textoValido(String texto, int minLen) {
        return texto != null && texto.trim().length() >= minLen;
    }

    //Valida que un valor numérico sea positivo
    public static boolean montoValido(double monto) {
        return monto >= 0;
    }

    //Valida cilindraje de moto
    public static boolean cilindrajeValido(int cc) {
        return cc >= 50 && cc <= 2000;
    }
}
