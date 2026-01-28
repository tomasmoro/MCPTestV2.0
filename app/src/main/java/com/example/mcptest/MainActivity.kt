package com.example.mcptest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mcptest.ui.theme.MCPTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MCPTestTheme {
                PizzaDeliveryApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PizzaDeliveryApp() {
    val items = listOf("Order", "History", "Profile")
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pizza Delivery") },
                // add default elevation/background handling
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, label ->
                    val icon = when (label) {
                        "Order" -> Icons.Filled.ShoppingCart
                        "History" -> Icons.Filled.DateRange
                        "Profile" -> Icons.Filled.Person
                        else -> Icons.Filled.ShoppingCart
                    }

                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = { Icon(imageVector = icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {

            // AnimatedContent provides directional slide animations when target index changes
            AnimatedContent(
                targetState = selectedIndex,
                transitionSpec = {
                    // forward (to higher index): new enters from right, old exits to left
                    if (targetState > initialState) {
                        val enter = slideInHorizontally(animationSpec = tween(300)) { fullWidth -> fullWidth } +
                                fadeIn(animationSpec = tween(300))
                        val exit = slideOutHorizontally(animationSpec = tween(300)) { fullWidth -> -fullWidth } +
                                fadeOut(animationSpec = tween(300))
                        androidx.compose.animation.ContentTransform(
                            targetContentEnter = enter,
                            initialContentExit = exit,
                            sizeTransform = SizeTransform(clip = false)
                        )
                    } else {
                        // backward (to lower index): new enters from left, old exits to right
                        val enter = slideInHorizontally(animationSpec = tween(300)) { fullWidth -> -fullWidth } +
                                fadeIn(animationSpec = tween(300))
                        val exit = slideOutHorizontally(animationSpec = tween(300)) { fullWidth -> fullWidth } +
                                fadeOut(animationSpec = tween(300))
                        androidx.compose.animation.ContentTransform(
                            targetContentEnter = enter,
                            initialContentExit = exit,
                            sizeTransform = SizeTransform(clip = false)
                        )
                    }
                }
            ) { target ->
                when (target) {
                    0 -> OrderScreen()
                    1 -> HistoryScreen()
                    2 -> ProfileScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PizzaDeliveryPreview() {
    MCPTestTheme {
        PizzaDeliveryApp()
    }
}