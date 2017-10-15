
package riscv

import Chisel._
import Constants._

class Top extends Module {
  
  val io = new Bundle {
    
  }
  val control = Module(new Control())
  val datapath = Module(new Datapath())
  
  // Control input to datapath output:
  control.io.ControlOpcode <> datapath.io.ControlOpcode
  control.io.ALUzero <> datapath.io.ALUzero
  control.io.ALUcontrol <> datapath.io.ALUcontrol
  
  // Control output to datapath input:
  control.io.ALUctl <> datapath.io.ALUctl
  control.io.PCSrc <> datapath.io.PCSrc
  control.io.ALUSrc <> datapath.io.ALUSrc
  control.io.MemtoReg <> datapath.io.MemtoReg
  control.io.MemWrite <> datapath.io.MemWrite
  control.io.MemRead <> datapath.io.MemRead
  control.io.RegWrite <> datapath.io.RegWrite
  
}


// Generate the Verilog code by invoking chiselMain() in our main()
object Main {
  def main(args: Array[String]): Unit = {
    println("Generating the hardware")
    chiselMain(Array("--backend", "v", "--targetDir", "generated"),
      () => Module(new Top()))
  }
}