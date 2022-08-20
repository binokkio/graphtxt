package b.nana.technology.graphtxt;

import org.junit.jupiter.api.Test;

import java.util.List;

class GraphTxtTest {

    @Test
    void testAB() {
        System.out.println(new GraphTxt(
                List.of(
                        SimpleNode.of("AAA", "BBBBBBBBBBB"),
                        SimpleNode.of("BBBBBBBBBBB")
                )
        ).getText());
    }

    @Test
    void testTwoRootsIntertwined() {
        System.out.println(new GraphTxt(
                List.of(
                        SimpleNode.of("Alpha", "Booo", "C", "F"),
                        SimpleNode.of("Booo", "F"),
                        SimpleNode.of("C"),
                        SimpleNode.of("D", "Booo", "E", "ZZZ", "H"),
                        SimpleNode.of("E"),
                        SimpleNode.of("F"),
                        SimpleNode.of("ZZZ"),
                        SimpleNode.of("H")
                )
        ).getText());
    }
}