package b.nana.technology.graphtxt;

import org.junit.jupiter.api.Test;

import java.util.List;

class GraphTxtTest {

    @Test
    void testSimple() {
        new GraphTxt(
                List.of(new SimpleNode("A", new SimpleEdge("Boo"), new SimpleEdge("C")), new SimpleNode("Boo"), new SimpleNode("C"))
        ).getText();
    }

    @Test
    void testTwoRoots() {
        new GraphTxt(
                List.of(SimpleNode.of("A", "B", "C"), SimpleNode.of("B"), SimpleNode.of("C"), SimpleNode.of("D", "E"), SimpleNode.of("E"))
        ).getText();
    }

    @Test
    void testTwoRootsIntertwined() {
        new GraphTxt(
                List.of(SimpleNode.of("A", "B", "C"), SimpleNode.of("B"), SimpleNode.of("C"), SimpleNode.of("D", "B", "E"), SimpleNode.of("E"))
        ).getText();
    }

    @Test
    void testJumpRow() {
        new GraphTxt(
                List.of(new SimpleNode("A", new SimpleEdge("B"), new SimpleEdge("C")), new SimpleNode("B", new SimpleEdge("C")), new SimpleNode("C"))
        ).getText();
    }
}