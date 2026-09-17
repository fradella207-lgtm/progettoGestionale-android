package it.my360garage.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import it.my360garage.app.data.model.Vehicle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleDialog(
    onDismiss: () -> Unit,
    onConfirm: (Vehicle) -> Unit
) {
    var vehicleType by remember { mutableStateOf("car") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var trimLevel by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var fuelType by remember { mutableStateOf("Benzina") }
    var tankCapacity by remember { mutableStateOf("50") }
    var initialKm by remember { mutableStateOf("0") }
    var powerCv by remember { mutableStateOf("120") }

    val fuelOptions = listOf("Benzina", "Diesel", "Full / Mild Hybrid", "Plug-in Hybrid (PHEV)", "Elettrica (BEV)", "GPL", "Metano")
    var fuelExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Aggiungi Nuovo Veicolo",
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
                // Type selector: Auto vs Moto
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = vehicleType == "car",
                        onClick = { vehicleType = "car" },
                        label = { Text("Auto") },
                        leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = vehicleType == "moto",
                        onClick = { vehicleType = "moto" },
                        label = { Text("Moto / Scooter") },
                        leadingIcon = { Icon(Icons.Default.TwoWheeler, contentDescription = null) },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Marca (es. Alfa Romeo, BMW, Ducati)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Modello (es. Giulia, Golf, Monster)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = trimLevel,
                    onValueChange = { trimLevel = it },
                    label = { Text("Allestimento (es. Veloce, S-Line, Style)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it.uppercase() },
                    label = { Text("Targa (es. GA 892 TR)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Fuel Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = fuelExpanded,
                    onExpandedChange = { fuelExpanded = !fuelExpanded }
                ) {
                    OutlinedTextField(
                        value = fuelType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Alimentazione") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fuelExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = fuelExpanded,
                        onDismissRequest = { fuelExpanded = false }
                    ) {
                        fuelOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    fuelType = opt
                                    fuelExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = tankCapacity,
                        onValueChange = { tankCapacity = it },
                        label = { Text("Serbatoio (L/kWh)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = powerCv,
                        onValueChange = { powerCv = it },
                        label = { Text("Potenza (CV)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = initialKm,
                    onValueChange = { initialKm = it },
                    label = { Text("Chilometraggio Iniziale (km)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (brand.isNotBlank() && model.isNotBlank() && plate.isNotBlank()) {
                        val parsedTank = tankCapacity.toDoubleOrNull() ?: 50.0
                        val parsedKm = initialKm.toDoubleOrNull() ?: 0.0
                        val parsedCv = powerCv.toIntOrNull() ?: 120
                        val parsedKw = (parsedCv * 0.735499).toInt()

                        val newVehicle = Vehicle(
                            vehicleType = vehicleType,
                            brand = brand.trim(),
                            model = model.trim(),
                            trimLevel = trimLevel.trim(),
                            plate = plate.trim(),
                            fuelType = fuelType,
                            tankCapacity = parsedTank,
                            powerCv = parsedCv,
                            powerKw = parsedKw,
                            initialKm = parsedKm,
                            photoUrl = if (vehicleType == "moto")
                                "https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=800&auto=format&fit=crop&q=80"
                            else
                                "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=800&auto=format&fit=crop&q=80"
                        )
                        onConfirm(newVehicle)
                    }
                },
                enabled = brand.isNotBlank() && model.isNotBlank() && plate.isNotBlank()
            ) {
                Text("Salva Veicolo")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}
