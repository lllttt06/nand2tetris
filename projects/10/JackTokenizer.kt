package projects.`10`

import java.io.BufferedReader
import java.io.File

class JackTokenizer(inputFile: File) {
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
        var isMultiComment = false
        var line = nextLine()
        while (line != null) {
            var hasTokenBeforeComment = false
            if (line.isEmpty()) {
                line = nextLine()
                continue
            }
            val commentStartIdx = line.indexOf("/*")
            val commentEndIdx = line.indexOf("*/")

            // 複数行のコメントが/*で閉じられている場合
            if (commentStartIdx != -1 && commentEndIdx == -1) {
                line = line.substringBefore("/*")
                isMultiComment = true
                if (line.isNotEmpty()) hasTokenBeforeComment = true
                if (line.isEmpty()) {
                    line = nextLine()
                    continue
                }
            }

            // 複数行のコメントが*/で閉じられている場合
            if (commentStartIdx == -1 && commentEndIdx != -1) {
                line = line.substringAfter("*/")
                isMultiComment = false
            }

            // /**/を使ったコメントが1行で書かれている場合
            if (commentStartIdx != -1 && commentEndIdx != -1) {
                line = line.replaceRange(commentStartIdx, commentEndIdx + 2, "")
            }

            // 複数行のコメントで/**/を含まない場合
            if (isMultiComment && !hasTokenBeforeComment) {
                line = nextLine()
                continue
            }

            val doubleQuoteSegments = line.split("\"")

            for (index in doubleQuoteSegments.indices) {
                if (index % 2 != 0) {
                    nextTokenList.add("\"${doubleQuoteSegments[index]}\"")
                    continue
                }

                val trimmedLine = doubleQuoteSegments[index].replace("(\\s)+".toRegex(), " ")
                trimmedLine.split(" ")
                    .filter { it.isNotEmpty() }
                    .forEach { word -> splitSymbol(word, nextTokenList) }
            }

            line = nextLine()
        }
        println(nextTokenList)
    }

    /**
     * シンボルを含んだテキストをトークンに分割してnextTokenListに追加する
     */
    private fun splitSymbol(word: String, tokenList: MutableList<String>) {
        val symbolIdx = word.indexOfFirst { symbols.contains(it.toString()) }
        if (word.length == 1 || symbolIdx == -1) {
            tokenList.add(word)
            return
        }

        val token = if (symbolIdx == 0) word.first().toString() else word.substring(0, symbolIdx)
        val rest = if (symbolIdx == 0) word.substring(1) else word.substring(symbolIdx)
        tokenList.add(token)
        splitSymbol(rest, tokenList)
    }

    fun hasMoreTokens(): Boolean {
        return nextTokenList.isNotEmpty()
    }

    fun nextToken(): String {
        return nextTokenList.first()
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