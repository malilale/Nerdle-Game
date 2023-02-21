
package NerdleGame;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Muhammed Ali
 */
public class FileOperations {
    
    //Record sınıfının objesi olarak tutulan oyunun kaydedilmiş bilgilerini "records.dat" dosyasına kaydeder
    public static void serializeRecord(Record rec){
        try {
         FileOutputStream fileOut = new FileOutputStream("records.dat");
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(rec);
         out.close();
         fileOut.close();
         System.out.printf("Serialized data is saved.");
      } catch (IOException e) {
         e.printStackTrace();
         System.out.printf("Data could not serialized!");
      }
    }
    
    //"records.dat" dosyasından keydedilmiş oyun bilgilerini okuyup Record türünde döndürür
    public static Record readRecord(){
        try {
         FileInputStream fileIn = new FileInputStream("records.dat");
         ObjectInputStream in = new ObjectInputStream(fileIn);
         
         Record readenRec = (Record) in.readObject();
    
         in.close();
         fileIn.close();
         return readenRec;
      } catch (IOException i) {
         i.printStackTrace();
         return null;
      } catch (ClassNotFoundException c) {
         System.out.println("Record class not found");
         c.printStackTrace();
         return null;
      }
    }
    
    //Statics sınıfının objesi olarak tutulan geçmiş oyunların istatistikleri "statics.dat" dosyasına kaydeder
    public static void serializeStatics(Statics stat){
        try {
         FileOutputStream fileOut = new FileOutputStream("statics.dat");
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(stat);
         out.close();
         fileOut.close();
         System.out.printf("Serialized data is saved.");
      } catch (IOException e) {
         e.printStackTrace();
         System.out.printf("Data could not serialized!");
      }
    }
    
    //"statics.dat" dosyasından geçmiş oyunların istatistiklerini okuyup Statics türünde döndürür
    public static Statics readStatics(){
        try {
         FileInputStream fileIn = new FileInputStream("statics.dat");
         ObjectInputStream in = new ObjectInputStream(fileIn);
         
         Statics readenstatics = (Statics) in.readObject();
    
         in.close();
         fileIn.close();
         return readenstatics;
      } catch (IOException i) {
         i.printStackTrace();
         return null;
      } catch (ClassNotFoundException c) {
         System.out.println("Statics class not found");
         c.printStackTrace();
         return null;
      }
    }
}