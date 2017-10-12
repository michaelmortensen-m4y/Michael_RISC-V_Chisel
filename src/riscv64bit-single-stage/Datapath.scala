
package riscv

import Chisel._
import Constants._

class Datapath extends Module {
  // Inputs are from Controlpath
  val io = new Bundle {
    val ALUctl = UInt(INPUT, 4) // Input from ALU control
    val PCSrc = UInt(INPUT, 1)
    val ALUSrc = UInt(INPUT, 1)
    val MemtoReg = UInt(INPUT, 1)
    val MemWrite = UInt(INPUT, 1)
    val MemRead = UInt(INPUT, 1)
    val RegWrite = UInt(INPUT, 1)
    
    val ControlOpcode = UInt(OUTPUT, 7) // Output to main control
    val ALUzero = UInt(OUTPUT, 1) // Output to branch AND gate
    val ALUcontrol = UInt(OUTPUT, 4) // Output to ALU control
    
    // Outputs for testing:
    val instructionOut = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction1Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction2Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction3Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction4Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction5Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction6Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction7Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction8Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction9Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val instruction10Out = UInt(OUTPUT, INSTRUCTION_WIDTH)
    val register0Out = UInt(OUTPUT, DATA_WIDTH)
    val register1Out = UInt(OUTPUT, DATA_WIDTH)
    val register2Out = UInt(OUTPUT, DATA_WIDTH)
    val register3Out = UInt(OUTPUT, DATA_WIDTH)
    val register4Out = UInt(OUTPUT, DATA_WIDTH)
    val register10Out = UInt(OUTPUT, DATA_WIDTH)
    val register11Out = UInt(OUTPUT, DATA_WIDTH)
    val dataMemory0Out = UInt(OUTPUT, DATA_WIDTH)
    val dataMemory1Out = UInt(OUTPUT, DATA_WIDTH)
    val dataMemory2Out = UInt(OUTPUT, DATA_WIDTH)
  }
  
  // INSTRUCTION FETCH:  
  
  // PCmux output wire
  val pc_next = Wire(UInt(width = INSTRUCTION_WIDTH))
  // PCmux input wires
  val pc_incremented = Wire(UInt(width = INSTRUCTION_WIDTH))
  val pc_BranchAddrCalc = Wire(UInt(width = INSTRUCTION_WIDTH))
  pc_BranchAddrCalc := UInt(0) // Default value
  // PCmux
  when (io.PCSrc === UInt(1)) {
    pc_next := pc_BranchAddrCalc
  } .otherwise {
    pc_next := pc_incremented
  } 
  // PC Register
  val pc_register = Reg(init = UInt(0, PC_SIZE))
  pc_register := pc_next
  // PC Adder
  pc_incremented := pc_register + UInt(1)
  // Instruction Memory
  val instructionMemory = Vec(Array(UInt(51, INSTRUCTION_WIDTH), UInt(51, INSTRUCTION_WIDTH), // add x0, x0, x0  Two nop instructions needed here for it to work?
                                         //  |  immediate  | rs1 |fu3| rd  |opcode | 
      UInt(1048723 , INSTRUCTION_WIDTH), // = 0000000 00001 00000 000 00001 0010011 = addi x1, x0, 1
                                         //  |  immediate  | rs1 |fu3| rd  |opcode | 
      UInt(2097427 , INSTRUCTION_WIDTH), // = 0000000 00010 00000 000 00010 0010011 = addi x2, x0, 2
                                         //  |  immediate  | rs1 |fu3| rd  |opcode | 
      UInt(3146131 , INSTRUCTION_WIDTH), // = 0000000 00011 00000 000 00011 0010011 = addi x3, x0, 3
                                         //  |  fu7  | rs2 | rs1 |fu3| rd  |opcode | 
      UInt(2130483 , INSTRUCTION_WIDTH), // = 0000000 00010 00001 000 00100 0110011 = add  x4, x1, x2
                                         //  |  imm  | rs2 | rs1 |fu3| imm |opcode |
      UInt(4206627 , INSTRUCTION_WIDTH), // = 0000000 00100 00000 011 00000 0100011 = sd   x4, 0(x0)
                                         //  |  immediate  | rs1 |fu3| rd  |opcode |
      UInt(531     , INSTRUCTION_WIDTH), // = 0000000 00000 00000 000 00100 0010011 = addi x4, x0, 0
                                         //  |  immediate  | rs1 |fu3| rd  |opcode |
      UInt(12803   , INSTRUCTION_WIDTH), // = 0000000 00000 00000 011 00100 0000011 = ld   x4, 0(x0)
                                         //  |  imm  | rs2 | rs1 |fu3| imm |opcode |
      UInt(3278307 , INSTRUCTION_WIDTH), // = 0000000 00011 00100 000 01011 1100011 = beq  x4, x3, 11
                                         //  |  immediate  | rs1 |fu3| rd  |opcode |
      UInt(10487059, INSTRUCTION_WIDTH), // = 0000000 01010 00000 000 01010 0010011 = addi x10, x0, 10
                                         //  |  immediate  | rs1 |fu3| rd  |opcode |
      UInt(11535763, INSTRUCTION_WIDTH)  // = 0000000 01011 00000 000 01011 0010011 = addi x11, x0, 11
      ))
  val instruction = instructionMemory(pc_next)
  
  
  // INSTRUCTION DECODE:
  
