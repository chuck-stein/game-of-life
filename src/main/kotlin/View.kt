import androidx.compose.desktop.Window
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() = Window(
    size = IntSize(
        width = CELL_SIZE_DP * NUM_COLUMNS, 
        height = CELL_SIZE_DP * NUM_ROWS
    ),
    undecorated = true,
    resizable = false
) {
    val state by programState.collectAsState(InitialState)
    val gameCalculationScope = rememberCoroutineScope { Dispatchers.Default }
    
    MaterialTheme {
        state.let { currentState ->
            when (currentState) {
                is InitialState -> Menu(
                    onStartClick = { gameCalculationScope.launch { runGameOfLife() } }
                )
                is GameOfLifeState -> GameOfLife(currentState)
            }
        }
    }
}

@Composable
fun Menu(onStartClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onStartClick) {
            Text("Begin the Game of Life")
        }
    }
}

@Composable
fun GameOfLife(state: GameOfLifeState) {
    Box {
        Column {
            state.cells.forEach { row ->
                Row {
                    row.forEach { cell ->
                        Surface(
                            color = if (cell is Live) Color.White else Color.Black,
                            border = BorderStroke(CELL_BORDER_SIZE_DP.dp, Color.DarkGray),
                            modifier = Modifier.size(CELL_SIZE_DP.dp),
                            content = {}
                        )
                    }
                }
            }
        }

        Text(
            "Generation ${state.generation}",
            style = TextStyle(
                color = Color.Red,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
        )
    }
}
