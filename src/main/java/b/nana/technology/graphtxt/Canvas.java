package b.nana.technology.graphtxt;

public class Canvas {

    private final int width;
    private final int height;
    private final Pixel[][] pixels;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new Pixel[height][width];
    }

    public Pixel getPixel(int x, int y) {
        Pixel pixel = pixels[y][x];
        if (pixel == null) {
            pixel = new Pixel();
            pixels[y][x] = pixel;
        }
        return pixel;
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        for (int y = 0; y < height; y++) {
            Pixel[] row = pixels[y];
            for (int x = 0; x < width; x++) {
                Pixel pixel = row[x];
                if (pixel != null)
                    pixel.appendTo(text);
                else
                    text.append(' ');
            }
            text.append('\n');
        }
        return text.toString();
    }
}
