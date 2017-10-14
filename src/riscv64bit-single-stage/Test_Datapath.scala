package riscv

import Chisel._

class Test_Datapath(dut: Datapath) extends Tester(dut) {
  
  // Control signals to be poked (for testing without control logic):
  //                                 |ALUctl|PCSrc|ALUSrc|MemtoReg|MemWrite|MemRead|RegWrite  // Step:
  var pokedControlSignals = Array(Array(2    , 0   , 1     , 0      , 0      , 0     , 1),    //  0
                                  Array(2    , 0   , 1     , 0      , 0      , 0     , 1),    //  1
                                  Array(2    , 0   , 1     , 0      , 0      , 0     , 1),    //  2
                                  Array(2    , 0   , 0     , 0      , 0      , 0     , 1),    //  3
                                  Array(2    , 0   , 1     , 0      , 1      , 0     , 0),    //  4
                                  Array(2    , 0   , 1     , 0      , 0      , 0     , 1),    //  5
                                  Array(2    , 0   , 1     , 1      , 0      , 1     , 1),    //  6
                                  Array(6    , 1   , 0     , 0      , 0      , 0     , 0),    //  7
                                  Array(2    , 0   , 1     , 0      , 0      , 0     , 1))    //  8
  
  // Expected instructions in instruction memory:
  // (Results of an instruction at step x is seen in registers/memory at step x+1)
  //                                      |  Dec:  |    Hex:       | Inst: | Program:
  var expectedInstructionsInMemory = Array(1048723,  // = 0x100093 |   0   | addi x1, x0, 1
                                           2097427,  // = 0x200113 |   1   | addi x2, x0, 2
                                           3146131,  // = 0x300193 |   2   | addi x3, x0, 3
                                           2130483,  // = 0x208233 |   3   | add  x4, x1, x2
                                           4206627,  // = 0x403023 |   4   | sd   x4, 0(x0)
                                           531,      // = 0x213    |   5   | addi x4, x0, x0
                                           12803,    // = 0x3203   |   6   | ld   x4, 0(x0)
                                           3277411,  // = 0x320263 |   7   | beq  x4, x3, 2
                                           10487059, // = 0xA00513 |   8   | addi x10, x0, 10
                                           11535763) // = 0xB00593 |   9   | addi x11, x0, 11
                                  
  // Expected fetched instructions doing execution:                 (Inst:) // Step:
  var expectedFetchedInstructions = Array(expectedInstructionsInMemory(0),  //   0
                                          expectedInstructionsInMemory(1),  //   1
                                          expectedInstructionsInMemory(2),  //   2
                                          expectedInstructionsInMemory(3),  //   3
                                          expectedInstructionsInMemory(4),  //   4
                                          expectedInstructionsInMemory(5),  //   5
                                          expectedInstructionsInMemory(6),  //   6
                                          expectedInstructionsInMemory(7),  //   7 // (It will branch from inst. 7 to 9)
                                          expectedInstructionsInMemory(9))  //   8                                      
                                          
  // Expected values in register file:
  //                                   |x0|x1|x2|x3|x4|x10|x11  // Step:
  var expectedRegisterFile = Array(Array(0, 0, 0, 0, 0, 0, 0),  //  0
                                   Array(0, 1, 0, 0, 0, 0, 0),  //  1
                                   Array(0, 1, 2, 0, 0, 0, 0),  //  2
                                   Array(0, 1, 2, 3, 0, 0, 0),  //  3
                                   Array(0, 1, 2, 3, 3, 0, 0),  //  4
                                   Array(0, 1, 2, 3, 3, 0, 0),  //  5
                                   Array(0, 1, 2, 3, 0, 0, 0),  //  6
                                   Array(0, 1, 2, 3, 3, 0, 0),  //  7
                                   Array(0, 1, 2, 3, 3, 0, 0),  //  8
                                   Array(0, 1, 2, 3, 3, 0, 11)) //  9
                                   
