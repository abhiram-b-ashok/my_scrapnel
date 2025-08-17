package com.example.myscrapnel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.myscrapnel.ui.theme.MyScrapnelTheme
import com.example.myscrapnel.views.create_scrapnel_page.CreateScrapnelPage
import com.example.myscrapnel.views.home_page.Homepage
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false) // must-have for edge-to-edge

        setContent {
            MyScrapnelTheme {
                val systemUiController = rememberSystemUiController()
                val colorScheme = MaterialTheme.colorScheme

                SideEffect {
                    // Status bar: same color as header
                    systemUiController.setStatusBarColor(
                        color = colorScheme.primary,
                        darkIcons = colorScheme.background.luminance() > 0.5f
                    )
                    // Nav bar: match background
                    systemUiController.setNavigationBarColor(
                        color = colorScheme.background,
                        darkIcons = colorScheme.background.luminance() > 0.5f
                    )
                }

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(colorScheme.background) // fill behind nav bar
                        .padding(WindowInsets.navigationBars.asPaddingValues()) // bottom padding only
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        content = { innerPadding ->
                            CreateScrapnelPage(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyScrapnelTheme {
        Greeting("Android")
    }
}



