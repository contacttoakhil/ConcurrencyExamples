package phaser.buffer.implementations;

/**
 * Created by Akhil on 07-03-2015.
 */
public interface IBuffer {
    public void write(String value);
    public void cleanUp();
    public int size();
}
