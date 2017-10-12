

package riscv

import Chisel._

object Constants {
  
  val INSTRUCTION_WIDTH = 32
  val DATA_WIDTH = 64
  val ADDRESS_WIDTH = 64
  val PC_SIZE = 32
  val REGISTER_WIDTH = 64
  
  val BYTE_WIDTH = 8
  val BYTES_PER_WORD = DATA_WIDTH / BYTE_WIDTH
  
  val OPCODE_R_1 = UInt(51)  // 0110011
  val OPCODE_R_2 = UInt(59)  // 0111011
  val OPCODE_I_1 = UInt(3)   // 0000011
  val OPCODE_I_2 = UInt(15)  // 0001111
  val OPCODE_I_3 = UInt(19)  // 0010011
  val OPCODE_I_4 = UInt(27)  // 0011011
  val OPCODE_I_5 = UInt(102) // 1100111
  val OPCODE_I_6 = UInt(115) // 1110011
  val OPCODE_S = UInt(35)    // 0100011 // If funct3 = 011 then the instruction is an I-type (for the sd instruction)
  val OPCODE_SB = UInt(99)   // 1100011
  val OPCODE_U_1 = UInt(23)  // 0010111
  val OPCODE_U_2 = UInt(55)  // 0110111
  val OPCODE_UJ = UInt(111)  // 1101111
  
  val ALUCTL_AND = UInt(0)  // AND, ALUctl = 0000
  val ALUCTL_OR = UInt(1)   // OR, ALUctl = 0001
  val ALUCTL_ADD = UInt(2)  // Add, ALUctl = 0010
  val ALUCTL_SUB = UInt(6)  // Subtract, ALUctl = 0110
  val ALUCTL_SLT = UInt(7)  // Set less than, ALUctl = 0111
  val ALUCTL_NOR = UInt(12) // NOR, ALUctl = 1100
  
//  val ALUOP_LD_SD = UInt(0)      // ALUop for load/store doubleword
//  val ALUOP_BEQ = UInt(1)        // ALUop for branch if equal
//  val ALUOP_ARITHMETIC = UInt(2) // ALUop for arithmetic operation (add, sub, AND, OR)
//  
//  val FUNCT3_ALU_ADD_SUB = UInt(0) // Funct3 for add and subtract
//  val FUNCT3_ALU_AND = UInt(7)     // Funct3 for AND
//  val FUNCT3_ALU_OR = UInt(6)      // Funct3 for OR
//  
//  val FUNCT7_ALU_ADD_AND_OR = UInt(0) // Funct7 for add, AND, OR
//  val FUNCT7_ALU_SUB = UInt(32)        // Funct7 for subtract
}