package it.my360garage.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.my360garage.app.data.model.Vehicle
import it.my360garage.app.data.model.VehicleDocument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDocumentDialog(
    vehicle: Vehicle,
    onDismiss: () -> Unit,
    onConfirm: (VehicleDocument) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("assicurazione") }
    var expiryDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val docTypes = listOf(
        "assicurazione" to "Assicurazione (RCA / Kasko)",
        "bollo" to "Bollo Auto / Tassa di Possesso",
        "revisione" to "Revisione Ministeriale Biennale",
        "libretto" to "Carta di Circolazione (DUC)",
        "tagliando" to "Fattura Tagliando",
        "altro" to "Altro Documento"
    )
    var typeExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Aggiungi Documento / Scadenza",
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
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nome Documento (es. Polizza RCA Allianz)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded }
                ) {
                    OutlinedTextField(
                        value = docTypes.firstOrNull { it.first == type }?.second ?: "Documento",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo di Documento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        docTypes.forEach { (key, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    type = key
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it },
                    label = { Text("Data di Scadenza (AAAA-MM-GG)") },
                    placeholder = { Text("es. 2026-10-31") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Note / Agenzia / Numero Polizza") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val newDoc = VehicleDocument(
                            title = title.trim(),
                            type = type,
                            fileName = "${title.trim().replace(" ", "_")}.pdf",
                            expiryDate = expiryDate.trim().ifBlank { null },
                            notes = notes.trim()
                        )
                        onConfirm(newDoc)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Salva Documento")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    )
}
