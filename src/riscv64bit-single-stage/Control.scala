
package riscv

import Chisel._
import Constants._

class Control extends Module {
  // Inputs are from Datapath
  val io = new Bundle {
    val ControlOpcode = UInt(INPUT, 7) // Input to main control
    val ALUzero = UInt(INPUT, 1) // Input to branch AND gate
    val ALUcontrol = UInt(INPUT, 4) // Input to ALU control
    
    val ALUctl = UInt(OUTPUT, 4) // Output to ALU
    val PCSrc = UInt(OUTPUT, 1)
    val ALUSrc = UInt(OUTPUT, 1)
    val MemtoReg = UInt(OUTPUT, 1)
    val MemWrite = UInt(OUTPUT, 1)
    val MemRead = UInt(OUTPUT, 1)
    val RegWrite = UInt(OUTPUT, 1)
  }
  
  // Default values:
  io.ALUctl := UInt(0)
  io.PCSrc := UInt(0)
  io.ALUSrc := UInt(0)
  io.MemtoReg := UInt(0)
  io.MemWrite := UInt(0)
  io.MemRead := UInt(0)
  io.RegWrite := UInt(0)
  
  // Signals:
  val funct3 = io.ALUcontrol(2, 0)
  val branch = Wire(UInt(width = 1))
  branch := UInt(0) // Default value
  
  // Branch AND gate:
  io.PCSrc := (branch & io.ALUzero)
  
  // Control logic:
  when(io.ControlOpcode === OPCODE_S) { // S-type
    io.ALUctl := ALUCTL_ADD
    branch := UInt(0)
    io.ALUSrc := UInt(1)
    io.MemtoReg := UInt(0)
    io.MemWrite := UInt(1)
    io.MemRead := UInt(0)
    io.RegWrite := UInt(0)
  } .elsewhen(io.ControlOpcode === OPCODE_SB) { // SB-type
    io.ALUctl := ALUCTL_SUB
    branch := UInt(1)
    io.ALUSrc := UInt(0)
    io.MemtoReg := UInt(0)
    io.MemWrite := UInt(0)
    io.MemRead := UInt(0)
    io.RegWrite := UInt(0)
  } .elsewhen(io.ControlOpcode === OPCODE_U_1 || io.ControlOpcode === OPCODE_U_2) { // U-type
    io.ALUctl := ALUCTL_ADD
    branch := UInt(0)
    io.ALUSrc := UInt(1)
    io.MemtoReg := UInt(1)
    io.MemWrite := UInt(0)
    io.MemRead := UInt(1)
    io.RegWrite := UInt(1)
  } .elsewhen(io.ControlOpcode === OPCODE_UJ) { // UJ-type
    io.ALUctl := ALUCTL_ADD
    branch := UInt(0)
    io.ALUSrc := UInt(0)
    io.MemtoReg := UInt(0)
    io.MemWrite := UInt(0)
    io.MemRead := UInt(0)
    io.RegWrite := UInt(0)
  } .elsewhen(io.ControlOpcode === OPCODE_R_1 || io.ControlOpcode === OPCODE_R_2) { // R-type    
    when(io.ALUcontrol === UInt(8)) { // = funct7(5)funct3(2, 0) = 1000
      io.ALUctl := ALUCTL_SUB
    } .elsewhen(io.ALUcontrol === UInt(7)) { // = funct7(5)funct3(2, 0) = 0111
      io.ALUctl := ALUCTL_AND
    } .elsewhen(io.ALUcontrol === UInt(6)) { // = funct7(5)funct3(2, 0) = 0110
      io.ALUctl := ALUCTL_OR
    } .elsewhen(io.ALUcontrol === UInt(4)) { // = funct7(5)funct3(2, 0) = 0100
      io.ALUctl := ALUCTL_XOR
    } .elsewhen(io.ALUcontrol === UInt(1)) { // = funct7(5)funct3(2, 0) = 0001
      io.ALUctl := ALUCTL_SLL
    } .elsewhen(io.ALUcontrol === UInt(2)) { // = funct7(5)funct3(2, 0) = 0010
      io.ALUctl := ALUCTL_SLT
    } .elsewhen(io.ALUcontrol === UInt(3)) { // = funct7(5)funct3(2, 0) = 0011
      io.ALUctl := ALUCTL_SLTU
    } .elsewhen(io.ALUcontrol === UInt(5)) { // = funct7(5)funct3(2, 0) = 0101
      io.ALUctl := ALUCTL_SRL
    } .elsewhen(io.ALUcontrol === UInt(13)) { // = funct7(5)funct3(2, 0) = 1101
      io.ALUctl := ALUCTL_SRA
    } .otherwise { // io.ALUcontrol === UInt(0)
      io.ALUctl := ALUCTL_ADD
    }
    branch := UInt(0)
    io.ALUSrc := UInt(0)
    io.MemtoReg := UInt(0)
    io.MemWrite := UInt(0)
    io.MemRead := UInt(0)
    io.RegWrite := UInt(1)
  } .elsewhen(io.ControlOpcode === UInt(3)) { // I-type load
    io.ALUctl := ALUCTL_ADD
    branch := UInt(0)
    io.ALUSrc := UInt(1)
    io.MemtoReg := UInt(1)
    io.MemWrite := UInt(0)
    io.MemRead := UInt(1)
    io.RegWrite := UInt(1)
  } .otherwise { // I-type
    when(funct3 === UInt(1)) {
      io.ALUctl := ALUCTL_SLL
    } .elsewhen(funct3 === UInt(2)) {
      io.ALUctl := ALUCTL_SLT
    } .elsewhen(funct3 === UInt(3)) {
      io.ALUctl := ALUCTL_SLTU
    } .elsewhen(funct3 === UInt(4)) {
      io.ALUctl := ALUCTL_XOR
    } .elsewhen(io.ALUcontrol === UInt(5)) { // = funct7(5)funct3(2, 0) = 0101
      io.ALUctl := ALUCTL_SRL
    } .elsewhen(io.ALUcontrol === UInt(13)) { // = funct7(5)funct3(2, 0) = 1101
      io.ALUctl := ALUCTL_SRA
    } .elsewhen(funct3 === UInt(6)) {
      io.ALUctl := ALUCTL_OR
    } .elsewhen(funct3 === UInt(7)) {
      io.ALUctl := ALUCTL_AND
    } .otherwise { // funct3 === UInt(0)
      io.ALUctl := ALUCTL_ADD
    } 
    branch := UInt(0)
    io.ALUSrc := UInt(1)
    io.MemtoReg := UInt(0)
    io.MemWrite := UInt(0)
    io.MemRead := UInt(0)
    io.RegWrite := UInt(1)
  } 
  
}

// Generate the Verilog code by invoking chiselMain() in our main()
//object Main {
//  def main(args: Array[String]): Unit = {
//    println("Generating the hardware")
//    chiselMain(Array("--backend", "v", "--targetDir", "generated"),
//      () => Module(new Control()))
//  }
//}