  // Expected values in data memory:
  //                                  |0 |1 |2 |  // Step:
  var expectedDataMemory = Array(Array(0, 0, 0),  //  0
                                 Array(0, 0, 0),  //  1
                                 Array(0, 0, 0),  //  2
                                 Array(0, 0, 0),  //  3
                                 Array(0, 0, 0),  //  4
                                 Array(3, 0, 0),  //  5
                                 Array(3, 0, 0),  //  6
                                 Array(3, 0, 0),  //  7
                                 Array(3, 0, 0),  //  8
                                 Array(3, 0, 0))  //  9
  
  // Check instruction memory:                              
  println("\nInstruction memory values:")
  expect(dut.io.instruction0Out, expectedInstructionsInMemory(0))
  expect(dut.io.instruction1Out, expectedInstructionsInMemory(1))
  expect(dut.io.instruction2Out, expectedInstructionsInMemory(2))
  expect(dut.io.instruction3Out, expectedInstructionsInMemory(3))
  expect(dut.io.instruction4Out, expectedInstructionsInMemory(4))
  expect(dut.io.instruction5Out, expectedInstructionsInMemory(5))
  expect(dut.io.instruction6Out, expectedInstructionsInMemory(6))
  expect(dut.io.instruction7Out, expectedInstructionsInMemory(7))
  expect(dut.io.instruction8Out, expectedInstructionsInMemory(8))
  expect(dut.io.instruction9Out, expectedInstructionsInMemory(9))
  
  // Run program:
  println("")
  for (i <- 0 to 8) {
    println(" ***** STEP " + i + ": *****")
    println("\nPoking control signals:")
    poke(dut.io.ALUctl, pokedControlSignals(i)(0))
    poke(dut.io.PCSrc, pokedControlSignals(i)(1))
    poke(dut.io.ALUSrc, pokedControlSignals(i)(2))
    poke(dut.io.MemtoReg, pokedControlSignals(i)(3))
    poke(dut.io.MemWrite, pokedControlSignals(i)(4))
    poke(dut.io.MemRead, pokedControlSignals(i)(5))
    poke(dut.io.RegWrite, pokedControlSignals(i)(6))
    println("\nRegisterfile values:")
    expect(dut.io.register0Out, expectedRegisterFile(i)(0))
    expect(dut.io.register1Out, expectedRegisterFile(i)(1))
    expect(dut.io.register2Out, expectedRegisterFile(i)(2))
    expect(dut.io.register3Out, expectedRegisterFile(i)(3))
    expect(dut.io.register4Out, expectedRegisterFile(i)(4))
    expect(dut.io.register10Out, expectedRegisterFile(i)(5))
    expect(dut.io.register11Out, expectedRegisterFile(i)(6))
    println("\nData memory values:")
    expect(dut.io.dataMemory0Out, expectedDataMemory(i)(0))
    expect(dut.io.dataMemory1Out, expectedDataMemory(i)(1))
    expect(dut.io.dataMemory2Out, expectedDataMemory(i)(2))
    println("\nFetched instruction:")
    expect(dut.io.instructionOut, expectedFetchedInstructions(i))
    println("")
    step(1)
  }
  
  // Check results of last instruction:
  println("\nFinal Registerfile values:")
  expect(dut.io.register0Out, expectedRegisterFile(9)(0))
  expect(dut.io.register1Out, expectedRegisterFile(9)(1))
  expect(dut.io.register2Out, expectedRegisterFile(9)(2))
  expect(dut.io.register3Out, expectedRegisterFile(9)(3))
  expect(dut.io.register4Out, expectedRegisterFile(9)(4))
  expect(dut.io.register10Out, expectedRegisterFile(9)(5))
  expect(dut.io.register11Out, expectedRegisterFile(9)(6))
  println("\nFinal Data memory values:")
  expect(dut.io.dataMemory0Out, expectedDataMemory(9)(0))
  expect(dut.io.dataMemory1Out, expectedDataMemory(9)(1))
  expect(dut.io.dataMemory2Out, expectedDataMemory(9)(2))
  println("")
  
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