package it.my360garage.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import it.my360garage.app.data.model.RefuelRecord
import it.my360garage.app.data.model.Vehicle
import java.time.LocalDate

@Composable
fun AddRefuelDialog(
    vehicle: Vehicle,
    onDismiss: () -> Unit,
    onConfirm: (RefuelRecord) -> Unit
) {
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var km by remember { mutableStateOf(vehicle.currentKm.toInt().toString()) }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var isFullTank by remember { mutableStateOf(true) }
    var notes by remember { mutableStateOf("") }

    val unit = if (vehicle.fuelType.contains("Elettrica", ignoreCase = true)) "kWh" else "L"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Nuovo Rifornimento / Ricarica",
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

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Data (AAAA-MM-GG)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = km,
                    onValueChange = { km = it },
                    label = { Text("Chilometraggio Odierno (km)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantità ($unit)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Spesa Totale (€)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Calculated price per liter preview
                val parsedQty = quantity.replace(',', '.').toDoubleOrNull() ?: 0.0
                val parsedPrice = price.replace(',', '.').toDoubleOrNull() ?: 0.0
                if (parsedQty > 0 && parsedPrice > 0) {
                    val unitPrice = parsedPrice / parsedQty
                    Text(
                        text = "Prezzo unitario: ${String.format("%.3f", unitPrice)} €/$unit",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isFullTank,
                        onCheckedChange = { isFullTank = it }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Pieno completo (consigliato per calcolo consumi)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Stazione / Note (es. Eni Pioltello)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedKm = km.toDoubleOrNull() ?: vehicle.currentKm
                    val pQty = quantity.replace(',', '.').toDoubleOrNull() ?: 0.0
                    val pPrice = price.replace(',', '.').toDoubleOrNull() ?: 0.0

                    if (pQty > 0 && pPrice > 0) {
                        val newRefuel = RefuelRecord(
                            date = date.trim(),
                            km = parsedKm,
                            quantity = pQty,
                            price = pPrice,
                            type = if (isFullTank) "full" else "partial",
                            unit = unit,
                            notes = notes.trim()
                        )
                        onConfirm(newRefuel)
                    }
                },
                enabled = quantity.isNotBlank() && price.isNotBlank()
            ) {
                Text("Registra Rifornimento")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}
