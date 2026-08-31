package com.example.travel_planner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travel_planner.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.travel_planner.R

@Composable
fun SignUpScreen(
    prefilledEmail: String = "",
    onCreateAccount: (name: String, email: String, password: String) -> Unit = { _, _, _ -> },
    onSignInClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(prefilledEmail) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Navy)
                .padding(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.align(Alignment.BottomStart)) {
                Text("Your journey starts here.", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp)
                Text(
                    "Join 100k+ smart travelers planning with Voyago AI.",
                    color = White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Create your account", style = MaterialTheme.typography.headlineMedium, color = Navy)

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LabeledField(label = "Full Name", value = name, onValueChange = { name = it }, placeholder = "Jane Doe")
                LabeledField(
                    label = "Work Email",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "jane@example.com",
                    keyboardType = KeyboardType.Email
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Password", style = MaterialTheme.typography.labelLarge, color = Navy)
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••••••") },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = Slate
                                )
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Border,
                            focusedBorderColor = Teal
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Coral)
                    .clickable { onCreateAccount(name, email, password) },
                contentAlignment = Alignment.Center
            ) {
                Text("Create Account", color = White, style = MaterialTheme.typography.labelLarge)
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f).height(1.dp).background(Border))
                Text("or continue with", style = MaterialTheme.typography.bodySmall, color = Slate)
                Box(Modifier.weight(1f).height(1.dp).background(Border))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SocialButton(label = "Google", modifier = Modifier.weight(1f))
                SocialButton(label = "Apple", modifier = Modifier.weight(1f))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Already have an account? ", style = MaterialTheme.typography.bodyMedium, color = Slate)
                Text(
                    "Sign In",
                    style = MaterialTheme.typography.labelLarge,
                    color = Teal,
                    modifier = Modifier.clickable { onSignInClick() }
                )
            }
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = Navy)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Border,
                focusedBorderColor = Teal
            )
        )
    }
}

@Composable
private fun SocialButton(
    label: String,
    modifier: Modifier = Modifier
) {
    val logo = when (label) {
        "Google" -> R.drawable.google
        "Apple" -> R.drawable.apple_logo
        else -> null
    }

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                1.dp,
                Border,
                RoundedCornerShape(8.dp)
            )
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            logo?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "$label logo",
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Navy
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen()
}