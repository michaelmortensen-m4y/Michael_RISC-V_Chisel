package riscv

import Chisel._

class Test_Control(dut: Control) extends Tester(dut) {

  // Datapath signals to be poked (for testing without datapath) (corresponds to the program defined in Datapath.scala):
  //                              |ControlOpcode|ALUzero|ALUcontrol|   // Step:
  var pokedDatapathSignals = Array(Array(19        , 0      , 0),      //  0
                                   Array(19        , 0      , 0),      //  1
                                   Array(19        , 0      , 0),      //  2
                                   Array(51        , 0      , 0),      //  3
                                   Array(35        , 0      , 3),      //  4
                                   Array(19        , 0      , 0),      //  5
                                   Array(3         , 0      , 3),      //  6
                                   Array(99        , 1      , 0),      //  7
                                   Array(19        , 0      , 0))      //  8
  
  // Expected output from control logic:
  //                                    |ALUctl|PCSrc|ALUSrc|MemtoReg|MemWrite|MemRead|RegWrite  // Step:                                 
  var expectedControlOutputs = Array(Array(2    , 0   , 1     , 0      , 0      , 0     , 1),    //  0
                                     Array(2    , 0   , 1     , 0      , 0      , 0     , 1),    //  1
                                     Array(2    , 0   , 1     , 0      , 0      , 0     , 1),    //  2
                                     Array(2    , 0   , 0     , 0      , 0      , 0     , 1),    //  3
                                     Array(2    , 0   , 1     , 0      , 1      , 0     , 0),    //  4
                                     Array(2    , 0   , 1     , 0      , 0      , 0     , 1),    //  5
                                     Array(2    , 0   , 1     , 1      , 0      , 1     , 1),    //  6
                                     Array(6    , 1   , 0     , 0      , 0      , 0     , 0),    //  7
                                     Array(2    , 0   , 1     , 0      , 0      , 0     , 1))    //  8                                 

  // Run program:
  println("")
  for (i <- 0 to 8) {
    println(" ***** STEP " + i + ": *****")
    println("\nPoking datapath signals:")
    poke(dut.io.ControlOpcode, pokedDatapathSignals(i)(0))
    poke(dut.io.ALUzero, pokedDatapathSignals(i)(1))
    poke(dut.io.ALUcontrol, pokedDatapathSignals(i)(2))
    println("\nControl output values:")
    expect(dut.io.ALUctl, expectedControlOutputs(i)(0))
    expect(dut.io.PCSrc, expectedControlOutputs(i)(1))
    expect(dut.io.ALUSrc, expectedControlOutputs(i)(2))
    expect(dut.io.MemtoReg, expectedControlOutputs(i)(3))
    expect(dut.io.MemWrite, expectedControlOutputs(i)(4))
    expect(dut.io.MemRead, expectedControlOutputs(i)(5))
    expect(dut.io.RegWrite, expectedControlOutputs(i)(6))
    println("")
    step(1)
  }
                                     
                                     
                                     
                                     
}

object Test_Control {
  def main(args: Array[String]): Unit = {
    println("Testing the Control")
    chiselMainTest(Array("--genHarness", "--test", "--backend", "c",
      "--compile", "--targetDir", "generated"),
      () => Module(new Control())) {
        f => new Test_Control(f)
      }
  }
}