package config;

public enum  PrepareColor {
    ORANGE(254, 153, 6),
    MEMORY(66,133,224),
    BG_MEMORY(211,211,211),
    RED(234,67,53),
    KERNEL(52,168,83),
    FONT_GREY(120,120,120),
    FONT_DARK_GREY(80,80,80),
    FONT_LIGHT_GREY(180,180,180);
    int r,g,b;

    PrepareColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
