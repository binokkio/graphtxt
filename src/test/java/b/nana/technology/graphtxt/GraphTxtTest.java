package b.nana.technology.graphtxt;

import org.junit.jupiter.api.Test;

import java.util.List;

class GraphTxtTest {

    @Test
    void testAB() {
        System.out.println(new GraphTxt(
                List.of(
                        SimpleNode.of("A", "B"),
                        SimpleNode.of("B")
                )
        ).getText());
    }

    @Test
    void testPyramid() {
        System.out.println(new GraphTxt(
                List.of(
                        SimpleNode.of("1", "22"),
                        SimpleNode.of("22", "333"),
                        SimpleNode.of("333", "4444"),
                        SimpleNode.of("4444", "55555"),
                        SimpleNode.of("55555", "666666"),
                        SimpleNode.of("666666", "7777777"),
                        SimpleNode.of("7777777", "88888888"),
                        SimpleNode.of("88888888", "999999999"),
                        SimpleNode.of("999999999")
                )
        ).getText());
    }

    @Test
    void testReversePyramid() {
        System.out.println(new GraphTxt(
                List.of(
                        SimpleNode.of("999999999", "88888888"),
                        SimpleNode.of("88888888", "7777777"),
                        SimpleNode.of("7777777", "666666"),
                        SimpleNode.of("666666", "55555"),
                        SimpleNode.of("55555", "4444"),
                        SimpleNode.of("4444", "333"),
                        SimpleNode.of("333", "22"),
                        SimpleNode.of("22", "1"),
                        SimpleNode.of("1")
                )
        ).getText());
    }

    @Test
    void testTwoRootsIntertwined() {
        System.out.println(new GraphTxt(
                List.of(
                        SimpleNode.of("AAAAA", "BBBB", "C", "F"),
                        SimpleNode.of("BBBB", "F"),
                        SimpleNode.of("C"),
                        SimpleNode.of("D", "BBBB", "E", "ZZZ", "H"),
                        SimpleNode.of("E"),
                        SimpleNode.of("F"),
                        SimpleNode.of("ZZZ"),
                        SimpleNode.of("H", "IIII"),
                        SimpleNode.of("IIII")
                )
        ).getText());
    }
}