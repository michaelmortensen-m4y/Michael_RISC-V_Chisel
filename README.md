
RISC-V CPU Described in Chisel 2.2 for Implementation in an FPGA

This repository contains the files accociated with the design a soft RISC-V processor optimized for speed and area for the 
Altera DE2-115 architecture. The hardware of a 5-stage pipelined single-issue 64-bit version of the RISC-V microprocessor
is described in Chisel 2.2 and supports memory-reference instructions, arithmetic-logical instructions, and conditional branch instructions. The CPU contains hardware for hazard detection to support a no-operation instruction (nop) and forwarding hardware to operations in the execution stage and instruction decode stage for resolving data hazards. Floating-point capabilities and exception handling is not supported. In addition to the full 5-stage pipelined single-issue RISC-V CPU the repository also contains a simple
single-stage version.
