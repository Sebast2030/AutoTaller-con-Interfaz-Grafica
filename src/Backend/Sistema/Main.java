package Backend.Sistema;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import Backend.Modelo.*;
import Backend.Servicios.*;
import Backend.Modelo.Vehiculo.EstadoVehiculo;

/**
 * Clase principal - levanta un servidor HTTP embebido que sirve la interfaz web.
 * La lógica completa corre en Java; el navegador es solo la vista (HTML).
 */

public class Main {
    
    private static final TallerServicio servicio = new TallerServicio();
    private static final org.json.JSONObject OK  = new org.json.JSONObject();

    public static void main(String[] args) throws IOException {

        // Datos de demostración
        cargarDatosDemo();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/vehiculos",       Main::handleVehiculos);
        server.createContext("/api/ordenes",         Main::handleOrdenes);
        server.createContext("/api/estadisticas",    Main::handleEstadisticas);
        server.createContext("/",                    Main::handleStatic);

        server.setExecutor(null);
        server.start();
        System.out.println("Taller Mecánico iniciado en http://localhost:8080");
    }

    // ── Rutas de vehículos ──────────────────────────────────────
    private static void handleVehiculos(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        String path   = ex.getRequestURI().getPath();
        Map<String,String> params = queryParams(ex.getRequestURI().getRawQuery());

        if ("GET".equals(method)) {
            List<Vehiculo> lista = servicio.listarVehiculos();
            responder(ex, 200, vehiculosToJson(lista));

        } else if ("POST".equals(method)) {
            Map<String,String> body = parseBody(ex);
            String tipo = body.getOrDefault("tipo","carro").toLowerCase();

            if (!Validador.placaValida(body.get("placa"), body.get("Tipo Vehiculo"))) {
                responder(ex, 400, "{\"error\":\"Placa inválida\"}"); return;
            }
            if (!Validador.telefonoValido(body.get("telefono"))) {
                responder(ex, 400, "{\"error\":\"Teléfono inválido\"}"); return;
            }
            int ano;
            try { ano = Integer.parseInt(body.get("anio")); } catch (Exception e) { ano = 0; }
            if (!Validador.anoValido(ano)) {
                responder(ex, 400, "{\"error\":\"Año inválido\"}"); return;
            }

            String id = "V-" + System.currentTimeMillis();
            Vehiculo v;
            switch (tipo) {
                case "moto":
                    v = new Moto(id, body.get("placa"), body.get("marca"), body.get("modelo"),
                            ano, body.get("propietario"), body.get("telefono"),
                            body.get("color"), Integer.parseInt(body.getOrDefault("cilindraje","125")),
                            body.getOrDefault("tipoMoto","SCOOTER"));
                    break;
                case "camioneta":
                    v = new Camioneta(id, body.get("placa"), body.get("marca"), body.get("modelo"),
                            ano, body.get("propietario"), body.get("telefono"),
                            body.get("color"), Double.parseDouble(body.getOrDefault("carga","1.0")),
                            "true".equals(body.get("traccion4x4")));
                    break;
                default:
                    v = new Carro(id, body.get("placa"), body.get("marca"), body.get("modelo"),
                            ano, body.get("propietario"), body.get("telefono"),
                            body.get("color"), Integer.parseInt(body.getOrDefault("puertas","4")),
                            body.getOrDefault("transmision","MANUAL"));
            }

            boolean ok = servicio.registrarVehiculo(v);
            if (ok) responder(ex, 201, vehiculoToJson(v));
            else    responder(ex, 409, "{\"error\":\"Placa ya registrada\"}");

        } else if ("PUT".equals(method)) {
            Map<String,String> body = parseBody(ex);
            String placa = body.get("placa");
            EstadoVehiculo estado = null;
            try { estado = EstadoVehiculo.valueOf(body.get("estado")); } catch (Exception ignored) {}
            boolean ok = servicio.actualizarVehiculo(placa, body.get("propietario"), body.get("telefono"), estado);
            responder(ex, ok ? 200 : 404, ok ? "{\"ok\":true}" : "{\"error\":\"No encontrado\"}");

        } else if ("DELETE".equals(method)) {
            String placa = params.getOrDefault("placa","");
            boolean ok = servicio.eliminarVehiculo(placa);
            responder(ex, ok ? 200 : 404, ok ? "{\"ok\":true}" : "{\"error\":\"No encontrado\"}");
        }
    }

