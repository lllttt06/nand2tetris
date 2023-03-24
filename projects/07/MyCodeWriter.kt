package projects.`07`

import java.io.File

class MyCodeWriter(private val file: File) {
    private var fileName = ""
    private var returnAddressCounter = 0

    /**
     * 新しいVMファイルの変換が開始したことを知らせる
     */
    fun setFileName(fileName: String) {
        this.fileName = fileName
    }

    /**
     * VMの初期化
     */
    fun writeInit() {
        val assemblyCode = genLine("@256", "D=A", "@SP", "M=D")
        file.appendText(assemblyCode)
        writeCall("Sys.init", 0)
    }

    /**
     * labelコマンドを行う
     */
    fun writeLabel(label: String) {
        val assemblyCode = genLine("($fileName$label)")
        file.appendText(assemblyCode)
    }

    /**
     * gotoコマンドを行う
     */
    fun writeGoto(label: String) {
        val assemblyCode = genLine("@$fileName$label", "0;JMP")
        file.appendText(assemblyCode)
    }

    /**
     * if-gotoコマンドを行う
     */
    fun writeIf(label: String) {
        val assemblyCode = genLine(pop(), "@$fileName$label", "D;JNE")
        file.appendText(assemblyCode)
    }

    /**
     * callコマンドを行う
     */
    fun writeCall(functionName: String, numArgs: Int) {
        var assemblyCode = ""
        // push return-address
        assemblyCode += genLine("@return-address$returnAddressCounter", "D=A", push())
        // push LCL
        assemblyCode += genLine("@LCL", "D=M", push())
        // push ARG
        assemblyCode += genLine("@ARG", "D=M", push())
        // push THIS
        assemblyCode += genLine("@THIS", "D=M", push())
        // push THAT
        assemblyCode += genLine("@THAT", "D=M", push())
        // ARG = SP - n - 5
        assemblyCode += genLine("@SP", "D=M", "@$numArgs", "D=D-A", "@5", "D=D-A", "@ARG", "M=D")
        // LCL = SP
        assemblyCode += genLine("@SP", "D=M", "@LCL", "M=D")
        // goto f
        assemblyCode += genLine("@$functionName", "0;JMP")
        // (return-address)
        assemblyCode += genLine("(return-address$returnAddressCounter)")
        file.appendText(assemblyCode)
        returnAddressCounter++
    }

    /**
     * returnコマンド
     */
    fun writeReturn() {
        var assemblyCode = ""
        // frame = LCL
        assemblyCode += genLine("@LCL", "D=M", "@frame", "M=D")
        // ret = frame - 5 (リターンアドレスを取得)
        assemblyCode += genLine("@5", "D=A", "@frame", "A=M-D", "D=M", "@ret", "M=D")
        // ARG[0] = pop() (関数の呼び出し側のために、戻り値をセットする)
        assemblyCode += genLine(pop() + "@ARG", "A=M", "M=D")
        // SP = ARG + 1
        assemblyCode += genLine("@ARG", "D=M", "@SP", "M=D+1")
        // THAT = frame - 1
        assemblyCode += genLine("@frame", "A=M-1", "D=M", "@THAT", "M=D")
        // THIS = frame - 2
        assemblyCode += genLine("@2", "D=A", "@frame", "A=M-D", "D=M", "@THIS", "M=D")
        // ARG = frame - 3
        assemblyCode += genLine("@3", "D=A", "@frame", "A=M-D", "D=M", "@ARG", "M=D")
        // LCL = frame - 4
        assemblyCode += genLine("@4", "D=A", "@frame", "A=M-D", "D=M", "@LCL", "M=D")
        // goto ret
        assemblyCode += genLine("@ret", "A=M", "0;JMP")
        file.appendText(assemblyCode)
    }

    /**
     * functionコマンドを行う
     */
    fun writeFunction(functionName: String, numLocals: Int) {
        var assemblyCode = genLine("($functionName)")
        for (i in 0 until numLocals) assemblyCode += pushConst(0)
        file.appendText(assemblyCode)
    }


    /**
     * 与えられた算術コマンドをアセンブリコードに変換し、ファイルに書き込む
     */
    fun writeArithmetic(command: String) {
        val operator = when (command) {
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

        val assemblyCode =
            if (command == "neg" || command == "not") genLine(decrementSP(), loadSP(), operator, incrementSP())
            else genLine(pop(), decrementSP(), loadSP(), operator, incrementSP())

        file.appendText(assemblyCode)
    }

    /**
     * C_PUSHまたはC_POPコマンドをアセンブリコードに変換し、ファイルに書き込む
     */
    fun writePushPop(command: String, segment: String, index: Int) {
        val assemblyCode = if (command == "push") {
            when (segment) {
                "constant" -> pushConst(index)
                "local", "argument", "this", "that" -> pushSegment(segment, index)
                "pointer", "temp" -> pushPointerOrTemp(segment, index)
                "static" -> genLine("@$fileName.$index", "D=M", push())
                else -> throw IllegalArgumentException()
            }
        } else {
            when (segment) {
                "argument", "local", "this", "that", "pointer", "temp" -> popToSegment(segment, index)
                "static" -> genLine(pop(), "@$fileName.$index", "M=D")
                else -> throw IllegalArgumentException()
            }
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

    // stackに各セグメントに格納されている値をpushする
    private fun pushSegment(segment: String, idx: Int): String {
        val seg = when (segment) {
            "local" -> "@LCL"
            "argument" -> "@ARG"
            "this" -> "@THIS"
            "that" -> "@THAT"
            else -> throw IllegalArgumentException()
        }
        return genLine("@$idx", "D=A", seg, "A=M+D", "D=M", push())
    }

    private fun pushPointerOrTemp(segment: String, index: Int): String {
        val commands = when (segment) {
            "pointer" -> genLine("@${3 + index}", "D=M")
            "temp" -> genLine("@${5 + index}", "D=M")
            else -> throw IllegalArgumentException()
        }
        return genLine(commands + push())
    }

    // stackの値をpopして各セグメントに格納する
    private fun popToSegment(segment: String, index: Int): String {
        val commands = when (segment) {
            "local" -> genLine("@LCL", "D=M+D")
            "argument" -> genLine("@ARG", "D=M+D")
            "this" -> genLine("@THIS", "D=M+D")
            "that" -> genLine("@THAT", "D=M+D")
            "pointer" -> genLine("@3", "D=A+D")
            "temp" -> genLine("@5", "D=A+D")
            else -> throw IllegalArgumentException()
        }
        return genLine("@$index", "D=A", commands, "@R13", "M=D", pop(), "@R13", "A=M", "M=D")
    }

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