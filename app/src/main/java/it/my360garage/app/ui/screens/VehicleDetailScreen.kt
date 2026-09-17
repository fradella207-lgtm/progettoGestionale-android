package it.my360garage.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import it.my360garage.app.data.model.*
import it.my360garage.app.ui.theme.*

@Composable
fun VehicleDetailScreen(
    vehicle: Vehicle?,
    onAddRefuel: () -> Unit,
    onDeleteRefuel: (String) -> Unit,
    onAddMaintenance: () -> Unit,
    onDeleteMaintenance: (String) -> Unit,
    onAddDocument: () -> Unit,
    onDeleteDocument: (String) -> Unit
) {
    if (vehicle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nessun veicolo selezionato.")
        }
        return
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Scheda", "Rifornimenti", "Interventi", "Documenti", "Spese")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // 1. Vehicle Hero Card
        item {
            VehicleHeroCard(vehicle = vehicle)
        }

        // 2. Tabs Selector
        item {
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
        }

        // 3. Tab Contents
        when (selectedTab) {
            0 -> {
                // Technical Specs
                item {
                    VehicleTechnicalSpecsSection(vehicle = vehicle)
                }
            }
            1 -> {
                // Refuels
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Rifornimenti (${vehicle.refuels.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = onAddRefuel,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Nuovo Rifornimento")
                        }
                    }
                }

                if (vehicle.refuels.isEmpty()) {
                    item {
                        EmptyPlaceholderCard(
                            icon = Icons.Default.LocalGasStation,
                            title = "Nessun rifornimento registrato",
                            subtitle = "Tocca 'Nuovo Rifornimento' per calcolare i consumi reali della tua ${vehicle.brand}."
                        )
                    }
                } else {
                    items(vehicle.refuels, key = { it.id }) { refuel ->
                        RefuelItemCard(refuel = refuel, onDelete = { onDeleteRefuel(refuel.id) })
                    }
                }
            }
            2 -> {
                // Maintenances
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Registro Manutenzioni (${vehicle.maintenances.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = onAddMaintenance,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Registra Lavori")
                        }
                    }
                }

                if (vehicle.maintenances.isEmpty()) {
                    item {
                        EmptyPlaceholderCard(
                            icon = Icons.Default.Build,
                            title = "Nessun intervento registrato",
                            subtitle = "Registra tagliandi, pastiglie freni, cambi gomme o revisioni per tenere traccia dello storico del veicolo."
                        )
                    }
                } else {
                    items(vehicle.maintenances, key = { it.id }) { maint ->
                        MaintenanceItemCard(maint = maint, onDelete = { onDeleteMaintenance(maint.id) })
                    }
                }
            }
            3 -> {
                // Documents & Deadlines
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Scadenze & Documenti (${vehicle.documents.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = onAddDocument,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Aggiungi Scadenza")
                        }
                    }
                }

                if (vehicle.documents.isEmpty()) {
                    item {
                        EmptyPlaceholderCard(
                            icon = Icons.Default.Description,
                            title = "Nessun documento inserito",
                            subtitle = "Aggiungi scadenze di bollo, assicurazione o revisione per ricevere notifiche e promemoria automatici."
                        )
                    }
                } else {
                    items(vehicle.documents, key = { it.id }) { doc ->
                        DocumentItemCard(doc = doc, onDelete = { onDeleteDocument(doc.id) })
                    }
                }
            }
            4 -> {
                // Expenses breakdown
                item {
                    VehicleExpensesBreakdown(vehicle = vehicle)
                }
            }
        }
    }
}