    // ── Rutas de órdenes ────────────────────────────────────────
    private static void handleOrdenes(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        Map<String,String> params = queryParams(ex.getRequestURI().getRawQuery());

        if ("GET".equals(method)) {
            responder(ex, 200, ordenesToJson(servicio.listarOrdenes()));
        } else if ("POST".equals(method)) {
            Map<String,String> body = parseBody(ex);
            if (!Validador.textoValido(body.get("placa"), 6)) {
                responder(ex, 400, "{\"error\":\"Placa requerida\"}"); return;
            }
            OrdenServicio o = servicio.crearOrden(body.get("placa"), body.get("problema"), body.get("mecanico"));
            // Agregar servicios si vienen
            if (body.containsKey("servicios")) {
                for (String s : body.get("servicios").split("\\|")) {
                    String[] parts = s.split(":");
                    if (parts.length == 2) {
                        try { o.agregarServicio(parts[0], Double.parseDouble(parts[1])); } catch (Exception ignored) {}
                    }
                }
            }
            responder(ex, 201, ordenToJson(o));
        } else if ("PUT".equals(method)) {
            Map<String,String> body = parseBody(ex);
            String idOrden = body.get("idOrden");
            if ("CERRADA".equals(body.get("estado"))) {
                servicio.cerrarOrden(idOrden);
            }
            responder(ex, 200, "{\"ok\":true}");
        } else if ("DELETE".equals(method)) {
            String id = params.getOrDefault("idOrden","");
            boolean ok = servicio.eliminarOrden(id);
            responder(ex, ok ? 200 : 404, ok ? "{\"ok\":true}" : "{\"error\":\"No encontrado\"}");
        }
    }

    // ── Estadísticas ─────────────────────────────────────────────
    private static void handleEstadisticas(HttpExchange ex) throws IOException {
        String json = String.format(
            "{\"totalVehiculos\":%d,\"totalOrdenes\":%d,\"ingresos\":%.0f,\"colaEspera\":%d}",
            servicio.totalVehiculos(), servicio.totalOrdenes(),
            servicio.ingresosTotales(), servicio.tamanoColaEspera()
        );
        responder(ex, 200, json);
    }

    // ── Sirve la interfaz HTML ────────────────────────────────────
    private static void handleStatic(HttpExchange ex) throws IOException {
        InputStream is = Main.class.getResourceAsStream("/web/index.html");
        if (is == null) { responder(ex, 404, "Not found"); return; }
        byte[] bytes = is.readAllBytes();
        ex.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        ex.sendResponseHeaders(200, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.getResponseBody().close();
    }

    // ── Utilidades ────────────────────────────────────────────────
    private static void responder(HttpExchange ex, int code, String body) throws IOException {
        ex.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        ex.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.getResponseBody().close();
    }

    private static Map<String,String> queryParams(String query) {
        Map<String,String> map = new HashMap<>();
        if (query == null) return map;
        for (String p : query.split("&")) {
            String[] kv = p.split("=", 2);
            if (kv.length == 2) map.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
        }
        return map;
    }

    private static Map<String,String> parseBody(HttpExchange ex) throws IOException {
        String raw = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return queryParams(raw);
    }

    private static String vehiculoToJson(Vehiculo v) {
        return String.format(
            "{\"id\":\"%s\",\"placa\":\"%s\",\"tipo\":\"%s\",\"marca\":\"%s\",\"modelo\":\"%s\"," +
            "\"anio\":%d,\"color\":\"%s\",\"propietario\":\"%s\",\"telefono\":\"%s\"," +
            "\"estado\":\"%s\",\"costoBase\":%.0f}",
            v.getId(), v.getPlaca(), v.getTipoVehiculo(), v.getMarca(), v.getModelo(),
            v.getAnio(), v.getColor(), v.getPropietario(), v.getTelefono(),
            v.getEstado().name(), v.calcularCostoBase()
        );
    }

    private static String vehiculosToJson(List<Vehiculo> lista) {
        return "[" + lista.stream().map(Main::vehiculoToJson).collect(Collectors.joining(",")) + "]";
    }

    private static String ordenToJson(OrdenServicio o) {
        return String.format(
            "{\"idOrden\":\"%s\",\"placa\":\"%s\",\"fechaIngreso\":\"%s\"," +
            "\"descripcion\":\"%s\",\"mecanico\":\"%s\",\"costoTotal\":%.0f,\"estado\":\"%s\"}",
            o.getIdOrden(), o.getPlacaVehiculo(), o.getFechaIngreso(),
            o.getDescripcionProblema(), o.getMecanico(), o.getCostoTotal(), o.getEstado()
        );
    }

    private static String ordenesToJson(List<OrdenServicio> lista) {
        return "[" + lista.stream().map(Main::ordenToJson).collect(Collectors.joining(",")) + "]";
    }

    private static void cargarDatosDemo() {
        Carro c1 = new Carro("V-001","FPR625","Renault","Sandero",2019,
                "Javier Castro","3001234567","Azul",4,"MANUAL");
        Moto m1 = new Moto("V-002", "AUM95D", "Pulsar", "Ns", 2014, 
                "Sebastian Quintero", "3143336407", "Amarillo", 200, "SCOOTER");
        Camioneta cam1 = new Camioneta("V-003","SYS204","Toyota","Prado",2018,
                "Leo Cubillos","3157050265","Blanco",4.0,false);
        servicio.registrarVehiculo(c1);
        servicio.registrarVehiculo(m1);
        servicio.registrarVehiculo(cam1);

        OrdenServicio o1 = servicio.crearOrden("DFS254","Cambio de aceite y filtros","Jorge Méndez");
        o1.agregarServicio("Cambio aceite",85000);
        o1.agregarServicio("Filtro aire",25000);

        OrdenServicio o2 = servicio.crearOrden("JSY","Frenada deficiente","Ana Torres");
        o2.agregarServicio("Pastillas freno",200000);
        servicio.cerrarOrden(o2.getIdOrden());
    }
}
