package it.my360garage.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import it.my360garage.app.data.model.MaintenanceRecord
import it.my360garage.app.data.model.Vehicle
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMaintenanceDialog(
    vehicle: Vehicle,
    onDismiss: () -> Unit,
    onConfirm: (MaintenanceRecord) -> Unit
) {
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var km by remember { mutableStateOf(vehicle.currentKm.toInt().toString()) }
    var category by remember { mutableStateOf("Tagliando Ordinario") }
    var description by remember { mutableStateOf("") }
    var workshop by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val categories = listOf(
        "Tagliando Ordinario",
        "Pasticche & Freni",
        "Pneumatici",
        "Batteria",
        "Revisione Ministeriale",
        "Cinghia / Catena Distribuzione",
        "Ammortizzatori & Sospensioni",
        "Climatizzatore / Ricarica Gas",
        "Riparazione Meccanica",
        "Altro"
    )
    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Registra Manutenzione",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${vehicle.brand} ${vehicle.model} (${vehicle.plate})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Data") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = km,
                        onValueChange = { km = it },
                        label = { Text("Chilometri (km)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Category selector
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoria Intervento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrizione Lavori (es. Olio, filtri)") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = workshop,
                    onValueChange = { workshop = it },
                    label = { Text("Officina / Meccanico (es. Concessionaria Ufficiale)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cost,
                    onValueChange = { cost = it },
                    label = { Text("Costo Totale (€)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Note aggiuntive / Garanzia") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedKm = km.toDoubleOrNull() ?: vehicle.currentKm
                    val parsedCost = cost.replace(',', '.').toDoubleOrNull() ?: 0.0

                    if (description.isNotBlank()) {
                        val newRecord = MaintenanceRecord(
                            date = date.trim(),
                            km = parsedKm,
                            category = category,
                            description = description.trim(),
                            workshop = workshop.trim(),
                            cost = parsedCost,
                            notes = notes.trim()
                        )
                        onConfirm(newRecord)
                    }
                },
                enabled = description.isNotBlank()
            ) {
                Text("Salva Intervento")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}
