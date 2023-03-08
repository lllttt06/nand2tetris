package projects.`06`


import java.io.File

fun main(args: Array<String>) {
    val inputFilePath = args[0]  // "/Users/kokiyoshida/Desktop/nand2tetris/projects/06/add/Add.asm"

    val outputFileName = inputFilePath.substringBefore(".") + ".hack"
    val outputFile = File(outputFileName)
    if (outputFile.exists()) outputFile.delete()

    val parser = Parser(File(inputFilePath))
    val code = Code()

    while (parser.hasMoreCommands()) {
        parser.advance()
        val commandType = parser.commandType()
        var binaryCode = ""

        when (commandType) {
            CommandType.A_COMMAND -> {
                binaryCode = "0" + convertAddress(parser.symbol())
            }

            CommandType.L_COMMAND -> println("sssss")

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

fun convertAddress(address: String): String {
    if (address.toInt() >= 1.shl(16) - 1) throw IllegalArgumentException("Address must be less than 2**15")
    var binaryString = Integer.toBinaryString(address.toInt())
    while (binaryString.length < 15) {
        binaryString = "0$binaryString"
    }
    return binaryString
}