  val instructionOpcode = instruction(6, 0)
  val rs1Address = instruction(19, 15)
  val rs2Address = instruction(24, 20)
  val rdAddress = instruction(11, 7)
  val funct3 = instruction(14, 12)
  io.ControlOpcode := instructionOpcode
  val writeBackData = Wire(UInt(width = DATA_WIDTH))
  writeBackData := UInt(0) // Default value
  // Immediate generation
  val immediate = Wire(UInt(width = DATA_WIDTH))
  when(instructionOpcode === OPCODE_S) { // S-type
    immediate := Cat(instruction(31, 25), instruction(11, 7))
  } .elsewhen(instructionOpcode === OPCODE_SB) { // SB-type
    immediate := Cat(instruction(31, 25), instruction(11, 7))
    pc_BranchAddrCalc := Cat(instruction(31, 25), instruction(11, 7))
  } .elsewhen(instructionOpcode === OPCODE_U_1 || instructionOpcode === OPCODE_U_2) { // U-type
    immediate := instruction(31, 12)
  } .elsewhen(instructionOpcode === OPCODE_UJ) { // UJ-type
    immediate := instruction(31, 12)
  } .otherwise { // I-type (or R-type)
    immediate := instruction(31, 20)
  } 
  // Register file
  val registerFile = Mem(32, UInt(width = REGISTER_WIDTH))
  
  
  // EXECUTION:
  
  // Input to ALU control (instruction[30, 14-12])
  io.ALUcontrol := Cat(instruction(30), instruction(14, 12))
  // ALU output wires
  val alu_result = Wire(UInt(width = DATA_WIDTH))
  alu_result := UInt(0) // Default value
  io.ALUzero := UInt(0)
  // ALU input wires
  val rs1Data = registerFile(rs1Address)
  val rs2Data = registerFile(rs2Address)
  val aluOperand2 = Wire(UInt(width = DATA_WIDTH))
  // ALUmux
  when (io.ALUSrc === UInt(1)) {
    aluOperand2 := immediate
  } .otherwise {
    aluOperand2 := rs2Data
  }
  // ALU
  switch(io.ALUctl) {
    is(ALUCTL_AND) { alu_result := rs1Data & aluOperand2 } // AND, ALUctl = 0000
    is(ALUCTL_OR) { alu_result := rs1Data | aluOperand2 } // OR, ALUctl = 0001
    is(ALUCTL_ADD) { alu_result := rs1Data + aluOperand2 } // Add, ALUctl = 0010
    is(ALUCTL_SUB) { // Subtract, ALUctl = 0110
      alu_result := rs1Data - aluOperand2
      when (alu_result === UInt(0)) {
        io.ALUzero := UInt(1)
      } .otherwise {
        io.ALUzero := UInt(0)
      }
    }
    is(ALUCTL_SLT) { alu_result := rs1Data < aluOperand2 } // Set less than, ALUctl = 0111
    is(ALUCTL_NOR) { alu_result := ~(rs1Data | aluOperand2) } // NOR, ALUctl = 1100
  }
  
  
  // MEMORY:
  
  val memoryDataRead = Wire(UInt(width = DATA_WIDTH))
  memoryDataRead := UInt(0) // Default value
  // Data memory
  val dataMemory = Mem(32, UInt(width = DATA_WIDTH))
  when (io.MemWrite === UInt(1)) {
    dataMemory(alu_result) := rs2Data
  } .elsewhen (io.MemRead === UInt(1)) {
    memoryDataRead := dataMemory(alu_result)
  }
  
  // WRITE BACK:
  
  // Write back mux
  when (io.MemtoReg === UInt(1)) {
    writeBackData := memoryDataRead
  } .otherwise {
    writeBackData := alu_result
  } 
  when (io.RegWrite === UInt(1)) {
    registerFile(rdAddress) := writeBackData
  }
  
  
  
  // Outputs for testing:
  
  io.instructionOut := instruction // Fetched instruction
  // Instructions in instruction memory
  io.instruction1Out := instructionMemory(UInt(2))
  io.instruction2Out := instructionMemory(UInt(3))
  io.instruction3Out := instructionMemory(UInt(4))
  io.instruction4Out := instructionMemory(UInt(5))
  io.instruction5Out := instructionMemory(UInt(6))
  io.instruction6Out := instructionMemory(UInt(7))
  io.instruction7Out := instructionMemory(UInt(8))
  io.instruction8Out := instructionMemory(UInt(9))
  io.instruction9Out := instructionMemory(UInt(10))
  io.instruction10Out := instructionMemory(UInt(11))
  // Register file values
  io.register0Out := registerFile(UInt(0))
  io.register1Out := registerFile(UInt(1))
  io.register2Out := registerFile(UInt(2))
  io.register3Out := registerFile(UInt(3))
  io.register4Out := registerFile(UInt(4))
  io.register10Out := registerFile(UInt(10))
  io.register11Out := registerFile(UInt(11))
  // Data memory values:
  io.dataMemory0Out := dataMemory(UInt(0))
  io.dataMemory1Out := dataMemory(UInt(1))
  io.dataMemory2Out := dataMemory(UInt(2))
  
}


// Generate the Verilog code by invoking chiselMain() in our main()
object Main {
  def main(args: Array[String]): Unit = {
    println("Generating the hardware")
    chiselMain(Array("--backend", "v", "--targetDir", "generated"),
      () => Module(new Datapath()))
  }
}