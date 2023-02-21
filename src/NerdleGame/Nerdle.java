
package NerdleGame;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Muhammed Ali
 */
public class Nerdle extends javax.swing.JFrame{

    /**
     * Creates new form NerdleWindow
     */
    private final Equation eq=new Equation();
    private String equation;
    private ArrayList<Character> wrongChars = new ArrayList<>();
    private int lenght;
    private int round;
    private int durum;
    private int gecenSure;
    private Statics stat;
    private Blocks[][] blocks;
    private final JPanel boardPanel = new JPanel();
    private final Menu menu= new Menu(this);
    private final Test test= new Test(this);
    private boolean lost;
    private boolean won;
    private Timer timer;

    private final Color yesil = new Color(0,155,50);
    private final Color kirmizi = new Color(205,0,0);
    private final Color sari = new Color(255,240,0);
   
    
    public Nerdle() {
        initComponents();
        init();
        if(!menu.checkStatFile()) //kayıtlı istatistik yoksa
            stat=new Statics(); 
        else
            stat = FileOperations.readStatics(); //kayıtlı istatistik bilgilerini oku
    }
    
    private void init(){
        mainPanel.setLayout(null); //ana panel
        mainPanel.setBounds(0,0,1280,720);
        this.add(mainPanel); //ana paneli ekrana ekle
        boardPanel.setLayout(null);
        boardPanel.setBackground(new Color (220,230,240));
        boardPanel.setBounds(0, 0, 850, 720);
        addMouseListenerToPanel();
        menu.setBounds(0,0,1280,720);
        mainPanel.add(menu); //ana panele menüyü ekle
        numPad.setVisible(false);
    }
    
    //Blokları oluştur
    private void gridCreation() {
        blocks = new Blocks[6][lenght];
        for(int i = 0 ; i < 6 ; i ++ ) {
            for(int j = 0 ; j < lenght ; j++) {
                blocks[i][j] = new Blocks(90 + 70*j, 150 + 70*i); 
                boardPanel.add(blocks[i][j].getDisplay());
            }
        }
        blocks[0][0].setBorder();
    }
    
    public void newGame(){ //yeni oyun başlar
        gecenSure=0;
        setTimer(); //süreyi ayarlar
        if(menu.checkStatFile())
            stat = FileOperations.readStatics();
        equation = eq.generateEquation();
        lenght=equation.length();
        round=0;
        durum=0;
        won=false;
        lost=false;
        gridCreation();
        numPad.setVisible(true);
        mainPanel.remove(menu); //menüyü kaldırır
        mainPanel.add(boardPanel); //oyun ekranını ekler
        this.revalidate();
        this.repaint();
    }
    
