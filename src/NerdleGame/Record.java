
package NerdleGame;

import java.io.Serializable;

/**
 *
 * @author Muhammed Ali
 */

@SuppressWarnings("serial")
public class Record implements Serializable{
    /**
     * Bu sınıf oyundaki son durumun dosyaya kaydedilmesi için 
     */
    private int lenght;
    private int round;
    private int durum;
    private int time;
    private String equation;
    private Blocks recordedBlocks[][];

    public Record(int lenght, int round, int durum, int time, String equation, Blocks[][] recordedBlocks) {
        this.lenght = lenght;
        this.round = round;
        this.durum = durum;
        this.time = time;
        this.equation = equation;
        this.recordedBlocks = recordedBlocks;
    }

    public int getLenght() {
        return lenght;
    }

    public int getRound() {
        return round;
    }

    public int getDurum() {
        return durum;
    }
    
    public int getTime(){
        return time;
    }

    public String getEquation() {
        return equation;
    }

    public Blocks[][] getRecordedBlocks() {
        return recordedBlocks;
    }
}
