package projects.`07`

import java.io.File

class MyCodeWriter(private val file: File) {

    /**
     * 新しいVMファイルの変換が開始したことを知らせる
     */
    fun setFileName(fileName: String) {
        TODO()
    }

    /**
     * 与えられた算術コマンドをアセンブリコードに変換し、ファイルに書き込む
     */
    fun writeArithmetic(command: String) {
        val commands = when (command) {
            "add" -> "M=M+D"
            "sub" -> "M=M-D"
            "neg" -> "M=-M"
            "and" -> "M=M&D"
            "or" -> "M=M|D"
            "not" -> "M=!M"
            "eq" -> eq()
            "gt" -> gt()
            "lt" -> lt()
            else -> throw IllegalArgumentException()
        }

        val assemblyCode = genLine(pop(), decrementSP(), loadSP(), commands, incrementSP())

        file.appendText(assemblyCode)
//        val arithmeticMap = mapOf(
//            "add" to "M=M+D",
//            "sub" to "M=M-D",
//            "neg" to "M=-M",
//            "and" to "M=M&D",
//            "or" to "M=M|D",
//            "not" to "M=!M",
//            "eq" to eq(),
//            "gt" to gt(),
//            "lt" to lt()
//        )
//
//        val operator = arithmeticMap[command] ?: throw IllegalArgumentException()
//        val assemblyCode =
//            if (command == "neg" || command == "not") genLine(decrementSP(), loadSP(), operator, incrementSP())
//            else genLine(pop(), decrementSP(), loadSP(), operator, incrementSP())

//        file.appendText(assemblyCode)
    }

    /**
     * C_PUSHまたはC_POPコマンドをアセンブリコードに変換し、ファイルに書き込む
     */
    fun writePushPop(command: String, segment: String, index: Int) {
        val assemblyCode = if (command == "push") {
            if (segment == "constant") {
                pushConst(index)
            } else {
                TODO()
            }
        } else {
            TODO()
        }

        file.appendText(assemblyCode)
    }

    /**
     * 出力ファイルを閉じる
     * KotlinのFile::appendText()メソッドの中で書き込みと同時にclose処理をおこなっているので今回は実装しない
     */
    fun close() {}

    // SPを1つ減らす
    private fun decrementSP() = genLine("@SP", "M=M-1")

    // SPの値をAレジスタに格納する
    private fun loadSP() = genLine("@SP", "A=M")

    // SPを1つ増やす
    private fun incrementSP() = genLine("@SP", "M=M+1")

    // SPの値の1つ前の値(直前にstackにpushされた値)をDレジスタに格納する
    private fun pop() = genLine(decrementSP(), loadSP(), "D=M", "M=0")

    // Dレジスタの値をスタックに格納する
    private fun push() = genLine(loadSP(), "M=D", incrementSP())

    // stackにconstをpushする
    private fun pushConst(const: Int) = genLine("@$const", "D=A") + push()

    private var eqNum = 0
    private var gtNum = 0
    private var ltNum = 0
    private fun eq(): String {
        val command = genLine(
            "D=M-D",
            "@EQ$eqNum",
            "D;JEQ",
            "@NEQ$eqNum",
            "0;JMP",
            "(EQ$eqNum)",
            loadSP(),
            "M=-1",
            "@EQNEXT$eqNum",
            "0;JMP",
            "(NEQ$eqNum)",
            loadSP(),
            "M=0",
            "@EQNEXT$eqNum",
            "0;JMP",
            "(EQNEXT$eqNum)"
        )
        eqNum++
        return command
    }

    private fun gt(): String {
        val command = genLine(
            "D=M-D",
            "@GT$gtNum",
            "D;JGT",
            "@NGT$gtNum",
            "0;JMP",
            "(GT$gtNum)",
            loadSP(),
            "M=-1",
            "@GTNEXT$gtNum",
            "0;JMP",
            "(NGT$gtNum)",
            loadSP(),
            "M=0",
            "@GTNEXT$gtNum",
            "0;JMP",
            "(GTNEXT$gtNum)"
        )
        gtNum++
        return command
    }

    private fun lt(): String {
        val command = genLine(
            "D=M-D",
            "@LT$ltNum",
            "D;JLT",
            "@NLT$ltNum",
            "0;JMP",
            "(LT$ltNum)",
            loadSP(),
            "M=-1",
            "@LTNEXT$ltNum",
            "0;JMP",
            "(NLT$ltNum)",
            loadSP(),
            "M=0",
            "@LTNEXT$ltNum",
            "0;JMP",
            "(LTNEXT$ltNum)"
        )
        ltNum++
        return command
    }


    // vararg(可変長引数) commandsをそれぞれ改行して出力する関数これにより、いちいちコマンドの最後に\nを入れずにすむ
    private fun genLine(vararg commands: String): String =
        commands.joinToString(separator = "\n", postfix = "\n") { it.replace("(\\n)+$".toRegex(), "") }
}