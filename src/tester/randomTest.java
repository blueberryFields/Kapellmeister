package tester;

import java.util.Random;

public class randomTest {

    public static void main(String[] args) {
	Random rn = new Random();
	for (int i = 0; i < 8; i++) {
	    int randomNr = rn.nextInt(8);
	    System.out.println(randomNr);
	}
    }

}
