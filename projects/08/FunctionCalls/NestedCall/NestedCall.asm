@256
D=A
@SP
M=D
@return-address0
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@0
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.init
0;JMP
(return-address0)
(Sys.init)
@4000
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@3
D=A+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@5000
D=A
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@3
D=A+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@return-address1
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@0
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.main
0;JMP
(return-address1)
@1
D=A
@5
D=A+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
(SysLOOP)
@SysLOOP
0;JMP
(Sys.main)
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@SP
A=M
M=D
@SP
M=M+1
@4001
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@3
D=A+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@5001
D=A
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@3
D=A+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@200
D=A
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@LCL
D=M+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@40
D=A
@SP
A=M
M=D
@SP
M=M+1
@2
D=A
@LCL
D=M+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@6
D=A
@SP
A=M
M=D
@SP
M=M+1
@3
D=A
@LCL
D=M+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@123
D=A
@SP
A=M
M=D
@SP
M=M+1
@return-address2
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@1
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Sys.add12
0;JMP
(return-address2)
@0
D=A
@5
D=A+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@0
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@2
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@3
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@4
D=A
@LCL
A=M+D
D=M
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
M=M+D
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
M=M+D
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
M=M+D
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
M=M+D
@SP
M=M+1
@LCL
D=M
@frame
M=D
@5
D=A
@frame
A=M-D
D=M
@ret
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@frame
A=M-1
D=M
@THAT
M=D
@2
D=A
@frame
A=M-D
D=M
@THIS
M=D
@3
D=A
@frame
A=M-D
D=M
@ARG
M=D
@4
D=A
@frame
A=M-D
D=M
@LCL
M=D
@ret
A=M
0;JMP
(Sys.add12)
@4002
D=A
@SP
A=M
M=D
@SP
M=M+1
@0
D=A
@3
D=A+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@5002
D=A
@SP
A=M
M=D
@SP
M=M+1
@1
D=A
@3
D=A+D
@R13
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@R13
A=M
M=D
@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1
@12
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
M=M+D
@SP
M=M+1
@LCL
D=M
@frame
M=D
@5
D=A
@frame
A=M-D
D=M
@ret
M=D
@SP
M=M-1
@SP
A=M
D=M
M=0
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@frame
A=M-1
D=M
@THAT
M=D
@2
D=A
@frame
A=M-D
D=M
@THIS
M=D
@3
D=A
@frame
A=M-D
D=M
@ARG
M=D
@4
D=A
@frame
A=M-D
D=M
@LCL
M=D
@ret
A=M
0;JMP