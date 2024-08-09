package com.firstapplication.file
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.firstapplication.file.ViewModel.SportsEquipmentViewModel
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

@Composable
fun ReadAndInsertDataFromCSV(
    context: Context,
    viewModel: SportsEquipmentViewModel
) {
    val fileName = "CricketDataBase.csv"

    // Obtain InputStream from the assets folder
    val csvFileInputStream: InputStream = context.assets.open(fileName)

    // Use OpenCSV to create a CSVReader
    val csvReader =
        CSVReaderBuilder(BufferedReader(InputStreamReader(csvFileInputStream))).withSkipLines(1).build()

    // Read CSV records
    csvReader.use { reader ->
        reader.forEachIndexed { index, csvRecord ->
            try {
                processCsvRecord(index, csvRecord, viewModel)
            } catch (e: Exception) {
                Log.e("CSV Parsing", "Error processing row $index: $csvRecord", e)
            }
        }
    }

    csvFileInputStream.close()
}

private fun processCsvRecord(
    index: Int,
    csvRecord: Array<String>,
    viewModel: SportsEquipmentViewModel
) {
    if (csvRecord.size >= 8) {
        val equipment = csvRecord.getOrNull(0) ?: ""
        val typeOfEquipment = csvRecord.getOrNull(1) ?: ""
        val categories = csvRecord.getOrNull(2) ?: ""
        val size = csvRecord.getOrNull(3) ?: ""
        val minPlayerAge = csvRecord.getOrNull(4)?.toIntOrNull() ?: 0
        val maxPlayerAge = csvRecord.getOrNull(5)?.toIntOrNull() ?: 0
        val minPlayerHeight = csvRecord.getOrNull(6)?.toDoubleOrNull() ?: 0.0
        val maxPlayerHeight = csvRecord.getOrNull(7)?.toDoubleOrNull() ?: 0.0

        // Create SportsEquipmentEntity
        val sportsEquipmentEntity = SportsEquipmentEntity(
            equipment = equipment,
            typeOfEquipment = typeOfEquipment,
            categories = categories,
            EquipmentSize = size,
            minPlayerAge = minPlayerAge,
            maxPlayerAge = maxPlayerAge,
            minPlayerHeight = minPlayerHeight,
            maxPlayerHeight = maxPlayerHeight
        )

        // Insert into Room Database using ViewModel
        viewModel.insertSports(sportsEquipmentEntity)
    } else {
        Log.e("CSV Parsing", "Row $index does not have enough elements: ${csvRecord.joinToString(", ")}")
    }
}
