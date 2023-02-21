
package NerdleGame;

import java.util.Random;

/**
 *
 * @author Muhammed Ali
 */
public class Equation {
/**
 * Bu sınıf Nerdle oyununun kurallarına uygun denklemler üreten ve
 * kullanıcıların girdiği denklemlerin doğruluğunu kontrol eden methodlar içerir
 */

    private String equation = new String();
    
    public String generateEquation(){ //random denklem üretir
        equation="";
        int number = getRandomNumber(999); 
        equation += number;
        switch (countDigits(number)) {
            case 1 -> oneDigitEq(number); //eğer tek basamaklı bir sayı geldiyse
            case 2 -> twoDigitEq(number); //eğer iki basamaklı bir sayı geldiyse
            case 3 -> defaultEquation(number,99); //eğer üç basamaklı bir sayı geldiyse
            default -> {}
        }
        return equation;
    }
    
    private void oneDigitEq(int a){
        int result=11111;
        boolean okey=false;
        while(!okey){
            char op= getRandomOperator();
            equation += op;
            int b = getRandomNumber(999);
            switch (op) {
                case '+' -> result=a+b;
                case '-' -> result=a-b;
                case '*' -> result=a*b;
                case '/' -> {
                    while(a%b!=0){ //b, a'ya tam bölünemiyorsa
                        b = getRandomNumber(9);
                    }
                    result = a/b;
                }
                default -> {}
            }
            if(countDigits(b)==1){ //b de tek basamaklıysa yeni bir operator ve sayı seç
                equation += b;
                defaultEquation(result,99);
                okey=true;
            }else if(countDigits(b)==2){ //iki basamaklıysa yazı-tura at
                if(flipCoin()){ //yazı gelirse bu iki sayıyı hesapla
                    equation = equation + b + '=' + result;
                }else{ //tura gelirse bir işlem daha ekle
                    equation += b;
                    defaultEquation(result,9);
                    okey = true;
                }
                if(equation.length()>=6 && equation.length()<=9){
                    okey=true;
                }
            }else if(countDigits(b)==3){ //3 basamaklıysa sonucu hesapla
                equation = equation + b + '=' + result;
                if(equation.length()>=7 && equation.length()<=9){ //uzunluk eğer 7-9 aralığındaysa OKEY
                    okey=true;
                }
            }
            if(!okey){
                equation = String.valueOf(a); //yeni eklenen işlem ve sayıyı sil
            }
        }
    }
    
    private void defaultEquation(int a, int max){
        String[] parts = equation.split("(?=[-+])|(?<=[-+])"); //denklemi + ve - ye göre ayırır işlem önceliği için)
        int result=11111;
        int b;
        boolean okey=false;
        String prev=equation;
        while(!okey){
            char op= getRandomOperator();
            equation += op;
            b = getRandomNumber(max);
            switch (op) {
                case '+' -> result=a+b; //+ ve - için normar durumlar
                case '-' -> result=a-b;
                case '*' -> { //* gelmişse ve önceki denklemden + - varsa önce çarpma yapılır sonra + - ile işlem yapılır
                    if(parts.length==1 || !isPlusOrMinus(parts[1].charAt(0)))
                        result=a*b;
                    else{
                        result=Integer.parseInt(parts[2])*b;
                        if(parts[1].charAt(0)=='+'){
                            result += Integer.parseInt(parts[0]);
                        }
                        if(parts[1].charAt(0)=='-'){
                            result = Integer.parseInt(parts[0]) - result;
                        }
                    }
                }
                case '/' -> { //* gelmişse ve önceki denklemden + - varsa önce bölme yapılır sonra + - ile işlem yapılır
                    if(parts.length==1 || !isPlusOrMinus(parts[1].charAt(0))){
                        while(a%b!=0){
                            b = getRandomNumber(100);
                        }
                        result = a/b;
                        break;
                    }else{
                        int second=Integer.parseInt(parts[2]);
                        int first=Integer.parseInt(parts[0]);
                        while(second%b!=0){
                            b = getRandomNumber(100);
                        }
                        result=second/b;
                        if(parts[1].charAt(0)=='+'){
                            result += first;
                        }
                        if(parts[1].charAt(0)=='-'){
                            result = first - result;
                        }
                    }
                }
                default -> {}
            }
            equation = equation + b + '=' + result;
            if(equation.length()<=9 && equation.length()>=7){
                okey=true;
            }else{
                equation = prev; 
            }
        }
    }
    
    private void twoDigitEq(int a){
        int result=11111;
        boolean okey=false;
        while(!okey){
            char op= getRandomOperator();
            equation += op;
            int b = getRandomNumber(99);
            switch (op) {
                case '+' -> result=a+b;
                case '-' -> result=a-b;
                case '*' -> result=a*b;
                case '/' -> {
                    while(a%b!=0){
                        b = getRandomNumber(99);
                    }
                    result = a/b;
                }
                default -> {}
            }
            if(countDigits(b)==1){ //tek elemanlı gelmişse yeni bir işlem ve sayı seç
                equation += b;
                defaultEquation(result,9);
                okey=true;
            }else{ //2,3 elemanlıysa hesapla
                equation = equation + b + '=' + result;
                if(equation.length()>=6 && equation.length()<=9){
                    okey=true;
                }
            }if(!okey){
                equation = String.valueOf(a);
            }
        }
    }
    
   //yazı tura methodu
    private boolean flipCoin(){
        return (new Random().nextInt(2))==1;
    }
    
    //verilen sayının basamak sayısını döndürür
    private int countDigits(int num){
        int count=0;
        while (num != 0) {
            num /= 10;
            ++count;
        }
        return count;
    }
    
