package b.nana.technology.graphtxt;

public class Pixel {

    private char content = ' ';

    public char getContent() {
        return content;
    }

    public void setContent(char c) {
        this.content = c;
    }

    public void appendTo(StringBuilder text) {
        text.append(content);
    }
}
