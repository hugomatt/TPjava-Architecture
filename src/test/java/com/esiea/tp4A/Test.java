//Ceci importe la classe Scanner du package java.util
import java.util.Scanner; 
//Ceci importe toutes les classes du package java.util
import java.util.*;

public class Test {

    public static void main (String[] args){

    Scanner sc = new Scanner(System.in);
    System.out.println("Veuillez saisir un mot :");
    String str = sc.nextLine();
    System.out.println("Vous avez saisi : " + str);
    
}

}
