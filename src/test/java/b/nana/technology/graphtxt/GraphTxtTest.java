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
    void test() {
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