    private void addMouseListenerToPanel(){
    boardPanel.addMouseListener(new MouseListener() {
    @Override
    public void mouseClicked(MouseEvent e) {
        if(won || lost)
            return;
        Point coordinates=e.getPoint(); //mouse tıklandığındaki koordinatlarını alır
        int position = getSelectedBlock(coordinates.x, coordinates.y);
        if(position!=-1){
            blocks[round][durum].removeBorder();
            durum = position;
            blocks[round][durum].setBorder();
        }
    }
            @Override
            public void mousePressed(MouseEvent e){}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }
    
    private void setTimer(){ //ekrana süreyi yazdırır. süreyi saniye cinsinden saklar
        timeLabel.setText(String.format("%02d:%02d", gecenSure/60,gecenSure%60));
        timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            gecenSure++;
            timeLabel.setText(String.format("%02d:%02d", gecenSure/60,gecenSure%60));
        }
    });
        timer.start();
    }
    
    
    private int getSelectedBlock(int x, int y){ //mouse tıklandığında mouse'un hangi blok üzerinde olduğunu koordinatlar aracılığıyla bulur
       for(int i=0; i<lenght;i++){
           if(blocks[round][i].isSelected(x, y))
               return i;
       }
       return -1;
    }
    
    private boolean isWon(){ //tüm bloklar yeşilse kazanmılmıştır
        for(Blocks b: blocks[round])
            if(!(b.getDisplay().getBackground()==yesil))
                return false;
        won=true;
        return true;
    }
    
    
    private boolean isFull(){ //tahmin etmek için tüm blokların dolu olmasını kontrol eder
        for(Blocks b:blocks[round]){
            if(b.getText().isBlank())
                return false;
        }
        return true;
    }
    
    
    public void goTest(){ //TEST Equation paneli
        mainPanel.remove(menu);
        test.setBounds(0,0,1280,680);
        mainPanel.add(test);
        numPad.setVisible(false);
        this.revalidate();
        this.repaint();
    }
    
    
    private void winDialog(){ //Oyun kazandıldığında çalışlır
        Object[] options = {"Ana Menüye Dön","Yeni Oyuna Başla"};
        int response = JOptionPane.showOptionDialog(this, "TEBRİKLER!! KAZANDINIZ!!\n Geçen Süre: "+String.format("%02d:%02d", gecenSure/60,gecenSure%60), "KAZANDINIZ", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (response == JOptionPane.YES_OPTION) {
            boardPanel.removeAll();
            returnToMenu(boardPanel);
        }else if(response == JOptionPane.NO_OPTION){
            FileOperations.serializeStatics(stat);
            boardPanel.removeAll();
            this.revalidate();
            this.repaint();
            newGame();
        }
    }
    
    private void lostDialog(){ //Oyun kaybedildiğinde çalışır
        Object[] options = {"Ana Menüye Dön","Yeni Oyuna Başla"};
        int response = JOptionPane.showOptionDialog(this, "ÜZGÜNÜM KAYBETTİNİZ", "GAME OVER", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (response == JOptionPane.YES_OPTION) {
            boardPanel.removeAll();
            returnToMenu(boardPanel);
        }else if(response == JOptionPane.NO_OPTION){
            FileOperations.serializeStatics(stat);
            boardPanel.removeAll();
            this.revalidate();
            this.repaint();
            newGame();
        }
    }
    
    public void returnToMenu(JPanel currentPanel){ //Oyundan veya TESTten ana menüye döndürür
        FileOperations.serializeStatics(stat);
        mainPanel.remove(currentPanel);
        menu.getStatics();
        menu.checkRecordFile();
        mainPanel.add(menu);
        this.revalidate();
        this.repaint();
        numPad.setVisible(false);
    }
    
    public void devamEt(){ //kayıtlı dosyadan aldığı verilere göre oyunu aynı yerden tekrar başlatır
        Record rec=FileOperations.readRecord();
        gecenSure=rec.getTime();
        setTimer();
        equation = rec.getEquation();
        lenght=rec.getLenght();
        round=rec.getRound();
        durum=rec.getDurum();
        
        blocks=rec.getRecordedBlocks();
        for(int i = 0 ; i < 6 ; i ++ ) {
            for(int j = 0 ; j < lenght ; j++) {
                boardPanel.add(blocks[i][j].getDisplay());  //kayıtlı blokları ekrana ekler
            }
        }
        blocks[round][durum].setBorder();
        won=false;
        lost=false;
        mainPanel.remove(menu);
        mainPanel.add(boardPanel);
        this.revalidate();
        this.repaint();
        numPad.setVisible(true);
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        numPad = new javax.swing.JPanel();
        jButton_esit = new javax.swing.JButton();
        jButtonTahmin = new javax.swing.JButton();
        jButtonSil = new javax.swing.JButton();
        jButton0 = new javax.swing.JButton();
        jButton_bolu = new javax.swing.JButton();
        jButton_carpi = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton_eksi = new javax.swing.JButton();
        jButton_arti = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        ButtonReturn = new javax.swing.JButton();
        sonraBitirButton = new javax.swing.JButton();
        timeLabel = new javax.swing.JLabel();
        footer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1280, 720));

        mainPanel.setPreferredSize(new java.awt.Dimension(1280, 720));

        jButton_esit.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton_esit.setText("=");
        jButton_esit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButtonTahmin.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButtonTahmin.setText("Tahmin Et");
        jButtonTahmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTahminActionPerformed(evt);
            }
        });

        jButtonSil.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jButtonSil.setText("Sil");
        jButtonSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSilActionPerformed(evt);
            }
        });

        jButton0.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton0.setText("0");
        jButton0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton_bolu.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton_bolu.setText("/");
        jButton_bolu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton_carpi.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton_carpi.setText("*");
        jButton_carpi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton7.setText("7");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton8.setText("8");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton9.setText("9");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton6.setText("6");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton5.setText("5");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton4.setText("4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton_eksi.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton_eksi.setText("-");
        jButton_eksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton_arti.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton_arti.setText("+");
        jButton_arti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton1.setText("1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton2.setText("2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jButton3.setText("3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberButtonsActionPerformed(evt);
            }
        });

        ButtonReturn.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        ButtonReturn.setText("Menüye Dön");
        ButtonReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonReturnActionPerformed(evt);
            }
        });

        sonraBitirButton.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        sonraBitirButton.setText("Sonra Bitir");
        sonraBitirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sonraBitirButtonActionPerformed(evt);
            }
        });

        timeLabel.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout numPadLayout = new javax.swing.GroupLayout(numPad);
        numPad.setLayout(numPadLayout);
        numPadLayout.setHorizontalGroup(
            numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(numPadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(numPadLayout.createSequentialGroup()
                        .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton_arti, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton_eksi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton_carpi, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jButton_bolu, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton_esit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(numPadLayout.createSequentialGroup()
                                .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(numPadLayout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(numPadLayout.createSequentialGroup()
                                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(numPadLayout.createSequentialGroup()
                                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(numPadLayout.createSequentialGroup()
                                .addComponent(jButton0, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonSil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(ButtonReturn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(numPadLayout.createSequentialGroup()
                        .addComponent(sonraBitirButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonTahmin, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
            .addGroup(numPadLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        numPadLayout.setVerticalGroup(
            numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(numPadLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(numPadLayout.createSequentialGroup()
                        .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonSil, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton0, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(numPadLayout.createSequentialGroup()
                        .addComponent(jButton_arti, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_eksi, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_carpi, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_bolu, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton_esit, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(ButtonReturn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(numPadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sonraBitirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonTahmin, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(timeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        footer.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        footer.setText("Muhammed Ali LALE    -    20011045");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(902, 902, 902)
                .addComponent(numPad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(footer, javax.swing.GroupLayout.PREFERRED_SIZE, 958, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addComponent(numPad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(footer, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1268, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSilActionPerformed
        if(won || lost)
            return;
        blocks[round][durum].removeBorder();
        if(blocks[round][durum].getText().isBlank() && durum!=0)
            durum--;
        blocks[round][durum].setText("");
        blocks[round][durum].setBorder();
    }//GEN-LAST:event_jButtonSilActionPerformed

    private void jButtonTahminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTahminActionPerformed
        if(won || lost)
            return;
        if(!isFull()){
            JOptionPane.showMessageDialog(this, "Lütfen tüm boşlukları doldurun!");
            return;
        }
        if(!eq.checkEquation(getEnteredEquation()))
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir tahmin giriniz!");
        else{
            setColors();
            if(isWon()){  //oyun kazanılmışsa oyun biter ve istatistikler kaydedilir
                timer.stop();
                stat.incTotalGames();
                stat.incWinGames();
                stat.setAverageRowNumber(round+1);
                stat.setAverageTime(gecenSure);
                winDialog();
                blocks[round][durum].removeBorder();
            }else if(round==5){ //oyun kaybedilmişse oyun biter ve istatistikler kaydedilir
                timer.stop();
                lost=true;
                stat.incLostGames();
                stat.incTotalGames();
                lostDialog();
                blocks[round][durum].removeBorder();
            }else{ //diğer durumlarda alt satıra geçer
                blocks[round][durum].removeBorder();
                round++;
                durum=0;
                wrongChars.clear();
                blocks[round][durum].setBorder();
            }
        }
    }//GEN-LAST:event_jButtonTahminActionPerformed

    //giirlen denklemi bloklardan  bir stringe okur
    private String getEnteredEquation(){
        String inputEquation = new String();
        for(int i=0; i<lenght; i++){
            inputEquation += blocks[round][i].getText();
        }
        return inputEquation;
    }
    
    //blok renklerini ayarlar
    private void setColors(){
        for(int i=0; i<lenght;i++){
            if(blocks[round][i].getChar()==equation.charAt(i)){
                //blocks[round][i].setColor(Color.GREEN);
                blocks[round][i].setColor(yesil);
                
            }else{
                wrongChars.add(equation.charAt(i));
            }
        }
        System.out.println(wrongChars);
        for(int i=0; i<lenght;i++){
            if(!(blocks[round][i].getColor()==yesil)){
                if(wrongChars.contains(blocks[round][i].getChar()))
                    //blocks[round][i].setColor(Color.YELLOW);
                    blocks[round][i].setColor(sari);
                else
                    //blocks[round][i].setColor(Color.red);
                    blocks[round][i].setColor(kirmizi);
            }        
        }
    }
    
    //butonlara tıklanınca seçili bloğa yazar
    private void numberButtonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberButtonsActionPerformed
        if(won || lost)
            return;
        
        JButton selectedButton = (JButton)evt.getSource();
        String source = selectedButton.getText();
        if(source.compareTo("0")==0 && durum==0)
            return;
        if(durum < lenght){
            blocks[round][durum].setText(source);
            if(durum<lenght-1){
                blocks[round][durum].removeBorder();
                blocks[round][durum+1].setBorder();
                durum++;
            }
        }
    }//GEN-LAST:event_numberButtonsActionPerformed

    //tıklandığında ana menüye döner
    private void ButtonReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonReturnActionPerformed
        Object[] options = {"Ana Menüye Dön","Sayfada Kal"};
        int response = JOptionPane.showOptionDialog(this, "Oyundan çıkıp ana menüye dönmek istediğinizden emin misiniz?", "Ana Menüye Dön", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (response == JOptionPane.YES_OPTION) {
            timer.stop();
            stat.incTotalGames();
            stat.incUnfinishedGames();
            boardPanel.removeAll();
            returnToMenu(boardPanel);
        }
    }//GEN-LAST:event_ButtonReturnActionPerformed

    //mevcut oyunu dosyaya kaydedip menüye döner
    private void sonraBitirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sonraBitirButtonActionPerformed
        timer.stop();
        Record rec=new Record(lenght, round, durum, gecenSure, equation, blocks);
        FileOperations.serializeRecord(rec);
        boardPanel.removeAll();
        returnToMenu(boardPanel);
    }//GEN-LAST:event_sonraBitirButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Nerdle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Nerdle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Nerdle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Nerdle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Nerdle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonReturn;
    private javax.swing.JLabel footer;
    private javax.swing.JButton jButton0;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonSil;
    private javax.swing.JButton jButtonTahmin;
    private javax.swing.JButton jButton_arti;
    private javax.swing.JButton jButton_bolu;
    private javax.swing.JButton jButton_carpi;
    private javax.swing.JButton jButton_eksi;
    private javax.swing.JButton jButton_esit;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel numPad;
    private javax.swing.JButton sonraBitirButton;
    private javax.swing.JLabel timeLabel;
    // End of variables declaration//GEN-END:variables
}