    //0 ile verilen max arasında random sayı döndürür
    private int getRandomNumber(int max){
        int[] bounds = {9,99,999};
        switch (max) {
            case 9:
                return new Random().nextInt(9)+1;
            case 99:
                return new Random().nextInt(bounds[new Random().nextInt(2)])+1;
            default:
                return new Random().nextInt(bounds[new Random().nextInt(3)])+1;
        }
    }
    
    private boolean isPlusOrMinus(char c){
        return c == '+' || c == '-';
}
    
    private char getRandomOperator() {
        char[] operators = {'+','-','*','/'};
        int rnd = new Random().nextInt(operators.length);
    return operators[rnd];
}
    private boolean isOperator(char c){
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    //verilen stringin sadece sayılardan oluşup oluşmadığını kontrol eder
    private boolean isNumeric(String strNum) {
    if (strNum == null) {
        return false;
    }
    try {
        int i = Integer.parseInt(strNum);
    } catch (NumberFormatException nfe) {
        return false;
    }
    return true;
}
    //verilen denklemin doğruluğunu kontorl eder
    public boolean checkEquation(String eq){
        if(!operatorsCheck(eq)){
            return false;
        }
        if(!isEquationTrue(eq))
            return false;
        
        return true;
    }
    
    //operatorlerin doğru yerleşip yerleşmediğini kontrol eder
    private boolean operatorsCheck(String eq) {
        //does the equation starts or ends with an operator
        if(!containsEqualsSign(eq))
                return false;
    
        //does the equation contains '='
        if(isOperator(eq.charAt(0)) || isOperator(eq.charAt(eq.length()-1)) || eq.charAt(eq.length()-1)=='=')
                return false;
        
        //are there sequential operators
        for(int i=1; i<eq.length(); i++){
            if(isOperator(eq.charAt(i)) && isOperator(eq.charAt(i-1)))
                return false;
        }
        return true;
    }
    
    private boolean containsEqualsSign(String eq){
         for(int i=0; i<eq.length(); i++){
            if(eq.charAt(i)=='=')
                return true;
        }
         return false;
    }
    
    //operatörler doğruysa sonucun doğruluğunu kontrol eder
    private boolean isEquationTrue(String eq){
        String[] parts = eq.split("(?=[=])|(?<=[=])"); //eşittirin öncesi ve sonrasını ayır
        int result = Integer.parseInt(parts[2]);
        eq=parts[0];
        parts = eq.split("(?=[+-])|(?<=[+-])");  //denklemi + ve -ye göre ayır
        switch (parts.length) {
            case 1 -> {if(result == case1(parts[0])) //+ veya - yoksa
                        return true;
            }
            case 3 -> {if(result == case3(parts)) //bir tane + veya - varsa
                        return true;
            }
            case 5 -> {if(result == case5(parts)) //iki tane + veya - varsa
                        return true;
            }
            default -> {
                    return false;
            }
        }
        return false;
    }
    
    private int case1(String eq){
        String[] parts = eq.split("(?=[*/])|(?<=[*/])"); //Bu sefer de * ve / ye göre ayır

        int result=-1111;
        switch (parts.length) {
            case 3 -> { //sadece bir tane * veya / varsa
                if(parts[1].charAt(0)=='*'){
                    result = Integer.parseInt(parts[0]) * Integer.parseInt(parts[2]);
                }else if(parts[1].charAt(0)=='/'){
                    result = Integer.parseInt(parts[0]) / Integer.parseInt(parts[2]);
                }
            }
            case 5 -> { //iki tane * vyea / varsa
                if(parts[1].charAt(0)=='*'){
                    result = Integer.parseInt(parts[0]) * Integer.parseInt(parts[2]);
                }else if(parts[1].charAt(0)=='/'){
                    result = Integer.parseInt(parts[0]) / Integer.parseInt(parts[2]);
                }
                if(parts[3].charAt(0)=='*'){
                    result *= Integer.parseInt(parts[4]);
                }else if(parts[3].charAt(0)=='/'){
                    result /= Integer.parseInt(parts[4]);
                }
            }
            default -> {
                    return -1111;
            }
        }
        return result;
    }
    
    
    private int case3(String[] parts){
        int result=-1111;
        
        if(!isNumeric(parts[0])) // ayrılan ilk kısım numerik değilse sonucu hesaplayıp yine ilk kısma yaz
            parts[0] = Integer.toString(case1(parts[0]));
        if(!isNumeric(parts[2])) // ayrılan ikinci kısım numerik değilse sonucu hesaplayıp yine ikinci kısma yaz
            parts[2] = Integer.toString(case1(parts[2]));
        //hesapla
        if(parts[1].charAt(0)=='+'){
            result = Integer.parseInt(parts[0])+ Integer.parseInt(parts[2]);
        }else if(parts[1].charAt(0)=='-'){
            result = Integer.parseInt(parts[0]) - Integer.parseInt(parts[2]);
        }
        
        return result;
    }
    
    
    private int case5(String[] parts){
        int result=0;
        //önce ilk işlem
        if(parts[1].charAt(0)=='+'){
            result = Integer.parseInt(parts[0])+ Integer.parseInt(parts[2]);
        }else if(parts[1].charAt(0)=='-'){
            result = Integer.parseInt(parts[0]) - Integer.parseInt(parts[2]);
        }
        //sonra ikinci işlem
        if(parts[3].charAt(0)=='+'){
            result += Integer.parseInt(parts[4]);
        }else if(parts[3].charAt(0)=='-'){
            result -= Integer.parseInt(parts[4]);
        }
        
        return result;
    }
}