@Composable
fun VehicleHeroCard(vehicle: Vehicle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (vehicle.photoUrl.isNotBlank()) {
                AsyncImage(
                    model = vehicle.photoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Slate800),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (vehicle.vehicleType == "moto") Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = Slate500,
                        modifier = Modifier.size(72.dp)
                    )
                }
            }

            // Dark gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Black.copy(alpha = 0.85f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = vehicle.plate,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "${vehicle.brand} ${vehicle.model}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (vehicle.trimLevel.isNotBlank()) {
                    Text(
                        text = vehicle.trimLevel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate200
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Chilometri", style = MaterialTheme.typography.labelSmall, color = Slate400)
                        Text(
                            text = "${String.format("%,.0f", vehicle.currentKm)} km",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Column {
                        Text("Consumo Medio", style = MaterialTheme.typography.labelSmall, color = Slate400)
                        val cons = vehicle.averageConsumptionLitersPer100Km
                        Text(
                            text = if (cons > 0) "${String.format("%.1f", cons)} L/100km" else "N/D",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSuccess
                        )
                    }
                    Column {
                        Text("Spese Totali", style = MaterialTheme.typography.labelSmall, color = Slate400)
                        Text(
                            text = "${String.format("%.2f", vehicle.totalExpenses)} €",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BlueAccent
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VehicleTechnicalSpecsSection(vehicle: Vehicle) {
    val specs = vehicle.technicalSpecs

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Dati Tecnici Motore & Trazione",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                SpecRow("Alimentazione", vehicle.fuelType)
                SpecRow("Potenza", "${vehicle.powerCv} CV (${vehicle.powerKw} kW)")
                specs?.engineDisplacementCc?.let { SpecRow("Cilindrata", "$it cm³") }
                specs?.torqueNm?.let { SpecRow("Coppia Massima", "$it Nm") }
                SpecRow("Capacità Serbatoio", "${vehicle.tankCapacity} Litri / kWh")
                specs?.transmission?.let { SpecRow("Trasmissione", it) }
                specs?.drivetrain?.let { SpecRow("Trazione", it) }
                specs?.euroClass?.let { SpecRow("Omologazione", it) }
                specs?.wltpConsumption?.let { SpecRow("Consumo Omologato", it) }
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Manutenzione & Specifiche Fluidi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                specs?.recommendedOil?.let { SpecRow("Olio Raccomandato", it) }
                specs?.oilCapacityLiters?.let { SpecRow("Capacità Coppa Olio", "$it litri") }
                specs?.tirePressureFrontBar?.let {
                    SpecRow("Pressione Anteriore", "$it bar")
                }
                specs?.tirePressureRearBar?.let {
                    SpecRow("Pressione Posteriore", "$it bar")
                }
                specs?.allowedTireSizes?.let { sizes ->
                    SpecRow("Misure Pneumatici", sizes.joinToString("\n"))
                }
            }
        }

        specs?.summaryQuattroruote?.let { summary ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Note del Costruttore / Portale Auto",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = summary, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Slate500, modifier = Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1.4f))
    }
}

@Composable
fun RefuelItemCard(refuel: RefuelRecord, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = refuel.date, style = MaterialTheme.typography.labelMedium, color = Slate500)
                    Surface(
                        color = if (refuel.type == "full") EmeraldSuccess.copy(alpha = 0.15f) else BlueAccent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (refuel.type == "full") "Pieno" else "Parziale",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (refuel.type == "full") EmeraldSuccess else BlueAccent,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "${refuel.quantity} ${refuel.unit} @ ${String.format("%.3f", refuel.pricePerUnit)} €/${refuel.unit}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Odometro: ${String.format("%,.0f", refuel.km)} km ${if (refuel.notes.isNotBlank()) "• ${refuel.notes}" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate500
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${String.format("%.2f", refuel.price)} €",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Elimina", tint = Slate400, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun MaintenanceItemCard(maint: MaintenanceRecord, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = maint.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "${String.format("%.2f", maint.cost)} €",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = maint.description,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Data: ${maint.date} • ${String.format("%,.0f", maint.km)} km",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                    if (maint.workshop.isNotBlank()) {
                        Text(
                            text = "Officina: ${maint.workshop}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate500
                        )
                    }
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Elimina", tint = Slate400, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun DocumentItemCard(doc: VehicleDocument, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = doc.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (!doc.expiryDate.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Event, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(14.dp))
                        Text(
                            text = "Scadenza: ${doc.expiryDate}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = AmberWarning
                        )
                    }
                }
                if (doc.notes.isNotBlank()) {
                    Text(
                        text = doc.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Elimina", tint = Slate400, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun VehicleExpensesBreakdown(vehicle: Vehicle) {
    val fuelTotal = vehicle.totalFuelSpent
    val maintTotal = vehicle.totalMaintenanceSpent
    val grandTotal = fuelTotal + maintTotal
    val totalKmDriven = vehicle.currentKm - vehicle.initialKm
    val costPerKm = if (totalKmDriven > 0) grandTotal / totalKmDriven else 0.0

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Riepilogo Economico Veicolo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Spesa Carburante / Ricariche:", style = MaterialTheme.typography.bodyMedium)
                    Text("${String.format("%.2f", fuelTotal)} €", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Spesa Tagliandi & Manutenzioni:", style = MaterialTheme.typography.bodyMedium)
                    Text("${String.format("%.2f", maintTotal)} €", fontWeight = FontWeight.Bold, color = BlueAccent)
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Costo Totale Gestione:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("${String.format("%.2f", grandTotal)} €", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                }

                if (costPerKm > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Costo medio per km percorso:", style = MaterialTheme.typography.labelMedium)
                            Text("${String.format("%.3f", costPerKm)} €/km", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyPlaceholderCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Slate400, modifier = Modifier.size(44.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Slate500, modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}
