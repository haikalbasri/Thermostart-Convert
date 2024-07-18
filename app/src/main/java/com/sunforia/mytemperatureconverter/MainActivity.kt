package com.sunforia.mytemperatureconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sunforia.mytemperatureconverter.ui.theme.MyTemperatureConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTemperatureConverterTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        StatefulTemperatureInput()
                        ConverterApp()
                        TwoWayConverterApp()
                    }
                }
            }
        }
    }
}

@Composable
fun StatefulTemperatureInput(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.stateful_converter),
            style = MaterialTheme.typography.headlineSmall,
        )
        OutlinedTextField(
            value = input,
            label = { Text(stringResource(R.string.enter_celsius), textAlign = TextAlign.Center) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { newInput ->
                input = newInput
                output = convertToFahrenheit(newInput)
            },
        )
        Text(stringResource(R.string.temperature_fahrenheit, output))
    }
}

private fun convertToFahrenheit(celsius: String) = celsius.toDoubleOrNull()?.let {
    (it * 9 / 5) + 32
}.toString()

@Composable
fun StatelessTemperatureInput(
    input: String,
    output: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.stateful_converter),
            style = MaterialTheme.typography.headlineSmall,
        )
        OutlinedTextField(
            value = input,
            label = { Text(stringResource(R.string.enter_celsius)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange,
        )
        Text(stringResource(R.string.temperature_fahrenheit, output))
    }
}


@Composable
fun ConverterApp(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    Column(modifier) {
        StatelessTemperatureInput(input = input, output = output, onValueChange = {
            input = it
            output = convertToFahrenheit(it)
        })
    }
}

enum class Scale(val scaleName: String) {
    CELCIUS("Celcius"),
    FAHRENHEIT("Fahrenheit"),
}

@Composable
fun GeneralTemperatureInput(
    scale: Scale,
    input: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        OutlinedTextField(
            value = input,
            label = { Text(stringResource(R.string.enter_temperature, scale.scaleName)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange,
        )
    }
}

@Composable
fun TwoWayConverterApp(modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        var celcius by remember { mutableStateOf("") }
        var fahrenheit by remember { mutableStateOf("") }
        Text(stringResource(R.string.two_way_converter))
        GeneralTemperatureInput(scale = Scale.CELCIUS, input = celcius, onValueChange = {
            celcius = it
            fahrenheit = convertToFahrenheit(
                it
            )
        })
        GeneralTemperatureInput(scale = Scale.FAHRENHEIT, input = fahrenheit, onValueChange = {
            fahrenheit = it
            celcius = convertToCelsius(it)
        })
    }
}
private fun convertToCelsius(fahrenheit: String) = fahrenheit.toDoubleOrNull()?.let {
    (it - 32) * 5 / 9
}.toString()