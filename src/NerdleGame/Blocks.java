
package NerdleGame;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import javax.swing.BorderFactory;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 *
 * @author Muhammed Ali
 */
@SuppressWarnings("serial")
public class Blocks implements Serializable{
	private final JPanel panel = new JPanel();
	private final JLabel label = new JLabel();
        private Color color;
        private final Border whiteline = BorderFactory.createLineBorder(Color.white,3,true);
        public int x1, x2, y1, y2;
	
	public Blocks(int x, int y) {
            //panel içine yazılacak olan labelın ayarları
		label.setText(" "); 
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 48));
		label.setForeground(new Color(255, 255, 255));
		label.setBounds(0, 0, 60, 56);
		
            //her bir plok bir JPanelden oluşur
		panel.setLayout(null);
		panel.setBackground(Color.gray);
		panel.setBounds(x, y, 60, 55);
		panel.add(label);
                
                color=Color.gray;
            // blokların konumları
                this.x1 = x;
                this.x2 = x+60;
                this.y1 = y;
                this.y2 = y+55;
	}
	
	public JPanel getDisplay() {
		return panel;
	}
        
        public void setColor(Color color){
            panel.setBackground(color);
            this.color = color;
        }
        
        public Color getColor(){
            return color;
        }

	public void setText (String s) {
		label.setText(s);
	}
        
        public void setChar (char c) {
		label.setText(Character.toString(c));
	}
        
        public String getText () {
		return label.getText();
	}
        public char getChar () {
		return label.getText().charAt(0);
	}
        
        //seçili bloğu göstermek için kenarlık ekleme ve kaldırma methodları
        public void setBorder(){
            panel.setBorder(whiteline);
        }
        
        public void removeBorder(){
            panel.setBorder(null);
        }
        
        //mouseclicked eventinde mouse'un koordinatlarının hangi block üzerinde olduğunu bulmak için
        public boolean isSelected(int x, int y){
            return x>x1 && x<x2 && y>y1 && y<y2;
        }
}
