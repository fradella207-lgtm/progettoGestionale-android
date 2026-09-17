package it.my360garage.app.data.seed

import it.my360garage.app.data.model.*

object SeedData {

    val initialVehicles: List<Vehicle> = listOf(
        Vehicle(
            id = "car_1",
            vehicleType = "car",
            brand = "Alfa Romeo",
            model = "Giulia 2.2 Turbo",
            trimLevel = "Veloce Q2",
            generation = "Tipo 952 (2021)",
            plate = "GA 892 TR",
            vin = "ZAR95200007891234",
            fuelType = "Diesel",
            tankCapacity = 52.0,
            motorization = "2.2 Turbo Diesel 190 CV AT8 Q2",
            driveType = "Trazione Posteriore (RWD)",
            powerCv = 190,
            powerKw = 140,
            registrationDate = "2021-04-15",
            initialKm = 50000.0,
            photoUrl = "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?w=800&auto=format&fit=crop&q=80",
            technicalSpecs = VehicleTechnicalSpecs(
                engineDisplacementCc = 2143,
                powerCv = 190,
                powerKw = 140,
                torqueNm = 450,
                transmission = "Automatico AT8 ZF a 8 rapporti",
                drivetrain = "Posteriore con albero in fibra di carbonio",
                euroClass = "Euro 6D-Temp",
                fuelCapacityLiters = 52.0,
                wltpConsumption = "5.2 L/100 km (19.2 km/L)",
                wltpRangeKm = 1000,
                recommendedOil = "0W-20 Selenia WR Forward (Fiat 9.55535-DSX)",
                oilCapacityLiters = 4.3,
                tirePressureFrontBar = 2.3,
                tirePressureRearBar = 2.5,
                allowedTireSizes = listOf("225/50 R17 94W", "225/45 R18 (Ant) - 255/40 R18 (Post)"),
                summaryQuattroruote = "Berlina sportiva con telaio Giorgio, distribuzione pesi 50:50 perfetta e sterzo ultra-diretto."
            ),
            documents = listOf(
                VehicleDocument(
                    id = "doc_1_1",
                    title = "Libretto di Circolazione (DUC)",
                    type = "libretto",
                    fileName = "Libretto_Giulia_GA892TR.pdf",
                    notes = "Carta di circolazione unificata valida"
                ),
                VehicleDocument(
                    id = "doc_1_2",
                    title = "Polizza RCA & Furto/Incendio",
                    type = "assicurazione",
                    fileName = "Polizza_UnipolSai_2026.pdf",
                    expiryDate = "2026-10-30",
                    notes = "RCA Massimale 10M€ con soccorso stradale Top"
                ),
                VehicleDocument(
                    id = "doc_1_3",
                    title = "Bollo Auto Regionale",
                    type = "bollo",
                    fileName = "Ricevuta_Bollo_Lombardia.pdf",
                    expiryDate = "2026-09-30",
                    notes = "Tassa automobilistica Regione Lombardia (140 kW)"
                ),
                VehicleDocument(
                    id = "doc_1_4",
                    title = "Revisione Ministeriale Biennale",
                    type = "revisione",
                    fileName = "Certificato_Revisione.pdf",
                    expiryDate = "2027-04-15",
                    notes = "Prima revisione quadriennale superata con successo"
                )
            ),
            refuels = listOf(
                RefuelRecord(
                    id = "ref_1_1",
                    date = "2026-08-10",
                    km = 58200.0,
                    quantity = 45.5,
                    price = 78.50,
                    type = "full",
                    energyType = "fuel",
                    unit = "L",
                    notes = "Eni Diesel Pioltello"
                ),
                RefuelRecord(
                    id = "ref_1_2",
                    date = "2026-08-25",
                    km = 58980.0,
                    quantity = 42.0,
                    price = 72.80,
                    type = "full",
                    energyType = "fuel",
                    unit = "L",
                    notes = "Q8 San Zenone A1"
                ),
                RefuelRecord(
                    id = "ref_1_3",
                    date = "2026-09-08",
                    km = 59750.0,
                    quantity = 43.8,
                    price = 75.30,
                    type = "full",
                    energyType = "fuel",
                    unit = "L",
                    notes = "IP Gruppo API Milano Est"
                )
            ),
            maintenances = listOf(
                MaintenanceRecord(
                    id = "maint_1_1",
                    date = "2026-05-18",
                    km = 55000.0,
                    category = "Tagliando Ordinario",
                    description = "Sostituzione olio motore 0W-20 Selenia, filtro olio, filtro aria motore, filtro antipolline abitacolo e controllo livelli.",
                    workshop = "Officina Autorizzata Alfa Romeo Milano",
                    cost = 380.0,
                    notes = "Tutto regolare, usura freni al 30%"
                ),
                MaintenanceRecord(
                    id = "maint_1_2",
                    date = "2026-07-02",
                    km = 56800.0,
                    category = "Pneumatici",
                    description = "Inversione e bilanciatura pneumatici estivi Michelin Pilot Sport 4.",
                    workshop = "Gommista Express Segrate",
                    cost = 50.0,
                    notes = "Battistrada residuo 5.2 mm ant, 5.0 mm post"
                )
            )
        ),
        Vehicle(
            id = "car_2",
            vehicleType = "car",
            brand = "Volkswagen",
            model = "Golf 1.4 eHybrid",
            trimLevel = "Style DSG",
            generation = "Golf VIII (2022)",
            plate = "FY 412 KL",
            vin = "WVWZZZCDZNW048921",
            fuelType = "Plug-in Hybrid (PHEV)",
            tankCapacity = 40.0,
            batteryCapacity = 13.0,
            motorization = "1.4 TSI + Motore Elettrico 204 CV DSG",
            driveType = "Trazione Anteriore (FWD)",
            powerCv = 204,
            powerKw = 150,
            registrationDate = "2022-09-10",
            initialKm = 24000.0,
            photoUrl = "https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?w=800&auto=format&fit=crop&q=80",
            technicalSpecs = VehicleTechnicalSpecs(
                engineDisplacementCc = 1395,
                powerCv = 204,
                powerKw = 150,
                torqueNm = 350,
                transmission = "Automatico DSG a 6 rapporti a doppia frizione con motore elettrico integrato",
                drivetrain = "Trazione Anteriore",
                euroClass = "Euro 6D",
                fuelCapacityLiters = 40.0,
                wltpConsumption = "1.2 L/100 km + 14.8 kWh/100 km",
                wltpRangeKm = 72,
                recommendedOil = "0W-20 VW 508.00 / 509.00 LongLife IV",
                oilCapacityLiters = 4.0,
                tirePressureFrontBar = 2.4,
                tirePressureRearBar = 2.4,
                allowedTireSizes = listOf("205/55 R16 91V", "225/45 R17 91W", "225/40 R18 92Y"),
                summaryQuattroruote = "Compatta ibrida ricaricabile con oltre 60 km di autonomia 100% elettrica in città."
            ),
            documents = listOf(
                VehicleDocument(
                    id = "doc_2_1",
                    title = "Libretto di Circolazione (DUC)",
                    type = "libretto",
                    fileName = "Libretto_Golf_FY412KL.pdf",
                    notes = "Veicolo ibrido omologato classe Euro 6D"
                ),
                VehicleDocument(
                    id = "doc_2_2",
                    title = "Assicurazione All-Inclusive",
                    type = "assicurazione",
                    fileName = "Allianz_Direct_Golf.pdf",
                    expiryDate = "2026-11-20",
                    notes = "Copertura Kasko completa con franchigia 250€"
                ),
                VehicleDocument(
                    id = "doc_2_3",
                    title = "Esenzione / Bollo Ibrida",
                    type = "bollo",
                    fileName = "Ricevuta_Bollo_Lombardia_Ibrida.pdf",
                    expiryDate = "2026-12-31",
                    notes = "Riduzione 50% bollo per veicoli PHEV"
                )
            ),
            refuels = listOf(
                RefuelRecord(
                    id = "ref_2_1",
                    date = "2026-08-15",
                    km = 29500.0,
                    quantity = 35.0,
                    price = 62.00,
                    type = "full",
                    energyType = "fuel",
                    unit = "L",
                    notes = "Benzina 95 ottani Tamoil"
                ),
                RefuelRecord(
                    id = "ref_2_2",
                    date = "2026-08-28",
                    km = 29900.0,
                    quantity = 11.5,
                    price = 5.60,
                    type = "full",
                    energyType = "electricity",
                    unit = "kWh",
                    notes = "Ricarica Colonnina A2A Emoving Milano"
                ),
                RefuelRecord(
                    id = "ref_2_3",
                    date = "2026-09-10",
                    km = 30420.0,
                    quantity = 33.2,
                    price = 59.80,
                    type = "full",
                    energyType = "fuel",
                    unit = "L",
                    notes = "Benzina Esso Centro"
                )
            ),
            maintenances = listOf(
                MaintenanceRecord(
                    id = "maint_2_1",
                    date = "2026-04-10",
                    km = 28000.0,
                    category = "Tagliando Ordinario",
                    description = "Controllo alta tensione batteria PHEV, cambio olio 0W-20 LongLife e filtri.",
                    workshop = "Concessionaria Volkswagen Busto Arsizio",
                    cost = 320.0,
                    notes = "Stato batteria trazione 98% SoH"
                )
            )
        ),
        Vehicle(
            id = "moto_1",
            vehicleType = "moto",
            brand = "Ducati",
            model = "Monster 937",
            trimLevel = "Plus",
            generation = "Monster 937 (2023)",
            plate = "AA 34567",
            vin = "ZDM1B00AAKB019283",
            fuelType = "Benzina",
            tankCapacity = 14.0,
            motorization = "Testastretta 11° bicilindrico 937 cc 111 CV",
            driveType = "Catena con O-Ring",
            powerCv = 111,
            powerKw = 82,
            registrationDate = "2023-05-20",
            initialKm = 4000.0,
            photoUrl = "https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=800&auto=format&fit=crop&q=80",
            technicalSpecs = VehicleTechnicalSpecs(
                engineDisplacementCc = 937,
                powerCv = 111,
                powerKw = 82,
                torqueNm = 93,
                transmission = "Manuale 6 marce con Ducati Quick Shift (DQS) Up/Down",
                drivetrain = "Catena finale 520",
                euroClass = "Euro 5",
                fuelCapacityLiters = 14.0,
                wltpConsumption = "5.2 L/100 km",
                wltpRangeKm = 260,
                recommendedOil = "Shell Advance 4T Ultra 15W-50",
                oilCapacityLiters = 3.2,
                tirePressureFrontBar = 2.3,
                tirePressureRearBar = 2.5,
                allowedTireSizes = listOf("120/70 ZR17 (Ant) - 180/55 ZR17 (Post)"),
                summaryQuattroruote = "Naked sportiva leggera, telaio Front Frame in alluminio di derivazione Panigale V4."
            ),
            documents = listOf(
                VehicleDocument(
                    id = "doc_3_1",
                    title = "Libretto Moto (DUC)",
                    type = "libretto",
                    fileName = "Libretto_Ducati_Monster.pdf",
                    notes = "Omologata biposto Euro 5"
                ),
                VehicleDocument(
                    id = "doc_3_2",
                    title = "Assicurazione Moto Sospendibile",
                    type = "assicurazione",
                    fileName = "Polizza_Prima_Monster.pdf",
                    expiryDate = "2026-10-15",
                    notes = "Copertura estiva con guida esclusiva"
                ),
                VehicleDocument(
                    id = "doc_3_3",
                    title = "Bollo Moto",
                    type = "bollo",
                    fileName = "Bollo_Moto_Lombardia.pdf",
                    expiryDate = "2027-01-31",
                    notes = "Bollo annuale moto 82 kW"
                )
            ),
            refuels = listOf(
                RefuelRecord(
                    id = "ref_3_1",
                    date = "2026-08-20",
                    km = 7200.0,
                    quantity = 11.2,
                    price = 20.50,
                    type = "full",
                    energyType = "fuel",
                    unit = "L",
                    notes = "Q8 Easy Milano Nord"
                ),
                RefuelRecord(
                    id = "ref_3_2",
                    date = "2026-09-05",
                    km = 7420.0,
                    quantity = 10.8,
                    price = 19.80,
                    type = "full",
                    energyType = "fuel",
                    unit = "L",
                    notes = "Eni Station Lago di Como"
                )
            ),
            maintenances = listOf(
                MaintenanceRecord(
                    id = "maint_3_1",
                    date = "2026-05-10",
                    km = 6000.0,
                    category = "Tagliando Desmo Service",
                    description = "Sostituzione olio motore Shell 15W-50, filtro olio, tensione e ingrassaggio catena, aggiornamento software centralina.",
                    workshop = "Ducati Milano Nord Service",
                    cost = 240.0,
                    notes = "Pastiglie e pneumatici Pirelli Diablo Rosso III in ottimo stato"
                )
            )
        )
    )

