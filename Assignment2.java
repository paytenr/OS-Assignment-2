
import java.util.Scanner;
import java.util.Arrays;

public class Assignment2 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Enter reference string (9999 to exit): ");
            String reference = input.nextLine().trim();

            if (reference.equals("9999")) {
                System.out.println("Program terminated.");
                break;
            }

            // Remove commas and spaces
            String cleaned = reference.replaceAll("[,\\s]", "");

            if (cleaned.isEmpty() || cleaned.length() > 15
                    || !cleaned.matches("[0-9]+")) {
                System.out.println("Invalid reference string.");
                continue;
            }

            int[] pages = new int[cleaned.length()];

            for (int i = 0; i < cleaned.length(); i++) {
                pages[i] = Character.getNumericValue(cleaned.charAt(i));
            }

            System.out.println("\nSelect Page Replacement Algorithm:");
            System.out.println("1. LRU Approximation Using a Reference Byte");
            System.out.println("2. Second Chance Replacement");
            System.out.println("3. Optimal Replacement");
            System.out.println("4. LFU");
            System.out.println("5. FIFO");

            System.out.print("Enter choice (1-5): ");
            int choice;

            try {
                choice = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid algorithm choice.");
                continue;
            }

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid algorithm choice.");
                continue;
            }

            System.out.print("Enter number of frames (2-9): ");
            int frameCount;

            try {
                frameCount = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of frames.");
                continue;
            }

            if (frameCount < 2 || frameCount > 9) {
                System.out.println("Number of frames must be 2-9.");
                continue;
            }

            System.out.println();

            if (choice == 5) {
                fifo(pages, frameCount);
            } else {
                System.out.println("Algorithm not implemented yet.");
            }

            System.out.println("---------------------------------------------");
        }

        input.close();
    }

    public static void fifo(int[] pages, int frameCount) {
        int[] frames = new int[frameCount];
        Arrays.fill(frames, -1);

        int pointer = 0;
        int pageFaults = 0;

        System.out.println("Algorithm: FIFO");

        System.out.print("Reference String: ");
        for (int i = 0; i < pages.length; i++) {
            System.out.print(pages[i]);
            if (i < pages.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        System.out.println("Number of frames: " + frameCount);
        System.out.println("Content of frames after each page-fault:");

        for (int page : pages) {
            boolean found = false;

            for (int frame : frames) {
                if (frame == page) {
                    found = true;
                    break;
                }
            }

            // Only change frames when a page fault occurs
            if (!found) {
                pageFaults++;

                frames[pointer] = page;
                pointer = (pointer + 1) % frameCount;

                printFrames(frames);
            }
        }

        System.out.println("Number of page faults: " + pageFaults);
    }

    public static void printFrames(int[] frames) {
        for (int frame : frames) {
            if (frame == -1) {
                System.out.print("|   ");
            } else {
                System.out.print("| " + frame + " ");
            }
        }
        System.out.println("|");
    }
}
