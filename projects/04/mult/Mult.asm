// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here.
// 初期化
    @R2  // 答え
    M=0
    @R3  // ループ回数
    M=0
    // ループ終了条件
    @R1
    D=M
    @R3
    D=D-M  // D=M[1]-M[R3]
    @18
    D;JLE

    // ループ
    @R0
    D=M
    @R2
    M=D+M
    @R3
    M=M+1
    @4  // ループに飛ぶ
    0; JMP
    @18   // 無限ループ
    0; JMP