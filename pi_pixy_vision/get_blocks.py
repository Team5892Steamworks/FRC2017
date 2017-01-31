"""
Uses NetworkTables and the Pixy camera to send the raw block data to the robot.
More specifically, it sends the x and y positions of the two biggest blocks, which should be the boiler tape.
Presumably later I will make a program that gives the robot more directly useful information.
However, right now I just want the Pixy, Pi, and RoboRIO communicating.

Also, I feel like we need a standard for when programs use NetworkTables so that we can easily tell where to get and put stuff.
So here one is.

== NetworkTables info ==
This program puts data at:
    /PixyVision/get_blocks/xpos1 (Number)
    /PixyVision/get_blocks/ypos1 (Number)
    /PixyVision/get_blocks/xpos2 (Number)
    /PixyVision/get_blocks/ypos2 (Number)
This program gets data from:

"""

from networktables import NetworkTables
from pixy import *
from ctypes import *

# Pixy Python SWIG get blocks example #

print ("Pixy Python SWIG Example -- Get Blocks (NetworkTables edition)")

# Initialize Pixy Interpreter thread #
pixy_init()

# Initialize NetworkTables #
NetworkTables.initialize(server="10.58.92.2")
table = NetworkTables.getTable("PixyVision")
ntable = table.getSubTable("get_blocks")

class Blocks (Structure):
  _fields_ = [ ("type", c_uint),
               ("signature", c_uint),
               ("x", c_uint),
               ("y", c_uint),
               ("width", c_uint),
               ("height", c_uint),
               ("angle", c_uint) ]

blocks = BlockArray(100)
frame  = 0

# Wait for blocks #
while 1:

  count = pixy_get_blocks(100, blocks)

  if count > 0:
    # Blocks found #
    print 'frame %3d:' % (frame)
    frame = frame + 1
    max_blocks = [None, None]
    for index in range (0, count):
      print '[BLOCK_TYPE=%d SIG=%d X=%3d Y=%3d WIDTH=%3d HEIGHT=%3d]' % (blocks[index].type, blocks[index].signature, blocks[index].x, blocks[index].y, blocks[index].width, blocks[index].height)
      if blocks[index].signature == 1:
        if max_blocks[0] is None or blocks[index].width * blocks[index].height > max_blocks[0].width * max_blocks[0].height:
          max_blocks[1] = max_blocks[0]
          max_blocks[0] = blocks[index]
        elif max_blocks[1] is None or blocks[index].width * blocks[index].height > max_blocks[1].width * max_blocks[1].height:
          max_blocks[1] = blocks[index]
    if max_blocks[0] is not None:
      ntable.putNumber("xpos1", max_blocks[0].x)
      ntable.putNumber("ypos1", max_blocks[0].y)
      if max_blocks[1] is not None:
        ntable.putNumber("xpos2", max_blocks[1].x)
        ntable.putNumber("ypos2", max_blocks[1].y)
      else:
        ntable.putNumber("xpos2", -1)  # -1 denotes that there is not a block. Which is kind of obvious but w/e.
        ntable.putNumber("ypos2", -1)
    else:
      ntable.putNumber("xpos1", -1)
      ntable.putNumber("ypos1", -1)
