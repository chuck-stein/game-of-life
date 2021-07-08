import kotlinx.coroutines.flow.MutableStateFlow

val programState = MutableStateFlow<ProgramState>(InitialState)

sealed interface ProgramState
object InitialState : ProgramState
data class GameOfLifeState(
    val generation: Int,
    val cells: List<List<CellState>> // list of rows
) : ProgramState

sealed interface CellState
object Live : CellState
object Dead : CellState
