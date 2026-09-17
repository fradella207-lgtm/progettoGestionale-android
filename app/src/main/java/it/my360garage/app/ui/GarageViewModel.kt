package it.my360garage.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.my360garage.app.data.model.*
import it.my360garage.app.data.repository.GarageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GarageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GarageRepository(application.applicationContext)

    val vehicles = repository.vehicles
    val selectedVehicleId = repository.selectedVehicleId
    val stations = repository.stations
    val chatMessages = repository.chatMessages

    val selectedVehicle: StateFlow<Vehicle?> = combine(vehicles, selectedVehicleId) { list, id ->
        list.firstOrNull { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Station Filters State
    private val _stationSearchQuery = MutableStateFlow("")
    val stationSearchQuery: StateFlow<String> = _stationSearchQuery.asStateFlow()

    private val _selectedFuelFilter = MutableStateFlow("Tutti") // "Tutti", "Benzina", "Diesel", "GPL", "Metano", "Elettrico"
    val selectedFuelFilter: StateFlow<String> = _selectedFuelFilter.asStateFlow()

    private val _highwayFilter = MutableStateFlow(false)
    val highwayFilter: StateFlow<Boolean> = _highwayFilter.asStateFlow()

    val filteredStations: StateFlow<List<Station>> = combine(
        stations,
        _stationSearchQuery,
        _selectedFuelFilter,
        _highwayFilter
    ) { list, query, fuelFilter, highwayOnly ->
        list.filter { station ->
            val matchesQuery = query.isBlank() ||
                    station.name.contains(query, ignoreCase = true) ||
                    station.brand.contains(query, ignoreCase = true) ||
                    station.city.contains(query, ignoreCase = true) ||
                    station.province.contains(query, ignoreCase = true) ||
                    (station.highwayName?.contains(query, ignoreCase = true) == true)

            val matchesHighway = !highwayOnly || station.isHighway

            val matchesFuel = when (fuelFilter) {
                "Tutti" -> true
                "Elettrico" -> station.type == "ev" || station.type == "both" || station.evPlugs.isNotEmpty()
                else -> station.fuelPrices.any { it.fuel.equals(fuelFilter, ignoreCase = true) }
            }

            matchesQuery && matchesHighway && matchesFuel
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val deadlineAlerts: StateFlow<List<DeadlineAlert>> = vehicles.map {
        repository.getDeadlineAlerts()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectVehicle(id: String) {
        repository.selectVehicle(id)
    }

    fun addVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            repository.addVehicle(vehicle)
        }
    }

    fun deleteVehicle(id: String) {
        viewModelScope.launch {
            repository.deleteVehicle(id)
        }
    }

    fun addRefuel(vehicleId: String, refuel: RefuelRecord) {
        viewModelScope.launch {
            repository.addRefuel(vehicleId, refuel)
        }
    }

    fun deleteRefuel(vehicleId: String, refuelId: String) {
        viewModelScope.launch {
            repository.deleteRefuel(vehicleId, refuelId)
        }
    }

    fun addMaintenance(vehicleId: String, maintenance: MaintenanceRecord) {
        viewModelScope.launch {
            repository.addMaintenance(vehicleId, maintenance)
        }
    }

    fun deleteMaintenance(vehicleId: String, maintenanceId: String) {
        viewModelScope.launch {
            repository.deleteMaintenance(vehicleId, maintenanceId)
        }
    }

    fun addDocument(vehicleId: String, document: VehicleDocument) {
        viewModelScope.launch {
            repository.addDocument(vehicleId, document)
        }
    }

    fun deleteDocument(vehicleId: String, documentId: String) {
        viewModelScope.launch {
            repository.deleteDocument(vehicleId, documentId)
        }
    }

    fun sendChatMessage(text: String) {
        viewModelScope.launch {
            repository.sendChatMessage(text)
        }
    }

    fun setStationSearchQuery(query: String) {
        _stationSearchQuery.value = query
    }

    fun setSelectedFuelFilter(fuel: String) {
        _selectedFuelFilter.value = fuel
    }

    fun toggleHighwayFilter() {
        _highwayFilter.value = !_highwayFilter.value
    }
}
