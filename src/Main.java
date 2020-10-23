import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        Exercises exercises = new Exercises();
        System.out.println("Hello world!");

        System.out.println("Enter username");
        String username=bf.readLine();
        System.out.println("Enter password");
        String password=bf.readLine();
        exercises.connection(username,password);


        //exercises.connection("root","1234")

        System.out.println("Select a exercise you want to go to (2-9) or END to Exit");
        String input;
        while (!(input = bf.readLine()).equals("END")){
            switch (input) {
                case "2" -> exercises.ex2();
                case "3" -> exercises.ex3();
                case "4" -> exercises.ex4();
                case "5" -> exercises.ex5();
                case "6" -> exercises.ex6();
                case "7" -> exercises.ex7();
                case "8" -> exercises.ex8();
                case "9" -> exercises.ex9();
                default -> System.out.println("Try again!A single number between 2 and 9 or END to Exit");
            }
            System.out.println();
            System.out.println("Select a exercise you want to go to (2-9) or END to Exit");
        }
    }
}
