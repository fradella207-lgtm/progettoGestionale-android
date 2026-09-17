package it.my360garage.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.my360garage.app.data.model.Station
import it.my360garage.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationsScreen(
    stations: List<Station>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFuelFilter: String,
    onFuelFilterChange: (String) -> Unit,
    highwayOnly: Boolean,
    onToggleHighwayOnly: () -> Unit
) {
    var selectedStationForDetail by remember { mutableStateOf<Station?>(null) }
    val fuelOptions = listOf("Tutti", "Benzina", "Diesel", "GPL", "Metano", "Elettrico")

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Cerca distributore, città o autostrada...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancella")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Filter Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(fuelOptions) { fuel ->
                    FilterChip(
                        selected = selectedFuelFilter == fuel,
                        onClick = { onFuelFilterChange(fuel) },
                        label = { Text(fuel) }
                    )
                }

                item {
                    FilterChip(
                        selected = highwayOnly,
                        onClick = onToggleHighwayOnly,
                        label = { Text("Solo Autostrada") },
                        leadingIcon = {
                            Icon(Icons.Default.AltRoute, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Stazioni Trovate (${stations.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Prezzi aggiornati MISE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Slate500
                    )
                }
            }

            if (stations.isEmpty()) {
                item {
                    EmptyPlaceholderCard(
                        icon = Icons.Default.LocalGasStation,
                        title = "Nessuna stazione trovata",
                        subtitle = "Prova a modificare i filtri di ricerca o la tipologia di carburante selezionata."
                    )
                }
            } else {
                items(stations, key = { it.id }) { station ->
                    StationCard(
                        station = station,
                        onClick = { selectedStationForDetail = station }
                    )
                }
            }
        }
    }

    selectedStationForDetail?.let { station ->
        StationDetailBottomSheet(
            station = station,
            onDismiss = { selectedStationForDetail = null }
        )
    }
}

@Composable
fun StationCard(
    station: Station,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Brand & Highway Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = station.brand.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (station.isHighway) {
                        Surface(
                            color = AmberWarning.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.AltRoute, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = station.highwayName ?: "Autostrada",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberWarning
                                )
                            }
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(16.dp))
                    Text(
                        text = "${station.rating}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Station Name & Address
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = station.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${station.address}, ${station.city} (${station.province})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate500
                )
                if (station.highwayDirection != null) {
                    Text(
                        text = station.highwayDirection,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            // Prices overview
            if (station.fuelPrices.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    station.fuelPrices.filter { it.isSelf }.take(3).forEach { item ->
                        Surface(
                            color = Slate100,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = item.fuel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Slate600
                                )
                                Text(
                                    text = "${String.format("%.3f", item.price)} €",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                            }
                        }
                    }
                }
            }

            // EV charging plugs (if EV or Both)
            if (station.evPlugs.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                    val fastPlug = station.evPlugs.maxByOrNull { it.powerKw }
                    Text(
                        text = "Colonnina Ultra-Fast fino a ${fastPlug?.powerKw} kW • ${fastPlug?.pricePerKwh} €/kWh",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldSuccess,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Amenities
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (station.isOpen24h) {
                        Text("24h", style = MaterialTheme.typography.labelSmall, color = Slate500, fontWeight = FontWeight.Bold)
                    }
                    if (station.hasBar) {
                        Text("• Bar/Café", style = MaterialTheme.typography.labelSmall, color = Slate500)
                    }
                    if (station.hasCarWash) {
                        Text("• Autolavaggio", style = MaterialTheme.typography.labelSmall, color = Slate500)
                    }
                }

                Text(
                    text = "Dettagli & Andamento >",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationDetailBottomSheet(
    station: Station,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = station.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${station.address}, ${station.city} (${station.province})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate500
                    )
                    if (station.highwayName != null) {
                        Text(
                            text = "${station.highwayName} • ${station.highwayDirection}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = station.brand,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            HorizontalDivider()

            // Complete Fuel Price List
            if (station.fuelPrices.isNotEmpty()) {
                Text(
                    text = "Listino Prezzi Carburanti",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    station.fuelPrices.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = item.fuel,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Surface(
                                    color = if (item.isSelf) EmeraldSuccess.copy(alpha = 0.15f) else AmberWarning.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (item.isSelf) "Self" else "Servito",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (item.isSelf) EmeraldSuccess else AmberWarning,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${String.format("%.3f", item.price)} €/L",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // EV Plugs
            if (station.evPlugs.isNotEmpty()) {
                Text(
                    text = "Connettori Ricarica Elettrica",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    station.evPlugs.forEach { plug ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = plug.type,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${plug.powerKw} kW • Disponibili ${plug.availableCount}/${plug.totalCount}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Slate500
                                )
                            }
                            Text(
                                text = "${plug.pricePerKwh} €/kWh",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                        }
                    }
                }
            }

            // Price History Trend (30 Days)
            if (station.priceHistory.isNotEmpty()) {
                Text(
                    text = "Andamento Storico Prezzi (Ultimi 30 Giorni)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        station.priceHistory.forEach { pt ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = pt.date, style = MaterialTheme.typography.labelSmall, color = Slate500)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${String.format("%.3f", pt.price)} €",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
