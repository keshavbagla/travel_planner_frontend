package com.example.travel_planner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.R
import com.example.travel_planner.data.AuthRepository
import com.example.travel_planner.ui.theme.*
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoginScreen(
    prefilledEmail: String = "",
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf(prefilledEmail) }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Voyago AI logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(88.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            "Welcome back",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            color = Navy
        )
        Text(
            "Sign in to keep planning with Voyago AI.",
            style = MaterialTheme.typography.bodyLarge,
            color = Slate,
            modifier = Modifier.padding(top = 6.dp, bottom = 40.dp)
        )

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Email", style = MaterialTheme.typography.labelLarge, color = Navy)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; errorMessage = null },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    placeholder = { Text("jane@example.com", fontSize = 16.sp) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Border,
                        focusedBorderColor = Teal
                    )
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Password", style = MaterialTheme.typography.labelLarge, color = Navy)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorMessage = null },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    placeholder = { Text("••••••••••••", fontSize = 16.sp) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Border,
                        focusedBorderColor = Teal
                    )
                )
            }
        }

        errorMessage?.let {
            Text(it, color = Coral, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 16.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(top = 28.dp)
                .clip(RoundedCornerShape(29.dp))
                .background(Coral)
                .clickable(enabled = !isLoading) {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Enter email and password"
                        return@clickable
                    }
                    isLoading = true
                    scope.launch {
                        val result = AuthRepository.login(email, password)
                        isLoading = false
                        result.onSuccess { onLoginSuccess() }
                            .onFailure { errorMessage = "Incorrect email or password" }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
            } else {
                Text("Sign In", color = White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
            Text("Don't have an account? ", style = MaterialTheme.typography.bodyLarge, color = Slate)
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                "Create one",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Teal,
                modifier = Modifier.clickable { onSignUpClick() }
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}