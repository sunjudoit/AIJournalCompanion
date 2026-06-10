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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.aijournalcompanion.ui.theme.AIJournalCompanionTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.aijournalcompanion.utils.SortUtils
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.aijournalcompanion.utils.SearchUtils
import com.example.aijournalcompanion.ui.PieChartDialog
import androidx.compose.ui.platform.LocalContext
import com.example.aijournalcompanion.ui.HelpDialog
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

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

fun getEmoji(emotion: String): String {
    return when (emotion.uppercase()) {
        "HAPPINESS" -> "😊"
        "SADNESS"   -> "😢"
        "GRATITUDE" -> "🙏"
        "ANXIETY"   -> "😰"
        "ANGER"     -> "😡"
        "TIREDNESS" -> "😴"
        "SURPRISE"  -> "😲"
        else        -> "🤔"
    }
}

fun getTodayDate(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    return sdf.format(Date())
}

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    var inputContent by remember { mutableStateOf("") }
    var emotionResult by remember { mutableStateOf("") }
    var adviceResult by remember { mutableStateOf("") }

    val journalList = remember { mutableStateListOf<JournalEntry>() }
    val searchResultList = remember { mutableStateListOf<JournalEntry>() }
    var isSearching by remember { mutableStateOf(false) }

    val displayList = if (isSearching) searchResultList else journalList

    val scope = rememberCoroutineScope()

    var showChartDialog by remember {
        mutableStateOf(false)
    }

    var showHelpDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // title , HELP button
        item {
            HeaderSection(
                onHelpClick = {
                    showHelpDialog = true
                }
            )
        }
        // input section
        item{JournalInputSection(
            inputContent = inputContent,
            onTextChange = {inputContent = it},
            onAnalyzeClick = {
                scope.launch {
                    try{
                        val response = RetrofitClient.api.analyzeJournal(
                            AnalyzeRequest(content = inputContent)
                        )
                        emotionResult = response.emotion
                        adviceResult = response.advice

                        journalList.add(
                            JournalEntry(
                                emotion = response.emotion,
                                emoji   = getEmoji(response.emotion),
                                content = inputContent,
                                advice  = response.advice,
                                date    = getTodayDate()
                            )
                        )
                        inputContent = ""


                    }catch(e: Exception){
                        emotionResult = "Error"
                        adviceResult = "Error"
                    }
                }
            }
        )
        }


        //result section
        item{ResultSection(emotion = emotionResult, advice = adviceResult)}

        item {
            ChartButtonSection(
                onChartClick = { showChartDialog = true }
            )
        }

        item {
            SortSection(
                journalList = journalList
            )
        }

        item {
            SearchSection(
                journalList = journalList,
                searchResultList = searchResultList,
                onSearchStateChange = { isSearching = it }
            )
        }


        item {
            TrashZone()
        }

        items(displayList) { card ->
            DraggableJournalCard(
                card = card,
                onDelete = {
                    journalList.remove(card)
                    searchResultList.remove(card)

                    if (journalList.isEmpty()) {
                        searchResultList.clear()
                        isSearching = false
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }


    }
    if (showChartDialog) {
        PieChartDialog(
            journalList = journalList,
            onDismiss = { showChartDialog = false }
        )
    }

    if (showHelpDialog) {

        HelpDialog(
            context = context,
            onDismiss = {
                showHelpDialog = false
            }
        )
    }

}


@Composable
fun HeaderSection(
    onHelpClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "AI Journal Companion",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = onHelpClick
        ) {
            Text("HELP")
        }
    }
}
@Composable
fun JournalInputSection(
    inputContent: String,
    onTextChange: (String) -> Unit,
    onAnalyzeClick: () -> Unit
) {

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
            value = inputContent,
            onValueChange = onTextChange,
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        //Analyze button
        Button(
            onClick = onAnalyzeClick,
            modifier = Modifier.align (Alignment.End)
        ){
            Text(text = "ANALYZE")
        }
    }
}
@Composable
fun ResultSection(emotion:String, advice:String) {

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
                    Text(text = getEmoji(emotion), fontSize = 48.sp)
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
fun ChartButtonSection(
    onChartClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Your Journals",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = onChartClick
        ) {
            Text(text = "CHART")
        }
    }
}

@Composable
fun SortSection(
    journalList: SnapshotStateList<JournalEntry>
) {
    var selectedOption by remember { mutableStateOf("Bubble Sort") }
    var expanded by remember { mutableStateOf(false) }
    val sortOptions = listOf("Bubble Sort", "Insertion Sort", "Selection Sort")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
            onClick = {
                val sortedList = when (selectedOption) {
                    "Bubble Sort" -> SortUtils.bubbleSort(journalList)
                    "Insertion Sort" -> SortUtils.insertionSort(journalList)
                    "Selection Sort" -> SortUtils.selectionSort(journalList)
                    else -> journalList
                }

                journalList.clear()
                journalList.addAll(sortedList)
            }
        ) {
            Text(text = "SORT")
        }
    }
}

@Composable
fun SearchSection(
    journalList: SnapshotStateList<JournalEntry>,
    searchResultList: SnapshotStateList<JournalEntry>,
    onSearchStateChange: (Boolean) -> Unit
) {
    var selectedOption by remember { mutableStateOf("Binary Tree") }
    var expanded by remember { mutableStateOf(false) }
    val searchOptions = listOf("Binary Tree", "HashMap", "Doubly Linked List")

    var searchTarget by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            OutlinedButton(
                onClick = { expanded = true }
            ) {
                Text(text = selectedOption, fontSize = 12.sp)
                Text(text = "▽", fontSize = 12.sp)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
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
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type Emotion", fontSize = 12.sp) },
            singleLine = true
        )

        Button(
            onClick = {
                searchResultList.clear()

                if (searchTarget.isBlank()) {
                    onSearchStateChange(false)
                    return@Button
                }

                val result = when (selectedOption) {
                    "Binary Tree" -> SearchUtils.searchWithBinaryTree(journalList, searchTarget)
                    "HashMap" -> SearchUtils.searchWithHashMap(journalList, searchTarget)
                    "Doubly Linked List" -> SearchUtils.searchWithDoublyLinkedList(journalList, searchTarget)
                    else -> emptyList()
                }

                searchResultList.addAll(result)
                onSearchStateChange(true)
            }
        ) {
            Text(text = "SEARCH")
        }
    }
}

@Composable
fun JournalList(cardList: List<JournalEntry>){
    Column {
        cardList.forEach { card ->
            JournalCard(card = card)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun JournalCard(card: JournalEntry){
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            //Emoji, Emotion, Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = card.emoji, fontSize = 16.sp)
                Text(
                    text = card.emotion,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = card.date,
                    fontSize = 10.sp
                )

            }
            Spacer(modifier = Modifier.height(6.dp))
            // Journal content
            Text(
                text = card.content,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.height(6.dp))
            //acvice
            Text(
                text = "${card.advice}",
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun TrashZone() {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🗑 Drag a journal card up here to delete",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DraggableJournalCard(
    card: JournalEntry,
    onDelete: () -> Unit
) {
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = offsetY.roundToInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (offsetY < -30f) {
                            onDelete()
                        }
                        offsetY = 0f
                    },
                    onDragCancel = {
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetY += dragAmount.y
                    }
                )
            }
    ) {
        JournalCard(card = card)
    }
}