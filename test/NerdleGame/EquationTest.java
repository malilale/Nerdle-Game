
package NerdleGame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

/**
 *
 * @author Muhammed Ali
 */
public class EquationTest {
    
    public EquationTest() {
        
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
   
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of generateEquation methods, of class Equation.
     */
    @Test //The lenght of the returned equation must be between 6 and 9
    public void testEquationLenght() {
        Equation instance = new Equation();
        String resultEquation = instance.generateEquation();
        int equationLenght = resultEquation.length();
        assertTrue(equationLenght<=9 && equationLenght >=6);
    }
    
    @Test //The equation must not starts with zero
    public void testIfEquationStartsWithZero() {
        Equation instance = new Equation();
        String resultEquation = instance.generateEquation();
        char unexpectedChar = '0';
        assertNotEquals(unexpectedChar, resultEquation.charAt(0));
    }
    
    @Test //The equation must contains equal sign ('=')
    public void testIfEquationContainsEqualSign() {
        Equation instance = new Equation();
        boolean checkEqualSign = false;
        String resultEquation = instance.generateEquation();
        char equal = '=';
        for(char c:resultEquation.toCharArray()){
            if(c==equal)
                checkEqualSign = true;
        }
        assertTrue(checkEqualSign);
    }
    
    @Test //The equation must not ends with an operator
    public void testIfEquationEndsWithOperator() {
        Equation instance = new Equation();
        String resultEquation = instance.generateEquation();
        int equationLenght = resultEquation.length();
        assertFalse(resultEquation.charAt(equationLenght-1) == '/' ||
        resultEquation.charAt(equationLenght-1) == '*' ||
        resultEquation.charAt(equationLenght-1) == '+' ||
        resultEquation.charAt(equationLenght-1) == '-' ||
        resultEquation.charAt(equationLenght-1) == '=' );
    }

    
}
