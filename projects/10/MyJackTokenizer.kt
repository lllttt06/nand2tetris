package projects.`10`

import java.io.BufferedReader
import java.io.File

class MyJackTokenizer(inputFile: File) {
    private val bufferedReader: BufferedReader = inputFile.bufferedReader()

    private val nextTokenList: MutableList<String> = mutableListOf()

    var currentToken: String = ""

    private val keywords: Set<String> = setOf(
        "class",
        "constructor",
        "function",
        "method",
        "field",
        "static",
        "var",
        "int",
        "char",
        "boolean",
        "void",
        "true",
        "false",
        "null",
        "this",
        "let",
        "do",
        "if",
        "else",
        "while",
        "return"
    )

    private val symbols: Set<String> = setOf(
        "{",
        "}",
        "(",
        ")",
        "[",
        "]",
        ".",
        ",",
        ";",
        "+",
        "-",
        "*",
        "/",
        "&",
        "|",
        "<",
        ">",
        "=",
        "~"
    )

    /**
     * 入力.jackファイルを読み込み、トークンの一覧を生成してnextTokenListに追加する
     */
    init {
        // 複数行のコメントが存在するか
        var isMultiComment = false
        var line = nextLine()

        while (line != null) {
            var hasTokenBeforeComment = false

            // lineがからの場合は次の行を読み込む
            if (line.isEmpty()) {
                line = nextLine()
                continue
            }

            // 「/*　~~~~~ */」の形式のコメント文の開始と終了位置を取得
            val commentStartIndex = line.indexOf("/*")
            val commentEndIndex = line.indexOf("*/")

            // 複数行のコメントが/*で閉じられている場合
            if (commentStartIndex != -1 && commentEndIndex == -1) {
                line = line.substringBefore("/*")
                isMultiComment = true
                if (line.isEmpty()) {
                    line = nextLine()
                    continue
                } else hasTokenBeforeComment = true
            }

            // 複数行のコメントが*/で閉じられている場合
            if (commentStartIndex == -1 && commentEndIndex != -1) {
                line = line.substringAfter("*/")
                isMultiComment = false
            }

            // /**/を使ったコメントが1行で書かれている場合
            if (commentStartIndex != -1 && commentEndIndex != -1) {
                line = line.replaceRange(commentStartIndex, commentEndIndex + 2, "")
            }

            // 複数行のコメントで/**/を持たない場合
            if (isMultiComment && hasTokenBeforeComment.not()) {
                line = nextLine()
                continue
            }

            // 「"」で文を区切る
            val doubleQuoteSegments = line.split("\"")

            for (index in doubleQuoteSegments.indices) {
                // 「"」で囲まれたセグメント(String)
                if (index % 2 != 0) {
                    // nextTokenListにStringを追加する
                    nextTokenList.add("\"${doubleQuoteSegments[index]}\"")
                    continue
                }

                // 一つ以上の空白文字列を一つの空白文字で置換する
                val trimmedLine = doubleQuoteSegments[index].replace("(\\s)+".toRegex(), " ")
                trimmedLine.split(" ").filter { it.isNotEmpty() }.forEach { word ->
                    splitSymbol(word, nextTokenList)
                }
            }
            line = nextLine()
        }
        println(nextTokenList)
    }

    /**
     * シンボルを含んだテキストをトークンに分割してnextTokenListに追加する
     */
    private fun splitSymbol(word: String, tokenList: MutableList<String>) {
        // 最初にシンボルが現れるインデックスを取得
        val symbolIdx = word.indexOfFirst { symbols.contains(it.toString()) }

        // wordの長さがが1(シンボルのみ)もしくはシンボルを含まない場合はすぐにtokenListに追加して終了
        if (word.length == 1 || symbolIdx == -1) {
            tokenList.add(word)
            return
        }

        /**
         * wordをシンボルを含まないtokenと残りに分ける
         * word = main() -> token = main, rest = ()
         * restを再帰的にsplitSymbolに入れる
         **/
        val token = if (symbolIdx == 0) word.first().toString() else word.substring(0, symbolIdx)
        val rest = if (symbolIdx == 0) word.substring(1) else word.substring(symbolIdx)
        tokenList.add(token)
        splitSymbol(rest, tokenList)
    }

    fun hasMoreTokens(): Boolean {
        return nextTokenList.isNotEmpty()
    }

    /**
     * 入力から次のトークンを取得し、それを現在のトークンとする。
     * このルーチンは、hasMoreTokens()がtrueの場合のみ呼び出すことができる。
     */
    fun advance() {
        currentToken = nextTokenList.first()
        nextTokenList.removeAt(0)
    }

    fun tokenType(): TokenType {
        return when {
            keywords.contains(currentToken) -> TokenType.KEYWORD
            symbols.contains(currentToken) -> TokenType.SYMBOL
            isIdentifier(currentToken) -> TokenType.IDENTIFIER
            isInteger(currentToken) -> TokenType.INT_CONST
            currentToken.startsWith("\"") && currentToken.endsWith("\"") -> TokenType.STRING_CONST
            else -> throw NoSuchElementException()
        }
    }

    fun keyword(): String {
        return currentToken
    }

    fun symbol(): Char {
        return currentToken.first()
    }

    fun identifier(): String {
        return currentToken
    }

    fun intVal(): Int {
        return currentToken.toInt()
    }

    fun stringVal(): String {
        return currentToken.trim('"')
    }

    // 「//」でコメントアウトされる前までの文を取得する
    private fun nextLine(): String? {
        return bufferedReader.readLine()?.trim()?.substringBefore("//")
    }

    private fun isInteger(token: String): Boolean {
        val pattern = Regex("^0$|^[1-9][0-9]*$")
        return pattern.containsMatchIn(token)
    }

    private fun isIdentifier(token: String): Boolean {
        val pattern = Regex("^[^0-9]+.*")
        return pattern.containsMatchIn(token)
    }
}