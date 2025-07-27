// SecondScreen.kt
package com.volkdev.moodlink

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volkdev.moodlink.ui.theme.MoodLinkAppTheme
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun SecondScreen() {
    var selectedEmotion by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fundo com imagem
        Image(
            painter = painterResource(id = R.drawable.primeira_tela), // salve como 'mood_background.png'
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Como você está se sentindo hoje?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EmotionButton("😊", "Feliz", selectedEmotion) { selectedEmotion = it }
                EmotionButton("😢", "Triste", selectedEmotion) { selectedEmotion = it }
                EmotionButton("😴", "Cansado", selectedEmotion) { selectedEmotion = it }
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (selectedEmotion != null) {
                Button(
                    onClick = {
                        saveEmotionToFirestore(selectedEmotion!!) // aqui chamamos a função
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xAA2196F3)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Continuar", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}
fun saveEmotionToFirestore(emotion: String) {
    val db = Firebase.firestore
    val emotionData = hashMapOf(
        "emotion" to emotion,
        "timestamp" to System.currentTimeMillis()
    )

    db.collection("emotions")
        .add(emotionData)
        .addOnSuccessListener {
            Log.d("Firestore", "Emoção '$emotion' salva com sucesso!")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Erro ao salvar emoção", e)
        }
}


@Composable
fun EmotionButton(emoji: String, label: String, selected: String?, onSelect: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onSelect(label) }
            .background(
                color = if (selected == label) Color(0x882196F3) else Color(0x55000000),
                shape = CircleShape
            )
            .padding(20.dp)
    ) {
        Text(text = emoji, fontSize = 28.sp)
        Text(text = label, fontSize = 14.sp, color = Color.White)
    }
}