package com.example.aijournalcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aijournalcompanion.ui.theme.AIJournalCompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AIJournalCompanionTheme {
                MainScreen()

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // title , HELP button
        HeaderSection()
        Spacer(modifier = Modifier.height(16.dp))

        // input section
        JournalInputSection()
        Spacer(modifier = Modifier.height(16.dp))

        //result section
        ResultSection()
        Spacer(modifier = Modifier.height(16.dp))

        ChartButtonSection()
        Spacer(modifier = Modifier.height(8.dp))

        SortSection()
        Spacer(modifier = Modifier.height(8.dp))

        SearchSection()
        Spacer(modifier = Modifier.height(16.dp))

    }

}


@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "AI Journal Companion",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick ={}
        ){
            Text(text = "HELP")
        }
    }
}
@Composable
fun JournalInputSection() {

    var inputText by remember { mutableStateOf("") }
    Column{
        //label text
        Text(
            text = "How was today?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        //input textbox
        OutlinedTextField(
            value = inputText,
            onValueChange = {inputText = it},
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Analyze button
        Button(
            onClick = {/*API*/},
            modifier = Modifier.align (Alignment.End)
        ){
            Text(text = "ANALYZE")
        }
    }
}
@Composable
fun ResultSection() {
    val emotion = "HAPPY"
    val advice = "Enjoy your day"

    Column{
        //label text
        Text(
            text = "Result",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),

            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            OutlinedCard(
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Emotion",
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = emotion,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            OutlinedCard(
                modifier = Modifier.weight(1f).fillMaxHeight()
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "😁",
                        fontSize = 48.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Today's Advice",
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = advice,
                    fontSize = 18.sp
                )
            }
        }
    }
}
@Composable
fun ChartButtonSection(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        // list label
        Text(
            text = "Your Journals",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        //chart button
        Button(
            onClick = {}
        ){
            Text(text = "CHART")
        }
    }
}

@Composable
fun SortSection(){
    var selectedOption by remember { mutableStateOf("Bubble Sort") }
    var expanded by remember { mutableStateOf(false) }
    val sortOptions = listOf("Bubble Sort", "Insertion Sort", "Selection Sort")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        //Sort dropdown button
        Box(modifier = Modifier.weight(1f)) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = selectedOption, fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "▽", fontSize = 12.sp)

            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sortOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )

                }

            }
        }
        Button(
            onClick = {}
        ){
            Text(text = "SORT")
        }
    }
}

@Composable
fun SearchSection(){

    var selectedOption by remember { mutableStateOf("Binary Tree") }
    var expanded by remember { mutableStateOf(false) }
    val searchOptions = listOf("Binary Tree", "HashMap", "Doubly Linked List")

    var searchTarget by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically


    ){
        Box{
            OutlinedButton(
                onClick = {expanded = true}
            ) {
                Text(text = selectedOption, fontSize = 12.sp)
                Text(text = "▽", fontSize = 12.sp)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ){
                searchOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )

                }
            }
        }
        OutlinedTextField(
            value = searchTarget,
            onValueChange = { searchTarget = it },
            modifier = Modifier.weight(1f).heightIn(max = 40.dp)
        )
        Button(
            onClick = {}
        ){
            Text(text = "SEARCH")
        }
    }
}