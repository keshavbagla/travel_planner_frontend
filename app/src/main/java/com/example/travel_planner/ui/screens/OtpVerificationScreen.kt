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
fun OtpVerificationScreen(
    email: String,
    onVerified: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().background(Background).padding(14.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Verify your email", fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, color = Navy)
        Text(
            "Enter the 6-digit code we sent to $email",
            style = MaterialTheme.typography.bodyMedium,
            color = Slate,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 32.dp)
        )

        OutlinedTextField(
            value = code,
            onValueChange = { if (it.length <= 6) code = it; errorMessage = null },
            modifier = Modifier.fillMaxWidth() .height(60.dp),
            placeholder = { Text("123456") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(34.dp),
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
                .height(55.dp)
                .padding(top = 18.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Coral)
                .clickable(enabled = !isLoading) {
                    if (code.length != 6) {
                        errorMessage = "Enter the 6-digit code"
                        return@clickable
                    }
                    isLoading = true
                    scope.launch {
                        val result = AuthRepository.verifyOtp(email, code)
                        isLoading = false
                        result.onSuccess { onVerified() }
                            .onFailure { errorMessage = "Invalid or expired code" }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = White, modifier = Modifier.height(30.dp) )
            } else {
                Text("verify", color = White, style = MaterialTheme.typography.labelLarge , fontSize = 18.sp)
            }
        }

        Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), contentAlignment = Alignment.Center) {
            Text(
                "Resend code",
                style = MaterialTheme.typography.labelMedium,
                color = Teal,
                fontSize = 12.sp,
                modifier = Modifier.clickable {
                    scope.launch { AuthRepository.sendOtp(email) }
                }
            )
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun OtpVerificationScreenPreview() {
    OtpVerificationScreen(
        email = "name@example.com",
        onVerified = {}
    )
}