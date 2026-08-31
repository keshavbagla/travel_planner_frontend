package com.example.travel_planner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.data.AuthRepository
import com.example.travel_planner.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun EmailEntryScreen(
    onExistingUser: (email: String) -> Unit,
    onNewUser: (email: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Voyago AI", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = Navy)
        Text(
            "Enter your email to sign in or create an account.",
            style = MaterialTheme.typography.bodyMedium,
            color = Slate,
            modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("jane@example.com") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Border,
                focusedBorderColor = Teal
            )
        )

        errorMessage?.let {
            Text(it, color = Coral, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Coral)
                .clickable(enabled = !isLoading) {
                    if (email.isBlank()) {
                        errorMessage = "Enter a valid email"
                        return@clickable
                    }
                    isLoading = true
                    scope.launch {
                        val result = AuthRepository.emailExists(email)
                        isLoading = false
                        result.onSuccess { exists ->
                            if (exists) onExistingUser(email) else onNewUser(email)
                        }.onFailure {
                            errorMessage = "Couldn't reach server. Try again."
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = White, modifier = Modifier.height(20.dp))
            } else {
                Text("Continue", color =White, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EmailEntryScreenPreview() {
    EmailEntryScreen(
        onExistingUser = {},
        onNewUser = {}
    )
}