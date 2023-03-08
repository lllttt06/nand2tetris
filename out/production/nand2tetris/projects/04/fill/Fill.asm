// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

    @8191
    D=A
    @number_of_pixels
    M=D

(INF_LOOP)
    // ループの回数(0~number_of_pixelまでループする)
    @loop_count  
    M=0
    // keyboardの入力を監視する
    @KBD
    D=M
    @BLACK_SCREEN
    D; JGT

// スクリーンを黒くする
(BLACK_SCREEN)
    @loop_count
    D=M
    @SCREEN
    A=A+D
    M=-1
    @loop_count
    MD=M+1
    @number_of_pixels
    D=D-M
    @BLACK_SCREEN
    D; JLT
    @INF_LOOP
    D; JGE

// スクリーンを白くする
(WHITE_SCREEN)
    @loop_count
    D=M
    @SCREEN
    A=A+D
    M=0
    @loop_count
    MD=M+1
    @number_of_pixels
    D=D-M
    @WHITE_SCREEN
    D; JLT

// INF_LOOP
    @INF_LOOP
    0; JMP
