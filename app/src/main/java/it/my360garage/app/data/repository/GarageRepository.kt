package it.my360garage.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import it.my360garage.app.data.model.*
import it.my360garage.app.data.seed.SeedData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class GarageRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("my360garage_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = false }

    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles.asStateFlow()

    private val _selectedVehicleId = MutableStateFlow<String>("")
    val selectedVehicleId: StateFlow<String> = _selectedVehicleId.asStateFlow()

    private val _stations = MutableStateFlow<List<Station>>(SeedData.initialStations)
    val stations: StateFlow<List<Station>> = _stations.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val vehiclesJson = prefs.getString("vehicles_json", null)
        val loadedVehicles: List<Vehicle> = if (!vehiclesJson.isNullOrBlank()) {
            try {
                json.decodeFromString(vehiclesJson)
            } catch (e: Exception) {
                SeedData.initialVehicles
            }
        } else {
            SeedData.initialVehicles
        }
        _vehicles.value = loadedVehicles

        val savedCarId = prefs.getString("selected_car_id", null)
        if (!savedCarId.isNullOrBlank() && loadedVehicles.any { it.id == savedCarId }) {
            _selectedVehicleId.value = savedCarId
        } else {
            _selectedVehicleId.value = loadedVehicles.firstOrNull()?.id ?: ""
        }

        // Initialize chat with a welcome message for the selected car
        val currentCar = getSelectedVehicle()
        val carName = currentCar?.let { "${it.brand} ${it.model}" } ?: "il tuo veicolo"
        _chatMessages.value = listOf(
            ChatMessage(
                role = "assistant",
                content = "Ciao! Sono l'Assistente Tecnico My360Garage per $carName. Come posso aiutarti oggi? Puoi chiedermi informazioni sulle specifiche dell'olio, pressioni pneumatici, scadenze manutenzione, azzeramento spie o consigli di consumo.",
                timestamp = "Ora"
            )
        )
    }

    private fun saveVehicles() {
        try {
            val encoded = json.encodeToString(_vehicles.value)
            prefs.edit().putString("vehicles_json", encoded).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectVehicle(id: String) {
        if (_vehicles.value.any { it.id == id }) {
            _selectedVehicleId.value = id
            prefs.edit().putString("selected_car_id", id).apply()
        }
    }

    fun getSelectedVehicle(): Vehicle? {
        val currentId = _selectedVehicleId.value
        return _vehicles.value.firstOrNull { it.id == currentId } ?: _vehicles.value.firstOrNull()
    }

    fun addVehicle(vehicle: Vehicle) {
        val updated = _vehicles.value + vehicle
        _vehicles.value = updated
        _selectedVehicleId.value = vehicle.id
        saveVehicles()
    }

    fun updateVehicle(vehicle: Vehicle) {
        val updated = _vehicles.value.map { if (it.id == vehicle.id) vehicle else it }
        _vehicles.value = updated
        saveVehicles()
    }

    fun deleteVehicle(vehicleId: String) {
        val updated = _vehicles.value.filterNot { it.id == vehicleId }
        _vehicles.value = updated
        if (_selectedVehicleId.value == vehicleId) {
            _selectedVehicleId.value = updated.firstOrNull()?.id ?: ""
        }
        saveVehicles()
    }

    fun addRefuel(vehicleId: String, refuel: RefuelRecord) {
        val currentCar = _vehicles.value.firstOrNull { it.id == vehicleId } ?: return
        val updatedRefuels = (currentCar.refuels + refuel).sortedByDescending { it.km }
        val updatedCar = currentCar.copy(refuels = updatedRefuels)
        updateVehicle(updatedCar)
    }

    fun deleteRefuel(vehicleId: String, refuelId: String) {
        val currentCar = _vehicles.value.firstOrNull { it.id == vehicleId } ?: return
        val updatedRefuels = currentCar.refuels.filterNot { it.id == refuelId }
        val updatedCar = currentCar.copy(refuels = updatedRefuels)
        updateVehicle(updatedCar)
    }

    fun addMaintenance(vehicleId: String, maintenance: MaintenanceRecord) {
        val currentCar = _vehicles.value.firstOrNull { it.id == vehicleId } ?: return
        val updatedMaintenances = (currentCar.maintenances + maintenance).sortedByDescending { it.km }
        val updatedCar = currentCar.copy(maintenances = updatedMaintenances)
        updateVehicle(updatedCar)
    }

    fun deleteMaintenance(vehicleId: String, maintenanceId: String) {
        val currentCar = _vehicles.value.firstOrNull { it.id == vehicleId } ?: return
        val updatedMaintenances = currentCar.maintenances.filterNot { it.id == maintenanceId }
        val updatedCar = currentCar.copy(maintenances = updatedMaintenances)
        updateVehicle(updatedCar)
    }

    fun addDocument(vehicleId: String, document: VehicleDocument) {
        val currentCar = _vehicles.value.firstOrNull { it.id == vehicleId } ?: return
        val updatedDocuments = currentCar.documents + document
        val updatedCar = currentCar.copy(documents = updatedDocuments)
        updateVehicle(updatedCar)
    }

    fun deleteDocument(vehicleId: String, documentId: String) {
        val currentCar = _vehicles.value.firstOrNull { it.id == vehicleId } ?: return
        val updatedDocuments = currentCar.documents.filterNot { it.id == documentId }
        val updatedCar = currentCar.copy(documents = updatedDocuments)
        updateVehicle(updatedCar)
    }

    fun getDeadlineAlerts(): List<DeadlineAlert> {
        val alerts = mutableListOf<DeadlineAlert>()
        val today = LocalDate.now()

        for (vehicle in _vehicles.value) {
            // Check document expiration dates
            for (doc in vehicle.documents) {
                if (!doc.expiryDate.isNullOrBlank()) {
                    try {
                        val exp = LocalDate.parse(doc.expiryDate)
                        val days = ChronoUnit.DAYS.between(today, exp).toInt()
                        if (days < 0) {
                            alerts.add(
                                DeadlineAlert(
                                    id = "exp_${vehicle.id}_${doc.id}",
                                    vehiclePlate = vehicle.plate,
                                    vehicleName = "${vehicle.brand} ${vehicle.model}",
                                    title = "${doc.title} Scaduto",
                                    message = "Scaduto da ${Math.abs(days)} giorni (${doc.expiryDate}). Rinnova subito!",
                                    type = "urgent",
                                    daysLeft = days
                                )
                            )
                        } else if (days <= 45) {
                            alerts.add(
                                DeadlineAlert(
                                    id = "warn_${vehicle.id}_${doc.id}",
                                    vehiclePlate = vehicle.plate,
                                    vehicleName = "${vehicle.brand} ${vehicle.model}",
                                    title = "Scadenza ${doc.title}",
                                    message = "In scadenza tra $days giorni (${doc.expiryDate}).",
                                    type = if (days <= 15) "warning" else "info",
                                    daysLeft = days
                                )
                            )
                        }
                    } catch (e: Exception) {
                        // ignore malformed date
                    }
                }
            }

            // Check maintenance interval (tagliando every 15,000 km)
            val lastTagliando = vehicle.maintenances
                .filter { it.category.contains("Tagliando", ignoreCase = true) }
                .maxOfOrNull { it.km } ?: vehicle.initialKm
            val kmSinceTagliando = vehicle.currentKm - lastTagliando
            if (kmSinceTagliando >= 15000) {
                alerts.add(
                    DeadlineAlert(
                        id = "maint_${vehicle.id}",
                        vehiclePlate = vehicle.plate,
                        vehicleName = "${vehicle.brand} ${vehicle.model}",
                        title = "Tagliando Consigliato",
                        message = "Percorsi ${kmSinceTagliando.toInt()} km dall'ultimo tagliando. Controlla olio e filtri.",
                        type = "warning",
                        daysLeft = null
                    )
                )
            }
        }
        return alerts
    }

    fun sendChatMessage(userText: String) {
        val userMsg = ChatMessage(role = "user", content = userText, timestamp = "Ora")
        _chatMessages.value = _chatMessages.value + userMsg

        val currentCar = getSelectedVehicle()
        val response = generateAutomotiveResponse(userText, currentCar)
        val assistantMsg = ChatMessage(role = "assistant", content = response, timestamp = "Ora")
        _chatMessages.value = _chatMessages.value + assistantMsg
    }

    private fun generateAutomotiveResponse(prompt: String, car: Vehicle?): String {
        val lower = prompt.lowercase()
        val brand = car?.brand ?: "Veicolo"
        val model = car?.model ?: "Generico"
        val specs = car?.technicalSpecs

        return when {
            lower.contains("olio") || lower.contains("gradazione") -> {
                val oil = specs?.recommendedOil ?: "0W-20 / 5W-30 sintetico con specifiche costruttore"
                val cap = specs?.oilCapacityLiters?.let { "$it litri" } ?: "circa 4.0 - 4.5 litri"
                "Per la tua $brand $model, l'olio motore raccomandato dal costruttore è: **$oil**.\n\nCapacità coppa olio con filtro: **$cap**.\nSi consiglia di verificare sempre il livello ad asta fredda e su superficie pianeggiante."
            }
            lower.contains("pressione") || lower.contains("gomm") || lower.contains("pneumatic") -> {
                val front = specs?.tirePressureFrontBar ?: 2.3
                val rear = specs?.tirePressureRearBar ?: 2.5
                val sizes = specs?.allowedTireSizes?.joinToString(", ") ?: "Misure standard a libretto"
                "Pressioni consigliate a freddo per $brand $model:\n• Asse Anteriore: **$front bar**\n• Asse Posteriore: **$rear bar**\n(A pieno carico o in autostrada aumentare a ${front + 0.3} ant / ${rear + 0.3} post).\n\nMisure ammesse a libretto: $sizes."
            }
            lower.contains("tagliando") || lower.contains("manutenzion") -> {
                val currentKm = car?.currentKm?.toInt() ?: 0
                val nextKm = ((currentKm / 15000) + 1) * 15000
                "Per $brand $model l'intervallo di manutenzione programmata (Tagliando) è fissato ogni **15.000 - 20.000 km** o **12 mesi**.\n\nChilometraggio attuale: **$currentKm km**.\nProssimo tagliando previsto attorno a **$nextKm km** con sostituzione olio motore, filtro olio, filtro aria e controllo usura pastiglie freni."
            }
            lower.contains("consum") || lower.contains("risparmi") -> {
                val cons = car?.averageConsumptionLitersPer100Km ?: 0.0
                val kmL = car?.kmPerLiter ?: 0.0
                val consStr = if (cons > 0) "Attualmente il tuo veicolo registra una media di **${String.format("%.1f", cons)} L/100 km** (~${String.format("%.1f", kmL)} km/L).\n\n" else ""
                "${consStr}Consigli per ottimizzare i consumi su $brand $model:\n1. Mantieni la corretta pressione pneumatici (+0.2 bar riduce l'attrito di rotolamento).\n2. Sfrutta il freno motore e l'inerzia evitando accelerazioni brusche.\n3. Rimuovi carichi non necessari e portapacchi inutilizzati.\n4. Utilizza il cruise control sui tratti extraurbani ed autostradali."
            }
            lower.contains("spia") || lower.contains("reset") || lower.contains("azzeramento") -> {
                "Procedura per reset avviso service / spia su $brand $model:\n1. Inserisci il quadro strumenti senza avviare il motore (Start senza premere freno/frizione).\n2. Dal menù del computer di bordo (tasti al volante), scorri su 'Manutenzione' o 'Stato Veicolo'.\n3. Tieni premuto il tasto OK/Set per 5-10 secondi finché l'intervallo non viene ripristinato.\n\nSe si tratta della spia motore arancione (MIL/Check Engine), si raccomanda una lettura della memoria guasti tramite porta diagnosi OBD2."
            }
            else -> {
                "In base alla scheda tecnica di $brand $model ($plateDetails):\n• Motorizzazione: ${car?.motorization?.ifBlank { car.fuelType } ?: car?.fuelType}\n• Potenza: ${car?.powerCv} CV (${car?.powerKw} kW)\n• Serbatoio: ${car?.tankCapacity} L / kWh\n\nPosso fornirti informazioni dettagliate su scadenze amministrative (bollo, assicurazione, revisione), storico rifornimenti e guida economica!"
            }
        }
    }

    private val plateDetails: String
        get() = getSelectedVehicle()?.let { "${it.plate} - ${it.brand} ${it.model}" } ?: ""
}
