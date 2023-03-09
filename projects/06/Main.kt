package projects.`06`


import java.io.File

fun main(args: Array<String>) {
    val inputFilePath = args[0]  // "/Users/kokiyoshida/Desktop/nand2tetris/projects/06/add/Add.asm"

    val code = Code()
    val symbolTable = SymbolTable()
    println("sum".toIntOrNull())

    symbolTable.printSymbolTable()

    /**
     * シンボルテーブルを作成する
     */
    firstPath(code, inputFilePath, symbolTable)
    symbolTable.printSymbolTable()

    /**
     * 2回目のパス
     */
    secondPath(code, inputFilePath, symbolTable)
    symbolTable.printSymbolTable()
}

/**
 * シンボルテーブルの作成
 */
fun firstPath(code: Code, inputFilePath: String, symbolTable: SymbolTable) {
    val parser = Parser(File(inputFilePath))
    var currentROMAddress = 0

    while (parser.hasMoreCommands()) {
        parser.advance()
        val commandType = parser.commandType()

        if (commandType == CommandType.L_COMMAND) {
            val symbol = parser.symbol()
            if (symbolTable.contains(symbol).not()) symbolTable.addEntry(symbol, currentROMAddress)
        } else {
            currentROMAddress++
        }
    }
}

fun secondPath(code: Code, inputFilePath: String, symbolTable: SymbolTable) {
    val parser = Parser(File(inputFilePath))

    val outputFileName = inputFilePath.substringBefore(".") + ".hack"
    val outputFile = File(outputFileName)
    if (outputFile.exists()) outputFile.delete()

    var RAMAddress = 16

    while (parser.hasMoreCommands()) {
        parser.advance()
        val commandType = parser.commandType()
        var binaryCode = ""

        when (commandType) {
            CommandType.A_COMMAND -> {
                val symbol = parser.symbol()
                if (symbol.toIntOrNull() != null) {
                    // A命令の値が数値の場合
                    binaryCode = "0" + convertAddress(symbol)
                } else if (symbolTable.contains(symbol)) {
                    // A命令の値がシンボルであり定義済みの場合
                    binaryCode = "0" + convertAddress(symbolTable.getAddress(symbol).toString())
                } else {
                    // A命令の値がシンボルであり未定義の場合
                    if (RAMAddress == 16384) RAMAddress++
                    symbolTable.addEntry(symbol, RAMAddress)
                    binaryCode = "0" + convertAddress(symbolTable.getAddress(symbol).toString())
                    RAMAddress++
                }
            }

            CommandType.L_COMMAND -> continue

            else -> {
                val destMnemonic = parser.dest()
                val compMnemonic = parser.comp()
                val jumpMnemonic = parser.jump()

                binaryCode = "111" + code.comp(compMnemonic) + code.dest(destMnemonic) + code.jump(jumpMnemonic)
            }
        }
        outputFile.appendText(binaryCode + System.getProperty("line.separator"))
    }
}

private fun convertAddress(address: String): String {
    if (address.toInt() >= 1.shl(16) - 1) throw IllegalArgumentException("Address must be less than 2**15")
    var binaryString = Integer.toBinaryString(address.toInt())
    while (binaryString.length < 15) {
        binaryString = "0$binaryString"
    }
    return binaryString
}