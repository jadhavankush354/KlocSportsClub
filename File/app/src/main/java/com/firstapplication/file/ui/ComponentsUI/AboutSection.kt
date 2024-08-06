import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.opencsv.CSVReader
import java.io.StringReader
import androidx.navigation.NavHostController

fun parseCsvData(csvData: String): List<Map<String, String>> {
    val reader = CSVReader(StringReader(csvData))
    val records = reader.readAll()
    val headers = records[0]
    val data = records.drop(1)
    return data.map { row ->
        headers.zip(row).toMap()
    }
}

fun uploadAboutDataToFirebase(data: List<Map<String, String>>) {
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("AboutScreen")

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
fun UploadAboutCsvScreen() {
    val context = LocalContext.current
    var csvData by remember { mutableStateOf(TextFieldValue("")) }
    var status by remember { mutableStateOf("Data will be uploaded when you enter CSV data and click Upload.") }

    // Read CSV data from assets when the composable first loads
    LaunchedEffect(Unit) {
        try {
            csvData = TextFieldValue(readAboutCsvFromAssets(context))
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
                    uploadAboutDataToFirebase(dataList)
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

private fun readAboutCsvFromAssets(context: Context): String {
    return context.assets.open("about.csv").bufferedReader().use { it.readText() }
}


@Composable
fun FetchDataScreenofAboutScreen(controller: NavHostController) {
    var faqs by remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch data from Firebase when the screen is first displayed
    LaunchedEffect(Unit) {
        fetchAboutDataFromFirebase { data ->
            faqs = data
            isLoading = false
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            Text(text = "Loading...", style = MaterialTheme.typography.body2)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(faqs) { faq ->
                    val title = faq["title"] ?: "No title available"
                    val content = faq["content"] ?: "No content available"
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.body2
                        )
                        Text(
                            text = content,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }
}

fun fetchAboutDataFromFirebase(onResult: (List<Map<String, String>>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("AboutScreen")

    collectionRef.get()
        .addOnSuccessListener { result ->
            val data = result.mapNotNull { document ->
                val documentData = document.data
                Log.d("Firebase", "Document AboutData: $documentData") // Debugging log

                // Ensure the data contains necessary fields
                val title = documentData["title"] as? String
                val content = documentData["content"] as? String

                if (title != null && content != null) {
                    mapOf("title" to title, "content" to content)
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
