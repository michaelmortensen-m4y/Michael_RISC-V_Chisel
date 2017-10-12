package riscv

import Chisel._

class Test_Datapath(dut: Datapath) extends Tester(dut) {
  poke(dut.io.ALUctl, 2)
  poke(dut.io.PCSrc, 0)
  poke(dut.io.ALUSrc, 1)
  poke(dut.io.MemtoReg, 0)
  poke(dut.io.MemWrite, 0)
  poke(dut.io.MemRead, 0)
  poke(dut.io.RegWrite, 1)
  
  println("\nInstruction memory values:")
  expect(dut.io.instruction1Out, 1048723)   // addi x1, x0, 1
  expect(dut.io.instruction2Out, 2097427)   // addi x2, x0, 2
  expect(dut.io.instruction3Out, 3146131)   // addi x3, x0, 3
  expect(dut.io.instruction4Out, 2130483)   // add  x4, x1, x2
  expect(dut.io.instruction5Out, 4206627)   // sd   x4, 0(x0)
  expect(dut.io.instruction6Out, 531)       // addi x4, x0, x0
  expect(dut.io.instruction7Out, 12803)     // ld   x4, 0(x0)
  expect(dut.io.instruction8Out, 3278307)   // beq  x4, x3, 11
  expect(dut.io.instruction9Out, 10487059)  // addi x10, x0, 10
  expect(dut.io.instruction10Out, 11535763) // addi x11, x0, 11
  
  println("")
  step(1) // Step 1
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 1048723) // addi x1, x0, 1
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 0)
  expect(dut.io.register2Out, 0)
  expect(dut.io.register3Out, 0)
  expect(dut.io.register4Out, 0)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 0)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 2
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 2097427) // addi x2, x0, 2
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 0)
  expect(dut.io.register3Out, 0)
  expect(dut.io.register4Out, 0)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 0)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 3
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 3146131) // addi x3, x0, 3
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 2)
  expect(dut.io.register3Out, 0)
  expect(dut.io.register4Out, 0)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 0)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 4
  
  poke(dut.io.ALUctl, 2)
  poke(dut.io.PCSrc, 0)
  poke(dut.io.ALUSrc, 0)
  poke(dut.io.MemtoReg, 0)
  poke(dut.io.MemWrite, 0)
  poke(dut.io.MemRead, 0)
  poke(dut.io.RegWrite, 1)
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 2130483) // add  x4, x1, x2
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 2)
  expect(dut.io.register3Out, 3)
  expect(dut.io.register4Out, 0)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 0)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 5
  
  poke(dut.io.ALUctl, 2)
  poke(dut.io.PCSrc, 0)
  poke(dut.io.ALUSrc, 1)
  poke(dut.io.MemtoReg, 0)
  poke(dut.io.MemWrite, 1)
  poke(dut.io.MemRead, 0)
  poke(dut.io.RegWrite, 0)
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 4206627) // sd   x4, 0(x0)
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 2)
  expect(dut.io.register3Out, 3)
  expect(dut.io.register4Out, 3)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 0)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 6
  
  poke(dut.io.ALUctl, 2)
  poke(dut.io.PCSrc, 0)
  poke(dut.io.ALUSrc, 1)
  poke(dut.io.MemtoReg, 0)
  poke(dut.io.MemWrite, 0)
  poke(dut.io.MemRead, 0)
  poke(dut.io.RegWrite, 1)
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 531) // addi x4, x0, x0
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 2)
  expect(dut.io.register3Out, 3)
  expect(dut.io.register4Out, 3)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 3)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 7
  
  poke(dut.io.ALUctl, 2)
  poke(dut.io.PCSrc, 0)
  poke(dut.io.ALUSrc, 1)
  poke(dut.io.MemtoReg, 1)
  poke(dut.io.MemWrite, 0)
  poke(dut.io.MemRead, 1)
  poke(dut.io.RegWrite, 1)
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 12803) // ld   x4, 0(x0)
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 2)
  expect(dut.io.register3Out, 3)
  expect(dut.io.register4Out, 0)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 3)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 8
  
  poke(dut.io.ALUctl, 2)
  poke(dut.io.PCSrc, 0)
  poke(dut.io.ALUSrc, 0)
  poke(dut.io.MemtoReg, 0)
  poke(dut.io.MemWrite, 0)
  poke(dut.io.MemRead, 0)
  poke(dut.io.RegWrite, 0)
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 3278307) // beq  x4, x3, 11
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 2)
  expect(dut.io.register3Out, 3)
  expect(dut.io.register4Out, 3)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 3)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 9
  
  poke(dut.io.ALUctl, 6) // Subtract
  poke(dut.io.PCSrc, 0)
  poke(dut.io.ALUSrc, 0)
  poke(dut.io.MemtoReg, 0)
  poke(dut.io.MemWrite, 0)
  poke(dut.io.MemRead, 0)
  poke(dut.io.RegWrite, 0)
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 3278307) // beq  x4, x3, 11
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 2)
  expect(dut.io.register3Out, 3)
  expect(dut.io.register4Out, 3)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 3)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 10
  
  poke(dut.io.ALUctl, 2) // Add
  poke(dut.io.PCSrc, 1)
  poke(dut.io.ALUSrc, 1)
  poke(dut.io.MemtoReg, 0)
  poke(dut.io.MemWrite, 0)
  poke(dut.io.MemRead, 0)
  poke(dut.io.RegWrite, 1)
  
  println("\nFetched instruction:")
  expect(dut.io.instructionOut, 11535763) // addi x11, x0, 11
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 2)
  expect(dut.io.register3Out, 3)
  expect(dut.io.register4Out, 3)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 0)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 3)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
  
  println("")
  step(1) // Step 11
  
  poke(dut.io.ALUctl, 2) // Add
  poke(dut.io.PCSrc, 0)
  poke(dut.io.ALUSrc, 0)
  poke(dut.io.MemtoReg, 0)
  poke(dut.io.MemWrite, 0)
  poke(dut.io.MemRead, 0)
  poke(dut.io.RegWrite, 0)
  
  println("\nRegisterfile values:")
  expect(dut.io.register0Out, 0)
  expect(dut.io.register1Out, 1)
  expect(dut.io.register2Out, 2)
  expect(dut.io.register3Out, 3)
  expect(dut.io.register4Out, 3)
  expect(dut.io.register10Out, 0)
  expect(dut.io.register11Out, 11)
  
  println("\nData memory values:")
  expect(dut.io.dataMemory0Out, 3)
  expect(dut.io.dataMemory1Out, 0)
  expect(dut.io.dataMemory2Out, 0)
}

object Test_Datapath {
  def main(args: Array[String]): Unit = {
    println("Testing the Datapath")
    chiselMainTest(Array("--genHarness", "--test", "--backend", "c",
      "--compile", "--targetDir", "generated"),
      () => Module(new Datapath())) {
        f => new Test_Datapath(f)
      }
  }
}