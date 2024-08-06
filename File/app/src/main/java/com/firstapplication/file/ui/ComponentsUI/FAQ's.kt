package com.firstapplication.file.ui.ComponentsUI


import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.firstapplication.file.userAuthentication.util.readCsvFromAssets
import com.opencsv.CSVReader
import java.io.StringReader


fun parseCsvData(csvData: String): List<Map<String, String>> {
    val reader = CSVReader(StringReader(csvData))
    val records = reader.readAll()
    val headers = records[0]
    val data = records.drop(1)
    return data.map { row ->
        headers.zip(row).toMap()
    }
}

fun uploadDataToFirebase(data: List<Map<String, String>>) {
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("FAQs")

    data.forEach { item ->
        collectionRef.add(item)
            .addOnSuccessListener { documentReference ->
                Log.d("Firebase", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error adding document", e)
            }
    }
}

@Composable
fun UploadCsvScreen(controller: NavHostController) {
    val context = LocalContext.current
    var csvData by remember { mutableStateOf(TextFieldValue("")) }
    var status by remember { mutableStateOf("Data will be uploaded when you enter CSV data and click Upload.") }

    // Read CSV data from assets when the composable first loads
    LaunchedEffect(Unit) {
        try {
            csvData = TextFieldValue(readCsvFromAssets(context))
        } catch (e: Exception) {
            Log.e("UploadCsvScreen", "Error reading CSV file", e)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = csvData,
            onValueChange = { csvData = it },
            label = { Text("CSV Data") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                try {
                    val dataList = parseCsvData(csvData.text)
                    uploadDataToFirebase(dataList)
                    status = "Data uploaded successfully."
                } catch (e: Exception) {
                    Log.e("UploadCsvScreen", "Error uploading data", e)
                    status = "Error uploading data."
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Upload")
        }

        if (status.isNotEmpty()) {
            Text(text = status, color = Color.Green, modifier = Modifier.padding(top = 16.dp))
        }
    }
}


private fun readCsvFromAssets(context: Context): String {
    return context.assets.open("frequentlyaskedquestions.csv").bufferedReader().use { it.readText() }

}


@Composable
fun FetchDataScreen(controller: NavHostController) {
    var faqs by remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch data from Firebase when the screen is first displayed
    LaunchedEffect(Unit) {
        fetchDataFromFirebase { data ->
            faqs = data
            isLoading = false
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            Text(text = "Loading...", style = MaterialTheme.typography.body1)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(faqs) { faq ->
                    val question = faq["Q"] ?: "No question available"
                    val answer = faq["A"] ?: "No answer available"
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = "Q: $question",
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.body1
                        )
                        // Highlighted answer
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colors.secondary.copy(alpha = 0.1f)) // Light background color
                                .padding(8.dp) // Padding around the answer
                        ) {
                            Text(
                                text = "A: $answer",
                                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold) // Bold text
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun fetchDataFromFirebase(onResult: (List<Map<String, String>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("FAQs")

    collectionRef.get()
        .addOnSuccessListener { result ->
            val data = result.mapNotNull { document ->
                val documentData = document.data
                Log.d("Firebase", "Document data: $documentData") // Debugging log

                // Ensure the data contains "Q" and "A" fields
                val question = documentData["Q"] as? String
                val answer = documentData["A"] as? String

                if (question != null && answer != null) {
                    mapOf("Q" to question, "A" to answer)
                } else {
                    Log.d("Firebase", "Skipping document with missing fields: $documentData")
                    null
                }
            }
            onResult(data)
        }
        .addOnFailureListener { e ->
            Log.w("Firebase", "Error getting documents.", e)
            onResult(emptyList())
        }
}


