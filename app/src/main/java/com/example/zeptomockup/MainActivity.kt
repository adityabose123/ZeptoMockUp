package com.example.zeptomockup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zeptomockup.ui.ZeptoApp
import com.example.zeptomockup.ui.ZeptoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZeptoTheme {
                val cartViewModel: CartViewModel = viewModel()
                ZeptoApp(cartViewModel)
            }
        }
    }
}
