package bullscows

import kotlin.system.exitProcess

const val DIGITS = "0123456789abcdefghijklmnopqrstuvwxyz"

data class Game(val numSym: Int, val validSymbols: String, val secret: String, var turn: Int = 0, var bulls: Int = 0, var cows: Int = 0) {
    fun resetScore() {
        bulls = 0
        cows = 0
    }
}

fun gameInit(): Game {
    val length = println("Input the length of the secret code:").run { readln() }
    if (length.any { it !in '0'..'9' }) {
        println("Error: $length isn't a valid number.")
        exitProcess(-2)
    }
    val len = length.toInt()
    if (len < 1) {
        println("Error: Length of $len is not valid. The length of the secret code cannot be less than 1.")
        exitProcess(-3)
    }
    val numSym = println("Input the number of possible symbols in the code:").run { readln() }
    if (numSym.any { it !in '0'..'9' }) {
        println("Error: $numSym isn't a valid number.")
        exitProcess(-5)
    }
    val ns = numSym.toInt()
    if (len > ns) {
        println("Error: it's not possible to generate a code with a length of $len with $ns unique symbols.")
        exitProcess(-13)
    }
    if (ns > DIGITS.length) {
        println("Error: maximum number of possible symbols in the code is ${DIGITS.length} (0-9, a-${DIGITS.last()}).")
        exitProcess(-89)
    }
    val valSym = "(0-${minOf(9, ns - 1)}${if (ns == 11) ", a" else if (ns > 11) ", a-${DIGITS[ns - 1]}" else ""})"
    println("The secret is prepared: ${"*".repeat(len)} $valSym.")
    println("Okay, let's start a game!")
    return Game(ns, valSym, DIGITS.take(ns).toList().shuffled().take(len).joinToString(""))
}

fun main() {
    val game = gameInit()
    while (game.bulls != game.secret.length) {
        println("Turn ${++game.turn}:")
        game.resetScore()
        val guess = readln()
        if (guess.length != game.secret.length) {
            println("Error: The length of the guess (${guess.length}) is not the same as the length of the secret (${game.secret.length}).")
            exitProcess(-233)
        }
        if (guess.any { it !in DIGITS.take(game.numSym) }) {
            println("Error: The guess contains invalid symbols. Valid symbols are: ${game.validSymbols}")
            exitProcess(-1597)
        }
        guess.forEachIndexed { index, c -> if (c == game.secret[index]) game.bulls++ else if (c in game.secret) game.cows++ }
        println("Grade: ${game.bulls} bull${if (game.bulls != 1) "s" else ""}${if (game.bulls != game.secret.length) " and ${game.cows} cow${if (game.cows != 1) "s" else ""}" else ""}")
    }
    println("Congratulations! You guessed the secret code.")
}