package it.my360garage.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.my360garage.app.data.model.Vehicle
import it.my360garage.app.ui.GarageViewModel
import it.my360garage.app.ui.components.*
import it.my360garage.app.ui.screens.*
import it.my360garage.app.ui.theme.My360GarageTheme
import it.my360garage.app.ui.theme.RoseError

class MainActivity : ComponentActivity() {

    private val viewModel: GarageViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            My360GarageTheme {
                val vehicles by viewModel.vehicles.collectAsState()
                val selectedVehicle by viewModel.selectedVehicle.collectAsState()
                val filteredStations by viewModel.filteredStations.collectAsState()
                val stationSearchQuery by viewModel.stationSearchQuery.collectAsState()
                val selectedFuelFilter by viewModel.selectedFuelFilter.collectAsState()
                val highwayFilter by viewModel.highwayFilter.collectAsState()
                val chatMessages by viewModel.chatMessages.collectAsState()
                val deadlineAlerts by viewModel.deadlineAlerts.collectAsState()

                var currentTab by remember { mutableIntStateOf(0) }
                // 0: Garage, 1: Veicolo, 2: Distributori, 3: Assistente AI

                // Dialog States
                var showAddVehicleDialog by remember { mutableStateOf(false) }
                var vehicleForRefuelDialog by remember { mutableStateOf<Vehicle?>(null) }
                var vehicleForMaintenanceDialog by remember { mutableStateOf<Vehicle?>(null) }
                var vehicleForDocumentDialog by remember { mutableStateOf<Vehicle?>(null) }
                var showNotificationsDialog by remember { mutableStateOf(false) }
                var showSettingsDialog by remember { mutableStateOf(false) }

                var vehicleDropdownExpanded by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.app_logo),
                                        contentDescription = "Logo",
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Text(
                                        text = "My360Garage",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            actions = {
                                // Vehicle selector chip
                                if (vehicles.isNotEmpty() && (currentTab == 0 || currentTab == 1 || currentTab == 3)) {
                                    Box {
                                        AssistChip(
                                            onClick = { vehicleDropdownExpanded = true },
                                            label = {
                                                Text(
                                                    text = selectedVehicle?.plate ?: "Seleziona",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    maxLines = 1
                                                )
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    if (selectedVehicle?.vehicleType == "moto") Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            },
                                            trailingIcon = {
                                                Icon(
                                                    Icons.Default.ArrowDropDown,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            },
                                            modifier = Modifier.testTag("vehicle_selector_chip")
                                        )

                                        DropdownMenu(
                                            expanded = vehicleDropdownExpanded,
                                            onDismissRequest = { vehicleDropdownExpanded = false }
                                        ) {
                                            vehicles.forEach { car ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Column {
                                                            Text("${car.brand} ${car.model}", fontWeight = FontWeight.Bold)
                                                            Text(car.plate, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                        }
                                                    },
                                                    onClick = {
                                                        viewModel.selectVehicle(car.id)
                                                        vehicleDropdownExpanded = false
                                                    }
                                                )
                                            }
                                            HorizontalDivider()
                                            DropdownMenuItem(
                                                text = { Text("+ Aggiungi Veicolo", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) },
                                                onClick = {
                                                    vehicleDropdownExpanded = false
                                                    showAddVehicleDialog = true
                                                }
                                            )
                                        }
                                    }
                                }

                                // Notifications Icon with Badge
                                IconButton(onClick = { showNotificationsDialog = true }) {
                                    BadgedBox(
                                        badge = {
                                            if (deadlineAlerts.isNotEmpty()) {
                                                Badge(containerColor = RoseError) {
                                                    Text("${deadlineAlerts.size}")
                                                }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Notifications,
                                            contentDescription = "Notifiche & Scadenze"
                                        )
                                    }
                                }

                                // Settings Icon
                                IconButton(onClick = { showSettingsDialog = true }) {
                                    Icon(
                                        Icons.Default.Settings,
                                        contentDescription = "Impostazioni"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 6.dp
                        ) {
                            NavigationBarItem(
                                selected = currentTab == 0,
                                onClick = { currentTab = 0 },
                                icon = { Icon(Icons.Default.Garage, contentDescription = "Garage") },
                                label = { Text("Garage") },
                                modifier = Modifier.testTag("nav_garage")
                            )

                            NavigationBarItem(
                                selected = currentTab == 1,
                                onClick = { currentTab = 1 },
                                icon = { Icon(Icons.Default.DirectionsCar, contentDescription = "Veicolo") },
                                label = { Text("Veicolo") },
                                modifier = Modifier.testTag("nav_vehicle")
                            )

                            NavigationBarItem(
                                selected = currentTab == 2,
                                onClick = { currentTab = 2 },
                                icon = { Icon(Icons.Default.LocalGasStation, contentDescription = "Distributori") },
                                label = { Text("Distributori") },
                                modifier = Modifier.testTag("nav_stations")
                            )

                            NavigationBarItem(
                                selected = currentTab == 3,
                                onClick = { currentTab = 3 },
                                icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI") },
                                label = { Text("Assistente AI") },
                                modifier = Modifier.testTag("nav_ai")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentTab) {
                            0 -> GarageScreen(
                                vehicles = vehicles,
                                alerts = deadlineAlerts,
                                onSelectVehicle = { id -> viewModel.selectVehicle(id) },
                                onNavigateToVehicle = { id ->
                                    viewModel.selectVehicle(id)
                                    currentTab = 1
                                },
                                onAddRefuel = { car -> vehicleForRefuelDialog = car },
                                onAddMaintenance = { car -> vehicleForMaintenanceDialog = car },
                                onDeleteVehicle = { id -> viewModel.deleteVehicle(id) },
                                onOpenAddVehicle = { showAddVehicleDialog = true }
                            )
                            1 -> VehicleDetailScreen(
                                vehicle = selectedVehicle,
                                onAddRefuel = { vehicleForRefuelDialog = selectedVehicle },
                                onDeleteRefuel = { refuelId ->
                                    selectedVehicle?.let { viewModel.deleteRefuel(it.id, refuelId) }
                                },
                                onAddMaintenance = { vehicleForMaintenanceDialog = selectedVehicle },
                                onDeleteMaintenance = { maintId ->
                                    selectedVehicle?.let { viewModel.deleteMaintenance(it.id, maintId) }
                                },
                                onAddDocument = { vehicleForDocumentDialog = selectedVehicle },
                                onDeleteDocument = { docId ->
                                    selectedVehicle?.let { viewModel.deleteDocument(it.id, docId) }
                                }
                            )
                            2 -> StationsScreen(
                                stations = filteredStations,
                                searchQuery = stationSearchQuery,
                                onSearchQueryChange = { q -> viewModel.setStationSearchQuery(q) },
                                selectedFuelFilter = selectedFuelFilter,
                                onFuelFilterChange = { f -> viewModel.setSelectedFuelFilter(f) },
                                highwayOnly = highwayFilter,
                                onToggleHighwayOnly = { viewModel.toggleHighwayFilter() }
                            )
                            3 -> AIAssistantScreen(
                                vehicle = selectedVehicle,
                                messages = chatMessages,
                                onSendMessage = { text -> viewModel.sendChatMessage(text) }
                            )
                        }
                    }
                }

                // Dialogs
                if (showAddVehicleDialog) {
                    AddVehicleDialog(
                        onDismiss = { showAddVehicleDialog = false },
                        onConfirm = { newCar ->
                            viewModel.addVehicle(newCar)
                            showAddVehicleDialog = false
                            currentTab = 1 // Navigate to newly added car
                        }
                    )
                }

                vehicleForRefuelDialog?.let { car ->
                    AddRefuelDialog(
                        vehicle = car,
                        onDismiss = { vehicleForRefuelDialog = null },
                        onConfirm = { refuel ->
                            viewModel.addRefuel(car.id, refuel)
                            vehicleForRefuelDialog = null
                        }
                    )
                }

                vehicleForMaintenanceDialog?.let { car ->
                    AddMaintenanceDialog(
                        vehicle = car,
                        onDismiss = { vehicleForMaintenanceDialog = null },
                        onConfirm = { maint ->
                            viewModel.addMaintenance(car.id, maint)
                            vehicleForMaintenanceDialog = null
                        }
                    )
                }

                vehicleForDocumentDialog?.let { car ->
                    AddDocumentDialog(
                        vehicle = car,
                        onDismiss = { vehicleForDocumentDialog = null },
                        onConfirm = { doc ->
                            viewModel.addDocument(car.id, doc)
                            vehicleForDocumentDialog = null
                        }
                    )
                }

                if (showNotificationsDialog) {
                    NotificationsDialog(
                        alerts = deadlineAlerts,
                        onDismiss = { showNotificationsDialog = false }
                    )
                }

                if (showSettingsDialog) {
                    SettingsDialog(
                        totalVehiclesCount = vehicles.size,
                        totalExpensesSum = vehicles.sumOf { it.totalExpenses },
                        onDismiss = { showSettingsDialog = false }
                    )
                }
            }
        }
    }
}
