package com.msgkatz.ratesapp.chartdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msgkatz.ratesapp.chartdemo.ui.chart1.ChartScreen
import com.msgkatz.ratesapp.chartdemo.ui.chart1.ChartScreen2
import com.msgkatz.ratesapp.chartdemo.ui.chart2.DynamicCryptoChart
import com.msgkatz.ratesapp.chartdemo.ui.chart2.DynamicCryptoChart2
import com.msgkatz.ratesapp.chartdemo.ui.theme.ChartsnratesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChartsnratesTheme {
                //Chart1()
                //Chart2()
                Chart22()
            }
        }
    }
}

@Composable
fun Chart1() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        //ChartScreen(modifier = Modifier.padding(innerPadding))
        ChartScreen2(modifier = Modifier.padding(innerPadding))

    }
}

@Composable
fun Chart2() {
    Scaffold(
        topBar = { Text("Crypto Chart Demo", modifier = Modifier.padding(16.dp)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp) // Add some overall padding
        ) {
            DynamicCryptoChart(
                modifier = Modifier
                    .fillMaxWidth() // Take available width
                // Height is defined inside DynamicCryptoChart
            )
            // Add other UI elements below the chart if needed
        }
    }
}

@Composable
fun Chart22() {
    Scaffold(
        topBar = { Text("Crypto Chart Demo", modifier = Modifier.padding(16.dp)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp) // Add some overall padding
        ) {
            DynamicCryptoChart2(
                modifier = Modifier
                    .fillMaxWidth() // Take available width
                // Height is defined inside DynamicCryptoChart
            )
            // Add other UI elements below the chart if needed
        }
    }
}

@Composable
@Preview
fun PreviewChart2() {
    ChartsnratesTheme {
        Chart2()
    }
}