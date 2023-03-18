package projects.`07`

import java.io.File

class MyCodeWriter(private val file: File) {

    private val arithmeticMap = mapOf(
        "add" to "M=M+D",
        "sub" to "M=M-D",
        "neg" to "M=-M",
        "and" to "M=M&D",
        "or" to "M=M|D",
        "not" to "M=!M",
//        "eq" to eq(),
//        "gt" to gt(),
//        "lt" to lt()
    )

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
        val operator = arithmeticMap[command] ?: throw IllegalArgumentException()
        val assemblyCode =
            if (command == "neg" || command == "not") genLine(pop(), decrementSP(), loadSP(), operator, incrementSP())
            else genLine(decrementSP(), loadSP(), operator, incrementSP())

        file.appendText(assemblyCode)
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


    // vararg(可変長引数) commandsをそれぞれ改行して出力する関数これにより、いちいちコマンドの最後に\nを入れずにすむ
    private fun genLine(vararg commands: String): String =
        commands.joinToString(separator = "\n", postfix = "\n") { it.replace("(\\n)+$".toRegex(), "") }
}