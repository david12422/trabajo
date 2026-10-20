package com.ejemplo.transporte;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TransporteController {

    private List<Vehiculo> flota;
    private Map<String, String> conductores;

    public TransporteController() {
        flota = new ArrayList<>();
        flota.add(new Bus("BUS101", 40, "Centro-Norte"));
        flota.add(new Bus("BUS102", 35, "Sur-Occidente"));
        flota.add(new Taxi("TAX201", 4, "Taxis Bogotá"));
        flota.add(new Taxi("TAX202", 4, "Taxis Libres"));
        flota.add(new Vehiculo("VEH301", 8));

        conductores = new HashMap<>();
        conductores.put("BUS101", "Carlos");
        conductores.put("BUS102", "María");
        conductores.put("TAX201", "Andrés");
    }

    // 1. Devolver toda la flota (Arreglo JSON de 5 vehículos)
    @GetMapping("/vehiculos")
    public List<Vehiculo> obtenerVehiculos() {
        return flota;
    }

    // 2. Buscar por placa (sin distinguir mayúsculas) y devolver su descripción
    @GetMapping("/vehiculos/placa/{placa}")
    public String buscarPorPlaca(@PathVariable String placa) {
        for (Vehiculo v : flota) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v.descripcion();
            }
        }
        return "No se encontró el vehículo con placa " + placa;
    }

    // 3. Devolver una lista de textos con descripción y tarifa de cada vehículo
    @GetMapping("/vehiculos/tarifa/{km}")
    public List<String> calcularTarifas(@PathVariable double km) {
        List<String> resultado = new ArrayList<>();
        for (Vehiculo v : flota) {
            double tarifa = v.calcularTarifa(km);
            resultado.add(v.descripcion() + " -> $" + tarifa);
        }
        return resultado;
    }

    // 4. Devolver los vehículos con capacidad mayor o igual a la mínima
    @GetMapping("/vehiculos/capacidad/{minima}")
    public List<Vehiculo> filtrarPorCapacidad(@PathVariable int minima) {
        List<Vehiculo> filtrados = new ArrayList<>();
        for (Vehiculo v : flota) {
            if (v.getCapacidad() >= minima) {
                filtrados.add(v);
            }
        }
        return filtrados;
    }

    // 5. Consultar el mapa de conductores
    @GetMapping("/conductor/{placa}")
    public String buscarConductor(@PathVariable String placa) {
        String clave = placa.toUpperCase();
        if (conductores.containsKey(clave)) {
            return "El conductor de " + clave + " es " + conductores.get(clave);
        }
        return "Sin conductor asignado";
    }

    // --- RETO OPCIONAL (+10 PUNTOS) ---

    // 6. Reto: Vehículo con mayor capacidad
    @GetMapping("/vehiculos/mayor-capacidad")
    public Vehiculo obtenerMayorCapacidad() {
        Vehiculo mayor = flota.get(0);
        for (Vehiculo v : flota) {
            if (v.getCapacidad() > mayor.getCapacidad()) {
                mayor = v;
            }
        }
        return mayor;
    }

    // 7. Reto extra: Suma total de capacidades
    @GetMapping("/vehiculos/total-capacidad")
    public String obtenerTotalCapacidad() {
        int suma = 0;
        for (Vehiculo v : flota) {
            suma += v.getCapacidad();
        }
        return "Capacidad total de la flota: " + suma + " pasajeros";
    }
}
