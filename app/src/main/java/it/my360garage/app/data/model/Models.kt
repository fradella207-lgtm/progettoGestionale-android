package it.my360garage.app.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Vehicle(
    val id: String = UUID.randomUUID().toString(),
    val vehicleType: String = "car", // "car" | "moto"
    val brand: String,
    val model: String,
    val trimLevel: String = "",
    val generation: String = "",
    val plate: String,
    val vin: String = "",
    val fuelType: String = "Benzina", // "Benzina", "Diesel", "Full / Mild Hybrid", "Plug-in Hybrid (PHEV)", "Elettrica (BEV)", "GPL", "Metano"
    val tankCapacity: Double = 50.0, // Liters or kWh
    val batteryCapacity: Double? = null,
    val motorization: String = "",
    val driveType: String = "",
    val powerCv: Int = 120,
    val powerKw: Int = 88,
    val registrationDate: String = "2022-01-01",
    val initialKm: Double = 0.0,
    val photoUrl: String = "",
    val refuels: List<RefuelRecord> = emptyList(),
    val maintenances: List<MaintenanceRecord> = emptyList(),
    val documents: List<VehicleDocument> = emptyList(),
    val technicalSpecs: VehicleTechnicalSpecs? = null
) {
    val currentKm: Double
        get() {
            val maxRefuel = refuels.maxOfOrNull { it.km } ?: 0.0
            val maxMaint = maintenances.maxOfOrNull { it.km } ?: 0.0
            return maxOf(initialKm, maxRefuel, maxMaint)
        }

    val totalFuelSpent: Double
        get() = refuels.sumOf { it.price }

    val totalMaintenanceSpent: Double
        get() = maintenances.sumOf { it.cost }

    val totalExpenses: Double
        get() = totalFuelSpent + totalMaintenanceSpent

    val totalFuelQuantity: Double
        get() = refuels.sumOf { it.quantity }

    val averageConsumptionLitersPer100Km: Double
        get() {
            val sortedRefuels = refuels.sortedBy { it.km }
            if (sortedRefuels.size < 2) return 0.0
            val kmDriven = sortedRefuels.last().km - sortedRefuels.first().km
            if (kmDriven <= 0) return 0.0
            val totalQuantity = sortedRefuels.drop(1).sumOf { it.quantity }
            return (totalQuantity / kmDriven) * 100.0
        }

    val kmPerLiter: Double
        get() {
            val cons = averageConsumptionLitersPer100Km
            return if (cons > 0) 100.0 / cons else 0.0
        }
}

@Serializable
data class RefuelRecord(
    val id: String = UUID.randomUUID().toString(),
    val date: String, // YYYY-MM-DD
    val km: Double,
    val quantity: Double, // Liters or kWh
    val price: Double, // Total €
    val type: String = "full", // "full" | "partial"
    val energyType: String = "fuel", // "fuel" | "electricity" | "lpg" | "cng"
    val unit: String = "L",
    val notes: String = ""
) {
    val pricePerUnit: Double
        get() = if (quantity > 0) price / quantity else 0.0
}

@Serializable
data class MaintenanceRecord(
    val id: String = UUID.randomUUID().toString(),
    val date: String, // YYYY-MM-DD
    val km: Double,
    val category: String, // Tagliando, Freni, Gomme, Batteria, Revisione, Altro
    val description: String,
    val workshop: String = "",
    val cost: Double = 0.0,
    val notes: String = ""
)

@Serializable
data class VehicleDocument(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val type: String = "libretto", // "libretto", "assicurazione", "bollo", "tagliando", "altro"
    val fileName: String = "",
    val expiryDate: String? = null, // YYYY-MM-DD
    val notes: String = ""
)

@Serializable
data class VehicleTechnicalSpecs(
    val engineDisplacementCc: Int? = null,
    val powerCv: Int? = null,
    val powerKw: Int? = null,
    val torqueNm: Int? = null,
    val transmission: String? = null,
    val drivetrain: String? = null,
    val euroClass: String? = null,
    val fuelCapacityLiters: Double? = null,
    val wltpConsumption: String? = null,
    val wltpRangeKm: Int? = null,
    val recommendedOil: String? = null,
    val oilCapacityLiters: Double? = null,
    val tirePressureFrontBar: Double? = null,
    val tirePressureRearBar: Double? = null,
    val allowedTireSizes: List<String>? = null,
    val summaryQuattroruote: String? = null
)

@Serializable
data class Station(
    val id: String,
    val name: String,
    val brand: String,
    val type: String = "fuel", // "fuel" | "ev" | "both"
    val address: String,
    val city: String,
    val province: String,
    val lat: Double,
    val lng: Double,
    val isHighway: Boolean = false,
    val highwayName: String? = null,
    val highwayDirection: String? = null,
    val isOpen24h: Boolean = true,
    val hasCarWash: Boolean = false,
    val hasBar: Boolean = false,
    val hasShop: Boolean = false,
    val rating: Double = 4.5,
    val fuelPrices: List<FuelPriceItem> = emptyList(),
    val evPlugs: List<EVPlugItem> = emptyList(),
    val priceHistory: List<PriceHistoryPoint> = emptyList()
)

@Serializable
data class FuelPriceItem(
    val fuel: String, // "Benzina", "Diesel", "GPL", "Metano"
    val price: Double,
    val isSelf: Boolean = true,
    val updatedAt: String = "Oggi"
)

@Serializable
data class EVPlugItem(
    val type: String, // "CCS Combo 2 (DC)", "Type 2 (AC)", "Tesla Supercharger"
    val powerKw: Int,
    val pricePerKwh: Double,
    val availableCount: Int,
    val totalCount: Int,
    val status: String = "available"
)

@Serializable
data class PriceHistoryPoint(
    val date: String,
    val price: Double,
    val fuel: String,
    val isSelf: Boolean = true
)

@Serializable
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: String, // "user" | "assistant"
    val content: String,
    val timestamp: String = "Ora"
)

@Serializable
data class AppSettings(
    val unitDistance: String = "km", // "km" | "mi"
    val currency: String = "€",
    val fuelPriceAlerts: Boolean = true,
    val predictiveAlerts: Boolean = true,
    val darkTheme: Boolean = false
)

data class DeadlineAlert(
    val id: String,
    val vehiclePlate: String,
    val vehicleName: String,
    val title: String,
    val message: String,
    val type: String, // "urgent" | "warning" | "info"
    val daysLeft: Int? = null
)
