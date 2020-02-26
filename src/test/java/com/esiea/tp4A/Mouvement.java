import java.util.Scanner; 
import java.util.*;

public class Mouvement{

    public static void main (String[] args){

        int x = 0;
        int y = 0;
        char n = 'N';

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Input an z,s,q,d");
            char c = sc.next().charAt(0);

            if (c == 'z') {

                if (n == 'N'){
                    y++;
                }

                if (n == 'S'){
                    y--;
                }

                if (n == 'W'){
                    x--;
                }

                if (n == 'E'){
                    x++;
                }

                System.out.println("(" + x + "," + y + "," + n + ")"); 
            continue;
            }

            if (c == 's') {

                if (n == 'N'){
                    y--;
                }

                if (n == 'S'){
                    y++;
                }

                if (n == 'W'){
                    x++;
                }

                if (n == 'E'){
                    x--;
                }

                System.out.println("(" + x + "," + y + "," + n + ")"); 
            continue;
            }

            if (c == 'q') {

                if (n == 'N'){
                    n = 'W';
                }

                else if (n == 'S'){
                    n = 'E';
                }

                else if (n == 'W'){
                    n = 'S';
                }

                else if (n == 'E'){
                    n = 'N';
                }

                System.out.println("(" + x + "," + y + "," + n + ")"); 
            continue;
            }

            if (c == 'd') {

                if (n == 'N'){
                    n = 'E';
                }

                else if (n == 'S'){
                    n = 'W';
                }

                else if (n == 'W'){
                    n = 'N';
                }

                else if (n == 'E'){
                    n = 'S';
                }           
                System.out.println("(" + x + "," + y + "," + n + ")"); 
            continue;
            }
        }
    
    }
}