import kotlinx.coroutines.delay
import kotlin.random.Random

suspend fun runGameOfLife() {
    programState.value = createSeedState()
    while (true) {
        programState.value.let { currentState ->
            if (currentState !is GameOfLifeState) return
            println("emitted generation ${currentState.generation} at ${System.currentTimeMillis()}")
            delay(ITERATION_TIME_MILLIS)
            programState.value = currentState.update()
        }
    }
}

fun createSeedState() = GameOfLifeState(
    generation = 0,
    cells = List(NUM_ROWS) {
        List(NUM_COLUMNS) {
            if (Random.nextBoolean()) Live else Dead
        }
    }
)

fun GameOfLifeState.update() = GameOfLifeState(
    generation = generation + 1,
    cells = updateCells(cells)
)

fun updateCells(previousCells: List<List<CellState>>) =
    List(previousCells.size) { row ->
        List(previousCells[row].size) { col ->
            val previousCell = previousCells[row][col]
            val numLiveNeighbors = findNumLiveNeighbors(row, col, previousCells)
            when {
                previousCell is Live && numLiveNeighbors in 2..3 -> Live
                previousCell is Dead && numLiveNeighbors == 3 -> Live
                else -> Dead
            }
        }
    }

fun findNumLiveNeighbors(row: Int, col: Int, cells: List<List<CellState>>): Int {
    var numLiveNeighbors = 0
    for (neighborDy in -1..1) {
        for (neighborDx in -1..1) {
            if (neighborDy == 0 && neighborDx == 0) continue // do not count self as neighbor
            val neighborRow = Math.floorMod(row + neighborDy, cells.size)
            val neighborCol = Math.floorMod(col + neighborDx, cells[row].size)
            val neighbor = cells[neighborRow][neighborCol]
            if (neighbor is Live) numLiveNeighbors++
        }
    }
    return numLiveNeighbors
}
