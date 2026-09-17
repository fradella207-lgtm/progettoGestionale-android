package it.my360garage.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import it.my360garage.app.data.model.DeadlineAlert
import it.my360garage.app.data.model.Vehicle
import it.my360garage.app.ui.theme.*

@Composable
fun GarageScreen(
    vehicles: List<Vehicle>,
    alerts: List<DeadlineAlert>,
    onSelectVehicle: (String) -> Unit,
    onNavigateToVehicle: (String) -> Unit,
    onAddRefuel: (Vehicle) -> Unit,
    onAddMaintenance: (Vehicle) -> Unit,
    onDeleteVehicle: (String) -> Unit,
    onOpenAddVehicle: () -> Unit
) {
    var vehicleToDelete by remember { mutableStateOf<Vehicle?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 88.dp)
        ) {
            // 1. Garage Stats Overview Card
            item {
                GarageOverviewCard(vehicles = vehicles)
            }

            // 2. Urgent Alerts Banner (if any)
            if (alerts.isNotEmpty()) {
                item {
                    AlertsBanner(alerts = alerts)
                }
            }

            // 3. Section Title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "I Tuoi Veicoli (${vehicles.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 4. Vehicle Cards
            items(vehicles, key = { it.id }) { vehicle ->
                VehicleGarageCard(
                    vehicle = vehicle,
                    onCardClick = {
                        onSelectVehicle(vehicle.id)
                        onNavigateToVehicle(vehicle.id)
                    },
                    onAddRefuelClick = { onAddRefuel(vehicle) },
                    onAddMaintenanceClick = { onAddMaintenance(vehicle) },
                    onDeleteClick = { vehicleToDelete = vehicle }
                )
            }
        }

        ExtendedFloatingActionButton(
            onClick = onOpenAddVehicle,
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text("Aggiungi Veicolo") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("add_vehicle_fab")
        )
    }

    vehicleToDelete?.let { car ->
        AlertDialog(
            onDismissRequest = { vehicleToDelete = null },
            title = { Text("Eliminare questo veicolo?") },
            text = {
                Text("Sei sicuro di voler eliminare ${car.brand} ${car.model} (${car.plate})? Questa azione cancellerà anche i relativi rifornimenti e interventi registrati.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteVehicle(car.id)
                        vehicleToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError)
                ) {
                    Text("Elimina")
                }
            },
            dismissButton = {
                TextButton(onClick = { vehicleToDelete = null }) {
                    Text("Annulla")
                }
            }
        )
    }
}

@Composable
fun GarageOverviewCard(vehicles: List<Vehicle>) {
    val totalKm = vehicles.sumOf { it.currentKm }
    val totalSpent = vehicles.sumOf { it.totalExpenses }
    val totalRefuels = vehicles.sumOf { it.refuels.size }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF312E81))
                    )
                )
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Riepilogo Garage",
                            style = MaterialTheme.typography.labelMedium,
                            color = Slate400,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${vehicles.size} Veicoli Attivi",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Surface(
                        color = Color.White.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Garage,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(26.dp)
                        )
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.12f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Km Totali", style = MaterialTheme.typography.labelSmall, color = Slate400)
                        Text(
                            text = "${String.format("%,.0f", totalKm)} km",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text("Spese Totali", style = MaterialTheme.typography.labelSmall, color = Slate400)
                        Text(
                            text = "${String.format("%.2f", totalSpent)} €",
                            style = MaterialTheme.typography.titleMedium,
                            color = EmeraldSuccess,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text("Rifornimenti", style = MaterialTheme.typography.labelSmall, color = Slate400)
                        Text(
                            text = "$totalRefuels",
                            style = MaterialTheme.typography.titleMedium,
                            color = BlueAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlertsBanner(alerts: List<DeadlineAlert>) {
    val urgentCount = alerts.count { it.type == "urgent" }
    val warningCount = alerts.size - urgentCount

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (urgentCount > 0) RoseError.copy(alpha = 0.1f) else AmberWarning.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (urgentCount > 0) Icons.Default.ErrorOutline else Icons.Default.Warning,
                contentDescription = null,
                tint = if (urgentCount > 0) RoseError else AmberWarning,
                modifier = Modifier.size(28.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (urgentCount > 0) "$urgentCount Scadenze urgenti / scadute" else "$warningCount Promemoria imminenti",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (urgentCount > 0) RoseError else AmberWarning
                )
                Text(
                    text = alerts.firstOrNull()?.let { "${it.vehiclePlate}: ${it.title}" } ?: "Controlla le notifiche",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun VehicleGarageCard(
    vehicle: Vehicle,
    onCardClick: () -> Unit,
    onAddRefuelClick: () -> Unit,
    onAddMaintenanceClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick)
            .testTag("vehicle_card_${vehicle.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Vehicle Image with Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Slate800)
            ) {
                if (vehicle.photoUrl.isNotBlank()) {
                    AsyncImage(
                        model = vehicle.photoUrl,
                        contentDescription = "${vehicle.brand} ${vehicle.model}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (vehicle.vehicleType == "moto") Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Slate400,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                            )
                        )
                )

                // Top badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (vehicle.vehicleType == "moto") "MOTO" else "AUTO",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = Color.Black.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = vehicle.plate,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Bottom title on image
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "${vehicle.brand} ${vehicle.model}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (vehicle.trimLevel.isNotBlank()) {
                        Text(
                            text = vehicle.trimLevel,
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate200
                        )
                    }
                }
            }

            // Card Body: Specifications & Stats
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = Slate500, modifier = Modifier.size(16.dp))
                        Text(
                            text = "${String.format("%,.0f", vehicle.currentKm)} km",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.LocalGasStation, contentDescription = null, tint = Slate500, modifier = Modifier.size(16.dp))
                        Text(
                            text = vehicle.fuelType,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(16.dp))
                        Text(
                            text = "${vehicle.powerCv} CV",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Average consumption badge
                val avgCons = vehicle.averageConsumptionLitersPer100Km
                if (avgCons > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Consumo medio reale:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${String.format("%.1f", avgCons)} L/100 km (~${String.format("%.1f", vehicle.kmPerLiter)} km/L)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                // Action buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onAddRefuelClick,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.LocalGasStation, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rifornimento", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onAddMaintenanceClick,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tagliando", fontSize = 12.sp)
                    }

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Elimina veicolo",
                            tint = Slate500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
