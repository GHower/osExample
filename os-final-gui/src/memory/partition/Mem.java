package memory.partition;

public class Mem {
    public int start;
    public int size;
    public int end;

    public Mem() {}

    public Mem(int start, int size) {
        this.start = start;
        this.size = size;
        this.end = start+size;
    }

    @Override
    public String toString() {
        return "Mem{" +
                "start=" + start +
                ", size=" + size +
                ", end=" + end +
                '}';
    }
}
