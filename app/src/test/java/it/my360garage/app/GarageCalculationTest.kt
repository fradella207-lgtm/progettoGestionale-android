package it.my360garage.app

import it.my360garage.app.data.model.MaintenanceRecord
import it.my360garage.app.data.model.RefuelRecord
import it.my360garage.app.data.model.Vehicle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GarageCalculationTest {

    @Test
    fun testVehicleOdometerCalculation() {
        val vehicle = Vehicle(
            brand = "Alfa Romeo",
            model = "Giulia",
            plate = "GA 892 TR",
            initialKm = 50000.0,
            refuels = listOf(
                RefuelRecord(date = "2026-08-01", km = 52000.0, quantity = 40.0, price = 70.0),
                RefuelRecord(date = "2026-08-15", km = 53500.0, quantity = 42.0, price = 73.5)
            ),
            maintenances = listOf(
                MaintenanceRecord(date = "2026-08-10", km = 52500.0, category = "Tagliando", description = "Cambio olio", cost = 200.0)
            )
        )

        assertEquals(53500.0, vehicle.currentKm, 0.001)
        assertEquals(143.5, vehicle.totalFuelSpent, 0.001)
        assertEquals(200.0, vehicle.totalMaintenanceSpent, 0.001)
        assertEquals(343.5, vehicle.totalExpenses, 0.001)
    }

    @Test
    fun testAverageConsumptionCalculation() {
        val vehicle = Vehicle(
            brand = "Volkswagen",
            model = "Golf",
            plate = "FY 412 KL",
            initialKm = 20000.0,
            refuels = listOf(
                RefuelRecord(date = "2026-08-01", km = 20000.0, quantity = 40.0, price = 70.0),
                RefuelRecord(date = "2026-08-15", km = 20800.0, quantity = 48.0, price = 84.0)
            )
        )

        // Driven = 800 km, second refuel quantity = 48 L.
        // Consumption = (48 / 800) * 100 = 6.0 L / 100 km
        val consumption = vehicle.averageConsumptionLitersPer100Km
        assertEquals(6.0, consumption, 0.01)

        val kmL = vehicle.kmPerLiter
        assertEquals(100.0 / 6.0, kmL, 0.01)
    }

    @Test
    fun testRefuelPricePerUnit() {
        val refuel = RefuelRecord(
            date = "2026-08-20",
            km = 10000.0,
            quantity = 50.0,
            price = 90.0
        )
        assertEquals(1.80, refuel.pricePerUnit, 0.001)
    }
}