    val initialStations: List<Station> = listOf(
        Station(
            id = "st_1",
            name = "Eni Live Station & Café - San Zenone Ovest",
            brand = "Eni",
            type = "both",
            address = "Autostrada A1 Milano-Napoli km 15+100",
            city = "San Zenone al Lambro",
            province = "MI",
            lat = 45.3120,
            lng = 9.3510,
            isHighway = true,
            highwayName = "A1 Milano - Napoli",
            highwayDirection = "Dir. Sud (Bologna / Roma)",
            isOpen24h = true,
            hasCarWash = true,
            hasBar = true,
            hasShop = true,
            rating = 4.6,
            fuelPrices = listOf(
                FuelPriceItem(fuel = "Benzina", price = 1.789, isSelf = true, updatedAt = "Oggi 08:30"),
                FuelPriceItem(fuel = "Diesel", price = 1.699, isSelf = true, updatedAt = "Oggi 08:30"),
                FuelPriceItem(fuel = "GPL", price = 0.719, isSelf = false, updatedAt = "Oggi 08:30"),
                FuelPriceItem(fuel = "Benzina", price = 2.059, isSelf = false, updatedAt = "Oggi 08:30"),
                FuelPriceItem(fuel = "Diesel", price = 1.969, isSelf = false, updatedAt = "Oggi 08:30")
            ),
            evPlugs = listOf(
                EVPlugItem(type = "CCS Combo 2 (DC)", powerKw = 300, pricePerKwh = 0.65, availableCount = 4, totalCount = 4, status = "available"),
                EVPlugItem(type = "Type 2 (AC)", powerKw = 22, pricePerKwh = 0.49, availableCount = 2, totalCount = 2, status = "available")
            ),
            priceHistory = listOf(
                PriceHistoryPoint(date = "18 Ago", price = 1.839, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "25 Ago", price = 1.819, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "01 Set", price = 1.799, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "08 Set", price = 1.789, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "15 Set", price = 1.789, fuel = "Benzina", isSelf = true)
            )
        ),
        Station(
            id = "st_2",
            name = "Q8 Easy 24/7 - Milano Forlanini",
            brand = "Q8",
            type = "fuel",
            address = "Viale Enrico Forlanini 112",
            city = "Milano",
            province = "MI",
            lat = 45.4628,
            lng = 9.2561,
            isHighway = false,
            isOpen24h = true,
            hasCarWash = true,
            hasBar = false,
            hasShop = false,
            rating = 4.4,
            fuelPrices = listOf(
                FuelPriceItem(fuel = "Benzina", price = 1.749, isSelf = true, updatedAt = "Oggi 07:15"),
                FuelPriceItem(fuel = "Diesel", price = 1.649, isSelf = true, updatedAt = "Oggi 07:15"),
                FuelPriceItem(fuel = "GPL", price = 0.699, isSelf = false, updatedAt = "Oggi 07:15")
            ),
            priceHistory = listOf(
                PriceHistoryPoint(date = "18 Ago", price = 1.799, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "25 Ago", price = 1.779, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "01 Set", price = 1.759, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "08 Set", price = 1.749, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "15 Set", price = 1.749, fuel = "Benzina", isSelf = true)
            )
        ),
        Station(
            id = "st_3",
            name = "IP Gruppo API - Brianza Est (A4)",
            brand = "IP",
            type = "both",
            address = "Autostrada A4 Torino-Trieste km 148",
            city = "Agrate Brianza",
            province = "MB",
            lat = 45.5780,
            lng = 9.3620,
            isHighway = true,
            highwayName = "A4 Torino - Trieste",
            highwayDirection = "Dir. Est (Bergamo / Venezia)",
            isOpen24h = true,
            hasCarWash = true,
            hasBar = true,
            hasShop = true,
            rating = 4.5,
            fuelPrices = listOf(
                FuelPriceItem(fuel = "Benzina", price = 1.779, isSelf = true, updatedAt = "Oggi 09:00"),
                FuelPriceItem(fuel = "Diesel", price = 1.679, isSelf = true, updatedAt = "Oggi 09:00"),
                FuelPriceItem(fuel = "Metano", price = 1.289, isSelf = false, updatedAt = "Oggi 09:00"),
                FuelPriceItem(fuel = "GPL", price = 0.709, isSelf = false, updatedAt = "Oggi 09:00")
            ),
            evPlugs = listOf(
                EVPlugItem(type = "CCS Combo 2 (DC)", powerKw = 150, pricePerKwh = 0.69, availableCount = 2, totalCount = 2, status = "available")
            ),
            priceHistory = listOf(
                PriceHistoryPoint(date = "18 Ago", price = 1.829, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "25 Ago", price = 1.809, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "01 Set", price = 1.789, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "08 Set", price = 1.779, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "15 Set", price = 1.779, fuel = "Benzina", isSelf = true)
            )
        ),
        Station(
            id = "st_4",
            name = "Ionity High Power Hub - Melegnano",
            brand = "Ionity",
            type = "ev",
            address = "Via Privata Borgo Est 3",
            city = "Melegnano",
            province = "MI",
            lat = 45.3580,
            lng = 9.3240,
            isHighway = false,
            isOpen24h = true,
            hasCarWash = false,
            hasBar = true,
            hasShop = false,
            rating = 4.8,
            evPlugs = listOf(
                EVPlugItem(type = "CCS Combo 2 (DC)", powerKw = 350, pricePerKwh = 0.69, availableCount = 6, totalCount = 6, status = "available"),
                EVPlugItem(type = "CCS Combo 2 (DC)", powerKw = 50, pricePerKwh = 0.55, availableCount = 2, totalCount = 2, status = "available")
            ),
            priceHistory = listOf(
                PriceHistoryPoint(date = "18 Ago", price = 0.69, fuel = "Elettricità", isSelf = true),
                PriceHistoryPoint(date = "15 Set", price = 0.69, fuel = "Elettricità", isSelf = true)
            )
        ),
        Station(
            id = "st_5",
            name = "Tesla Supercharger & Bar - Arese Shopping Center",
            brand = "Tesla",
            type = "ev",
            address = "Via Giuseppe Eugenio Luraghi 11",
            city = "Arese",
            province = "MI",
            lat = 45.5560,
            lng = 9.0760,
            isHighway = false,
            isOpen24h = true,
            hasCarWash = false,
            hasBar = true,
            hasShop = true,
            rating = 4.9,
            evPlugs = listOf(
                EVPlugItem(type = "Tesla Supercharger V3 (DC)", powerKw = 250, pricePerKwh = 0.46, availableCount = 12, totalCount = 16, status = "available")
            ),
            priceHistory = listOf(
                PriceHistoryPoint(date = "18 Ago", price = 0.49, fuel = "Elettricità", isSelf = true),
                PriceHistoryPoint(date = "15 Set", price = 0.46, fuel = "Elettricità", isSelf = true)
            )
        ),
        Station(
            id = "st_6",
            name = "Tamoil Express - Roma Eur",
            brand = "Tamoil",
            type = "fuel",
            address = "Viale dell'Astronomia 25",
            city = "Roma",
            province = "RM",
            lat = 41.8310,
            lng = 12.4670,
            isHighway = false,
            isOpen24h = true,
            hasCarWash = true,
            hasBar = true,
            hasShop = false,
            rating = 4.3,
            fuelPrices = listOf(
                FuelPriceItem(fuel = "Benzina", price = 1.739, isSelf = true, updatedAt = "Oggi 08:00"),
                FuelPriceItem(fuel = "Diesel", price = 1.639, isSelf = true, updatedAt = "Oggi 08:00"),
                FuelPriceItem(fuel = "GPL", price = 0.689, isSelf = false, updatedAt = "Oggi 08:00")
            ),
            priceHistory = listOf(
                PriceHistoryPoint(date = "18 Ago", price = 1.789, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "25 Ago", price = 1.769, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "01 Set", price = 1.749, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "15 Set", price = 1.739, fuel = "Benzina", isSelf = true)
            )
        ),
        Station(
            id = "st_7",
            name = "Esso - Firenze Nord A1",
            brand = "Esso",
            type = "both",
            address = "Autostrada A1 km 279 Nord",
            city = "Campi Bisenzio",
            province = "FI",
            lat = 43.8340,
            lng = 11.1620,
            isHighway = true,
            highwayName = "A1 Milano - Napoli",
            highwayDirection = "Dir. Nord (Bologna / Milano)",
            isOpen24h = true,
            hasCarWash = true,
            hasBar = true,
            hasShop = true,
            rating = 4.4,
            fuelPrices = listOf(
                FuelPriceItem(fuel = "Benzina", price = 1.799, isSelf = true, updatedAt = "Oggi 07:45"),
                FuelPriceItem(fuel = "Diesel", price = 1.699, isSelf = true, updatedAt = "Oggi 07:45"),
                FuelPriceItem(fuel = "GPL", price = 0.729, isSelf = false, updatedAt = "Oggi 07:45")
            ),
            evPlugs = listOf(
                EVPlugItem(type = "CCS Combo 2 (DC)", powerKw = 150, pricePerKwh = 0.66, availableCount = 3, totalCount = 4, status = "available")
            ),
            priceHistory = listOf(
                PriceHistoryPoint(date = "18 Ago", price = 1.849, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "25 Ago", price = 1.829, fuel = "Benzina", isSelf = true),
                PriceHistoryPoint(date = "15 Set", price = 1.799, fuel = "Benzina", isSelf = true)
            )
        )
    )
}
