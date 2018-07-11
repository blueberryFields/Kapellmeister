package sequncerMotor;

import java.util.Scanner;

import note.Am;

public class SeqMainTest {

    public static void main(String[] args) {
	Scanner in = new Scanner(System.in);
	Sequencer seq = new Sequencer();
	Am am = new Am();

	System.out.println("Choose a device:");
	int index = in.nextInt();
	seq.chooseMidiDevice(index);

	// seq.playTestNote();

	//seq.generateSequence(8, am);
	seq.playSequence();

	System.out.println("Press S to Stop playing or N for new sequence:");

	while (true) {
	    String choice = in.next();
	    if (choice.equals("s") || choice.equals("S")) {
		seq.stopSequence();
		seq.closeDevice();
		seq.closeRcvr();
		in.close();
		break;
	    } else if (choice.equals("n") || choice.equals("N")) {
		seq.stopSequence();
		//seq.generateSequence(8, am);
		seq.playSequence();
	    }
	}
    }
}
