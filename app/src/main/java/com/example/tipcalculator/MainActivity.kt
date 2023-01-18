package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.components.InputField
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import com.example.tipcalculator.utill.calculateTotalPerPerson
import com.example.tipcalculator.utill.calculateTotalTip
import com.example.tipcalculator.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        MainContent()
                    }


                }
            }
        }
    }
}


@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {



    Surface(modifier = Modifier
        .padding(25.dp)
        .fillMaxWidth()
        .height(150.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(15.dp))),
        color = Color(0xFFE1C8FD)
    ) {
        Column(modifier = Modifier ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            val total = "%.2f".format(totalPerPerson)
            Text(text = "Total Per Person",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold)
            Text(text = "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold)
            
        }

    }
}


@Preview
@Composable
fun MainContent(){

    BillForm{}

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
){
    val totalBillState = remember{ mutableStateOf("") }
    val validState = remember(totalBillState.value){ totalBillState.value.trim().isNotEmpty() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val splitCount = remember{
        mutableStateOf(1)
    }
    val sliderPositionState = remember {
        mutableStateOf(0f)
    }
    val tipPercentage = (sliderPositionState.value * 100).toInt()
    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    TopHeader(totalPerPerson = totalPerPersonState.value)


    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        shape = RoundedCornerShape(corner = CornerSize(10)),
        border = BorderStroke(1.dp, color = Color.LightGray)
    ) {
        Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                modifier = modifier.padding(top = 10.dp),
                isSingleLine = true,
                onAction = KeyboardActions{
                    if (!validState) return@KeyboardActions
                    onValueChange(totalBillState.value.trim())
                    keyboardController?.hide()
                })
            if(validState){
                
                Row(modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start) {
                    Text(text = "Split",
                    modifier = Modifier.align(
                        alignment = Alignment.CenterVertically
                    ))

                    Spacer(modifier = Modifier.width(150.dp))

                    Row(modifier = Modifier.padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.End) {
                        RoundIconButton(imageVector = Icons.Default.Remove,
                            onClick = { if (splitCount.value >1) splitCount.value -= 1
                                totalPerPersonState.value = calculateTotalPerPerson(totalbill = totalBillState.value.toDouble(),
                                    splitBy = splitCount.value,tipPercentage = tipPercentage)})
                        
                        Text(text = "${splitCount.value}",
                            modifier = Modifier
                                .padding(4.dp)
                                .align(
                                    alignment = Alignment.CenterVertically
                                )
                                .padding(start = 9.dp, end = 9.dp)
                        )

                        RoundIconButton(imageVector = Icons.Default.Add,
                            onClick = { if (splitCount.value < 100) {
                                splitCount.value += 1
                            }
                                totalPerPersonState.value = calculateTotalPerPerson(totalbill = totalBillState.value.toDouble(),
                                    splitBy = splitCount.value,tipPercentage = tipPercentage)
                            })
                    }

                }
            //tip row
            Row(modifier = Modifier.padding( vertical = 12.dp),
            horizontalArrangement = Arrangement.Start) {
                Text(text = "Tip",
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically)
                        )

                Spacer(modifier = Modifier.width(250.dp))

                Text(text = "$ ${tipAmountState.value}",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically))

            }
            Column(
                modifier = Modifier.padding(vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                ) {
                Text(text = "$tipPercentage %")


                Slider(value = sliderPositionState.value,
                    onValueChange = {
                        newVal -> sliderPositionState.value = newVal

                        tipAmountState.value = calculateTotalTip(totalbill = totalBillState.value.toDouble(), tipPercentage = tipPercentage)

                         totalPerPersonState.value = calculateTotalPerPerson(totalbill = totalBillState.value.toDouble(),
                        splitBy = splitCount.value,tipPercentage = tipPercentage)
                    },
                modifier = Modifier.padding(start = 25.dp, end = 25.dp))
            }
                
           }else Box {}
        }

    }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(15.dp)) {
            TopHeader()
        }

    }

}