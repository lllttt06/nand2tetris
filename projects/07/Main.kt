package projects.`07`

import java.io.File

fun main(args: Array<String>) {
    val inputFilePath = args[0]
    val inputFile = File(inputFilePath)

    val outputFilePath = inputFilePath.substringBeforeLast(".") + ".asm"
    val outputFile = File(outputFilePath)
    if (outputFile.exists()) outputFile.delete()

    val parser = Parser(inputFile)
    val codeWriter = MyCodeWriter(outputFile)

//    var num = 0
//    val hashMap = mapOf("eq" to num)
//
//    for (i in 0..10) {
//        println(hashMap["eq"])
//        num++
//    }
    while (parser.hasMoreCommands()) {
        parser.advance()

        when (parser.commandType()) {
            CommandType.C_ARITHMETIC -> {
                val command = parser.arg1()
                codeWriter.writeArithmetic(command)
            }

            CommandType.C_PUSH, CommandType.C_POP -> {
                val segment = parser.arg1()
                val index = parser.arg2()
                codeWriter.writePushPop("push", segment, index)
            }

            CommandType.C_LABEL -> {
                TODO()
            }

            CommandType.C_GOTO -> {
                TODO()
            }

            CommandType.C_IF -> {
                TODO()
            }

            CommandType.C_FUNCTION -> {
                TODO()
            }

            CommandType.C_CALL -> {
                TODO()
            }

            CommandType.C_RETURN -> {
                TODO()
            }
        }
    }
}