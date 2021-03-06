/**
  * Licensed to Big Data Genomics (BDG) under one
  * or more contributor license agreements.  See the NOTICE file
  * distributed with this work for additional information
  * regarding copyright ownership.  The BDG licenses this file
  * to you under the Apache License, Version 2.0 (the
  * "License"); you may not use this file except in compliance
  * with the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package org.dsa.mediator.smithwaterman

import htsjdk.samtools.{Cigar, TextCigarCodec}
import scala.annotation.tailrec

abstract class SmithWaterman(xSequence: String, ySequence: String) extends Serializable {

  lazy val (maxScore, scoringMatrix, moveMatrix) = buildScoringMatrix()
  lazy val (cigarX, cigarY, xStart, yStart) = trackback(scoringMatrix, moveMatrix)
  //  lazy val maxScore=maxScores(scoringMatrix)

  /**
    * Builds Smith-Waterman scoring matrix.
    *
    * @return 2D array of doubles, along with move direction at each point.
    * @note To work with the move function, expected move directions are:
    *
    *       * I: move in I coordinate
    *       * J: move in J coordinate
    *       * B: move in both I and J
    *       * T: terminate move
    * @see move
    */
  private[smithwaterman] def buildScoringMatrix(): (Double,Array[Array[Int]], Array[Array[Char]])

  /**
    * Finds coordinates of a matrix with highest value.
    *
    * @param matrix Matrix to score.
    * @return Tuple of (i, j) coordinates.
    */
  private[smithwaterman] final def maxCoordinates(matrix: Array[Array[Int]]): (Int, Int) = {
    var xMax = 0
    var yMax = 0
    var max = Double.MinValue
    var x = 0
    while (x < matrix.length) {
      var y = 0
      while (y < matrix(x).length) {
        if (matrix(x)(y) >= max) {
          max = matrix(x)(y)
          xMax = x
          yMax = y
        }
        y += 1
      }
      x += 1
    }
    (yMax, xMax)
  }

  /**
    * Finds max Score of a matrix with highest value.
    *
    * @param matrix Matrix to score.
    * @return maxScore: max score in Matrix.
    */
  private[smithwaterman] final def maxScores(matrix: Array[Array[Double]]): Double = {

    var maxScoreReturn = 0
    var max = Double.MinValue
    var x = 0
    while (x < matrix.length) {
      var y = 0
      while (y < matrix(x).length) {
        if (matrix(x)(y) >= max) {
          max = matrix(x)(y)
        }
        y += 1
      }
      x += 1
    }
    max
  }

  /**
    * Converts a reversed non-numeric CIGAR into a normal CIGAR.
    *
    * @note A reversed non-numeric CIGAR is a CIGAR where each alignment block
    *       has length = 1, and the alignment block ordering goes from end-to-beginning. E.g.,
    *       the equivalent of the CIGAR 4M2D1M would be MDDMMMM.
    * @param nnc Reversed non-numeric CIGAR.
    * @return A normal CIGAR.
    */
  private[smithwaterman] def cigarFromRNNCigar(nnc: String): String = {

    @tailrec def buildCigar(last: Char, runCount: Int, nnc: String, cigar: String): String = {
      if (nnc.length == 0) {
        (runCount.toString + last) + cigar
      } else {
        val (next, nrc, nc) = if (nnc.head == last) {
          (last, runCount + 1, cigar)
        } else {
          (nnc.head, 1, (runCount.toString + last) + cigar)
        }
        buildCigar(next, nrc, nnc.drop(1), nc)
      }
    }

    buildCigar(nnc.head, 1, nnc.drop(1), "")
  }

  /**
    * Recursive function to do backtrack.
    *
    * @param matrix Matrix to track back upon.
    * @param i      Current position in x sequence.
    * @param j      Current position in y sequence.
    * @param cX     Current reversed non-numeric CIGAR for the X sequence.
    * @param cY     Current reversed non-numeric CIGAR for the Y sequence.
    * @return Returns the alignment CIGAR for the X and Y sequences, along with start indices.
    * @note To work with the move function, expected move directions are:
    *
    *       * I: move in I coordinate
    *       * J: move in J coordinate
    *       * B: move in both I and J
    *       * T: terminate move
    * @see buildScoringMatrix
    */
  @tailrec private[smithwaterman] final def move(
                                                  matrix: Array[Array[Char]],
                                                  i: Int,
                                                  j: Int,
                                                  cX: String,
                                                  cY: String): (String, String, Int, Int) = {
    if (matrix(i)(j) == 'T') {
      // return if told to terminate
      (cigarFromRNNCigar(cX), cigarFromRNNCigar(cY), i, j)
    } else {
      // find next move
      val (in, jn, cXn, cYn) = if (matrix(i)(j) == 'B') {
        (i - 1, j - 1, cX + "M", cY + "M")
      } else if (matrix(i)(j) == 'J') {
        (i - 1, j, cX + "I", cY + "D")
      } else {
        (i, j - 1, cX + "D", cY + "I")
      }

      // recurse
      move(matrix, in, jn, cXn, cYn)
    }
  }

  /**
    * Runs trackback on scoring matrix.
    *
    * @param scoreMatrix Scored matrix to track back on.
    * @param moveMatrix  Move matrix to track back on.
    * @return Tuple of Cigar for X, Y.
    */
  private[smithwaterman] def trackback(
                                        scoreMatrix: Array[Array[Int]],
                                        moveMatrix: Array[Array[Char]]): (Cigar, Cigar, Int, Int) = {
    assert(scoreMatrix.length == xSequence.length + 1)
    assert(scoreMatrix.forall(_.length == ySequence.length + 1))
    assert(moveMatrix.length == xSequence.length + 1)
    assert(moveMatrix.forall(_.length == ySequence.length + 1))

    // get the position of the max scored box - start trackback here
    val (sx, sy) = maxCoordinates(scoreMatrix)

    // run trackback
    val (cX, cY, xI, yI) = move(moveMatrix, sy, sx, "", "")

    // get cigars and return
    (TextCigarCodec.decode(cX), TextCigarCodec.decode(cY), xI, yI)
  }

}