package projects.`06`

import java.io.BufferedReader
import java.io.File
import java.lang.IllegalArgumentException

/**
 * アセンブリコマンドを基本要素(フィールド, シンボル)に分解するモジュール
 */
class Parser(file: File) {
    private val br: BufferedReader =
        if (file.exists()) file.bufferedReader() else throw IllegalArgumentException("File does not exist")

    private var currentCommand = ""
    private var nextCommand = ""

    /**
     * 次のコマンドが存在するかどうかを返す
     */
    fun hasMoreCommands(): Boolean {
        var line = br.readLine() ?: return false
        nextCommand = trimAllWhiteSpace(line.substringBefore("//"))
        // コメント行もしくは空行の場合、コマンドを読み込むまで、次の行を読み込み続ける
        while (nextCommand.isEmpty()) {
            line = br.readLine() ?: return false
            nextCommand = trimAllWhiteSpace(line.substringBefore("//"))
        }
        return true
    }

    /**
     * 入力から次のコマンドを読み、現在のコマンドとする。
     * hasMoreCommandsがtrueを返すときのみ呼ばれる。
     * 現在のコマンドの初期値は空。
     */
    fun advance() {
        currentCommand = trimAllWhiteSpace(nextCommand)
    }

    fun commandType(): CommandType {
        return when (currentCommand[0]) {
            '@' -> CommandType.A_COMMAND
            '(' -> CommandType.L_COMMAND
            else -> CommandType.C_COMMAND
        }
    }

    /**
     * シンボル解決なし
     * currentCommandがA_COMMAND, L_COMMANDの時のみ呼び出す
     */
    fun symbol(): String {
        val end = if (currentCommand[0] == '@') currentCommand.length else currentCommand.length - 1
        return currentCommand.substring(1 until end)
    }

    /**
     * 現在のC命令のdestニーモニックを返す
     * commandType()がC_COMMANDを返すときだけ呼ばれる
     */
    fun dest(): String {
        return currentCommand.substringBefore('=', "")
    }

    /**
     * 現在のC命令のcompニーモニックを返す
     * commandType()がC_COMMANDを返すときだけ呼ばれる
     */
    fun comp(): String {
        val compAndJump = currentCommand.substringAfter('=')
        return compAndJump.substringBefore(';')
    }

    /**
     * 現在のC命令のcompニーモニックを返す
     * commandType()がC_COMMANDを返すときだけ呼ばれる
     */
    fun jump(): String {
        return currentCommand.substringAfter(';', "")
    }

    /**
     * 空白を削除する
     */
    private fun trimAllWhiteSpace(line: String?): String {
        return line?.replace("\\s".toRegex(), "") ?: throw NullPointerException()
    }
}
