@8
D=A
@SP
A=M
M=D
@SP
M=M+1
@8
D=A
@SP
A=M
M=D
@SP
M=M+1

@SP
M=M-1
@SP
A=M
D=M
M=0
@SP
M=M-1
@SP
A=M
D=M-D
@EQ0
D;JEQ
@NEQ0
0;JMP
(EQ0)
@SP
A=M
M=-1
@EQNEXT0
0;JMP
(NEQ0)
@SP
A=M
M=0
@EQNEXT0
0;JMP
(EQNEXT0)
@SP
M=M+1
