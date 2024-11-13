/******************************************************************************
 *  Compilation:  javac GenomeCompressor.java
 *  Execution:    java GenomeCompressor - < input.txt   (compress)
 *  Execution:    java GenomeCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   genomeTest.txt
 *                virus.txt
 * <p>
 *  Compress or expand a genomic sequence using a 2-bit code.
 ******************************************************************************/

import java.util.Map;

/**
 *  The {@code GenomeCompressor} class provides static methods for compressing
 *  and expanding a genomic sequence using a 2-bit code.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 *  @author Alexandre Antoine Haddad-Delaveau
 */
public class GenomeCompressor {

    /**
     * Reads a sequence of 8-bit extended ASCII characters over the alphabet
     * { A, C, T, G } from standard input; compresses and writes the results to standard output.
     */
    public static void compress() {
        Map<Character, Integer> letterMap = Map.of(
                'A', 0,
                'C', 1,
                'T', 2,
                'G', 3
        );

        String s = BinaryStdIn.readString();

        // Get remainder
        int length = s.length();
        int remainder = length % 8;

        // Write length as first 4 bits
        BinaryStdOut.write(remainder, 4);

        // Write letters
        for (char letter : s.toCharArray()) {
            int letterValue = letterMap.get(letter);

            // Write bits
            BinaryStdOut.write(letterValue, 2);
        }

        BinaryStdOut.close();
    }

    /**
     * Reads a binary sequence from standard input; expands and writes the results to standard output.
     */
    public static void expand() {

        Map<Integer, Character> letterMap = Map.of(
                0, 'A',
                1, 'C',
                2, 'T',
                3, 'G'
        );

        // Read remainder
        int remainder = BinaryStdIn.readInt(4);

        // Buffer (8 letters)
        StringBuilder buffer = new StringBuilder();

        // Read letters
        while (!BinaryStdIn.isEmpty()) {
            int letterValue = BinaryStdIn.readInt(2);

            buffer.append(letterMap.get(letterValue));

            // If buffer is full, write to output
            if (buffer.length() == 8) {
                BinaryStdOut.write(buffer.toString());
                buffer.setLength(0);
            }
        }

        // Write remaining buffer to output
        BinaryStdOut.write(buffer.substring(0, remainder));

        BinaryStdOut.close();
    }


    /**
     * Main, when invoked at the command line, calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}