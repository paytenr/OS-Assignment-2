
import java.util.Scanner;

public class Assignment2 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running == true) {

            System.out.print("Enter reference string (9999 to exit): ");
            String reference = input.nextLine().trim();

            // Stop the program when the user enters 9999
            if (reference.equals("9999")) {
                running = false;
                continue;
            }

            // Removes commas and spaces from the user input
            String cleaned = reference.replaceAll("[,\\s]", "");

            boolean valid = true;

            if (cleaned.length() == 0 || cleaned.length() > 15) {
                valid = false;
            }

            for (int i = 0; i < cleaned.length(); i++) {
                char digit = cleaned.charAt(i);

                if (digit < '0' || digit > '9') {
                    valid = false;
                }
            }

            if (valid == false) {
                System.out.println("Invalid reference string.");
                continue;
            }

            // Converting the reference string into an integer array
            int[] pages = new int[cleaned.length()];

            for (int i = 0; i < cleaned.length(); i++) {
                String digit = cleaned.substring(i, i + 1);
                pages[i] = Integer.parseInt(digit);
            }

            // Display algorithm choices
            System.out.println("\nSelect a Page Replacement Algorithm!");
            System.out.println("1. LRU Approximation Using a Reference Byte");
            System.out.println("2. Second Chance Replacement");
            System.out.println("3. Optimal Replacement");
            System.out.println("4. LFU");
            System.out.println("5. FIFO");

            System.out.print("Enter choice: ");
            int choice;

            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid algorithm choice.");
                continue;
            }

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid algorithm choice.");
                continue;
            }

            // Ask for the number of allocated frames
            System.out.print("Enter number of frames (2-9): ");
            int frameCount;

            try {
                frameCount = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of frames.");
                continue;
            }

            if (frameCount < 2 || frameCount > 9) {
                System.out.println("Number of frames must be between 2 and 9.");
                continue;
            }

            System.out.println();

            // Run the selected algorithm
            if (choice == 1){
                lru(pages, frameCount);
            }
            else if (choice == 2){
                secondChance(pages, frameCount);
            }
            else if (choice == 3) {
                optimal(pages, frameCount);
            } 

            else if(choice == 5){
                fifo(pages, frameCount);
            }
            
            else {
                System.out.println("Please enter a valid choice.");
            }

            System.out.println("---------------------------------------------");
        }

        input.close();
        System.out.println("Program terminated.");
    }

    // FIFO Page Replacement Algorithm
    public static void fifo(int[] pages, int frameCount) {

        int[] frames = new int[frameCount];
        int pageFaults = 0;
        int filledFrames = 0;

        for (int i = 0; i < frameCount; i++) {
            frames[i] = -1;
        }

        System.out.println("Algorithm: FIFO");
        printReferenceString(pages);
        System.out.println("Number of frames: " + frameCount);
        System.out.println("Content of frames after each page-fault:");

        for (int i = 0; i < pages.length; i++) {

            int page = pages[i];
            boolean found = false;

            for (int j = 0; j < frameCount; j++) {
                if (frames[j] == page) {
                    found = true;
                }
            }

            // If the page is not found, a page fault occurs
            if (found == false) {
                pageFaults++;

                if (filledFrames < frameCount) {
                    frames[filledFrames] = page;
                    filledFrames++;
                } 
                else {
                    for (int j = 0; j < frameCount - 1; j++) {
                        frames[j] = frames[j + 1];
                    }
                    frames[frameCount - 1] = page;
                }

                printFrames(frames);
            }
        }

        System.out.println("Number of page faults: " + pageFaults);
    }


    // LFU Page Replacement Algorithm
    public static void lfu(int[] pages, int frameCount) {

        int[] frames = new int[frameCount];
        int[] counters = new int[frameCount];

        int pageFaults = 0;

        for (int i = 0; i < frameCount; i++) {
            frames[i] = -1;
            counters[i] = 0;
        }

        System.out.println("Algorithm: LFU");
        printReferenceString(pages);
        System.out.println("Number of frames: " + frameCount);
        System.out.println("Content of frames after each page-fault:");

        for (int i = 0; i < pages.length; i++) {

            int page = pages[i];
            boolean found = false;

            for (int j = 0; j < frameCount; j++) {
                if (frames[j] == page) {
                    found = true;
                    counters[j]++;
                    break;
                }
            }

            if (found == false) {
                pageFaults++;

                int emptyFrame = -1;

                for (int j = 0; j < frameCount; j++) {
                    if (frames[j] == -1) {
                        emptyFrame = j;
                        break;
                    }
                }

                if (emptyFrame != -1) {
                    frames[emptyFrame] = page;
                    counters[emptyFrame] = 1;

                } else {
                    int smallest = 0;

                    for (int j = 1; j < frameCount; j++) {
                        if (counters[j] < counters[smallest]) {
                            smallest = j;
                        }
                    }

                    frames[smallest] = page;
                    counters[smallest] = 1;
                }

                printLFU(frames, counters);
            }
        }

        System.out.println("Number of page faults: " + pageFaults);
    }

    // Optimal Page Replacement Algorithm
    public static void optimal(int[] pages, int frameCount) {

        int[] frames = new int[frameCount];
        int pageFaults = 0;

        for (int i = 0; i < frameCount; i++) {
            frames[i] = -1;
        }

        System.out.println("Algorithm: Optimal");
        printReferenceString(pages);
        System.out.println("Number of frames: " + frameCount);
        System.out.println("Content of frames after each page-fault:");

        for (int i = 0; i < pages.length; i++) {

            int page = pages[i];
            boolean found = false;

            for (int j = 0; j < frameCount; j++) {
                if (frames[j] == page) {
                    found = true;
                    break;
                }
            }

            if (found == false) {
                pageFaults++;

                int emptyFrame = -1;

                for (int j = 0; j < frameCount; j++) {
                    if (frames[j] == -1) {
                        emptyFrame = j;
                        break;
                    }
                }

                if (emptyFrame != -1) {
                    frames[emptyFrame] = page;

                } else {
                    int replaceIndex = 0;
                    int farthest = -1;

                    for (int j = 0; j < frameCount; j++) {

                        int next = pages.length;

                        for (int k = i + 1; k < pages.length; k++) {
                            if (pages[k] == frames[j]) {
                                next = k;
                                break;
                            }
                        }

                        if (next > farthest) {
                            farthest = next;
                            replaceIndex = j;
                        }
                    }

                    frames[replaceIndex] = page;
                }

                printFrames(frames);
            }
        }

        System.out.println("Number of page faults: " + pageFaults);
    }


    // Second Chance Page Replacement Algorithm
    public static void secondChance(int[] pages, int frameCount) {

        int[][] frames = new int[frameCount][2];

        int pointer = 0;
        int pageFaults = 0;

        for (int i = 0; i < frameCount; i++) {
            frames[i][0] = -1;
            frames[i][1] = 0;
        }

        System.out.println("Algorithm: Second Chance");
        printReferenceString(pages);
        System.out.println("Number of frames: " + frameCount);
        System.out.println("Content of frames after each page-fault:");

        for (int i = 0; i < pages.length; i++) {

            int page = pages[i];
            boolean found = false;

            for (int j = 0; j < frameCount; j++) {
                if (frames[j][0] == page) {
                    found = true;

                    frames[j][1] = 1;
                    break;
                }
            }

            if (found == false) {
                pageFaults++;

                boolean replaced = false;

                while (replaced == false) {

                    if (frames[pointer][0] == -1 ||
                            frames[pointer][1] == 0) {

                        frames[pointer][0] = page;
                        frames[pointer][1] = 0;

                        replaced = true;

                    } else {
                        frames[pointer][1] = 0;
                    }

                    pointer++;

                    if (pointer == frameCount) {
                        pointer = 0;
                    }
                }

                printSecondChance(frames, pointer);
            }
        }

        System.out.println("Number of page faults: " + pageFaults);
    }


    // LRU Approximation Using a Reference Byte
    public static void lru(int[] pages, int frameCount) {

        int[] frames = new int[frameCount];
        int[] referenceBytes = new int[frameCount];
        boolean[] referenced = new boolean[frameCount];

        int pageFaults = 0;

        for (int i = 0; i < frameCount; i++) {
            frames[i] = -1;
            referenceBytes[i] = 0;
            referenced[i] = false;
        }

        System.out.println("Algorithm: LRU Approximation Using a Reference Byte");
        printReferenceString(pages);
        System.out.println("Number of frames: " + frameCount);
        System.out.println("Content of frames after each page-fault:");

        for (int i = 0; i < pages.length; i++) {

            int page = pages[i];
            boolean found = false;

            for (int j = 0; j < frameCount; j++) {
                if (frames[j] == page) {
                    found = true;
                    referenced[j] = true;
                    break;
                }
            }

            if (found == false) {
                pageFaults++;

                int emptyFrame = -1;

                for (int j = 0; j < frameCount; j++) {
                    if (frames[j] == -1) {
                        emptyFrame = j;
                        break;
                    }
                }

                if (emptyFrame != -1) {
                    frames[emptyFrame] = page;
                    referenceBytes[emptyFrame] = 255;
                    referenced[emptyFrame] = false;

                } else {
                    int smallest = 0;

                    for (int j = 1; j < frameCount; j++) {
                        if (referenceBytes[j] < referenceBytes[smallest]) {
                            smallest = j;
                        }
                    }

                    frames[smallest] = page;
                    referenceBytes[smallest] = 255;
                    referenced[smallest] = false;
                }

                printLRU(frames, referenceBytes);
            }

            if ((i + 1) % 4 == 0) {
                shiftReferenceBytes(frames, referenceBytes, referenced);
                printLRU(frames, referenceBytes);
                System.out.println("shifting completed");
            }
        }

        if (pages.length % 4 != 0) {
            shiftReferenceBytes(frames, referenceBytes, referenced);
            printLRU(frames, referenceBytes);
            System.out.println("shifting completed");
        }

        System.out.println("Number of page faults: " + pageFaults);
    }

    public static void shiftReferenceBytes(int[] frames,
                                           int[] referenceBytes,
                                           boolean[] referenced) {

        for (int i = 0; i < frames.length; i++) {

            if (frames[i] != -1) {
                referenceBytes[i] = referenceBytes[i] >> 1;

                if (referenced[i] == true) {
                    referenceBytes[i] = referenceBytes[i] | 128;
                }

                referenced[i] = false;
            }
        }
    }

    // Print for LRU
    public static void printLRU(int[] frames, int[] referenceBytes) {

        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == -1) {
                System.out.print("|   ");
            } else {
                System.out.print("| " + frames[i] + " ");
            }
        }

        System.out.println("|");

        System.out.print("Reference bytes: ");

        for (int i = 0; i < referenceBytes.length; i++) {

            if (frames[i] == -1) {
                System.out.print("--------");
            } else {
                String binary = Integer.toBinaryString(referenceBytes[i]);

                while (binary.length() < 8) {
                    binary = "0" + binary;
                }

                System.out.print(binary);
            }

            if (i < referenceBytes.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println();
    }

    // Printing the reference string
    public static void printReferenceString(int[] pages) {
        System.out.print("Reference String: ");

        for (int i = 0; i < pages.length; i++) {
            System.out.print(pages[i]);

            if (i < pages.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println();
    }

    // Printing out the frames
    public static void printFrames(int[] frames) {

        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == -1) {
                System.out.print("|   ");
            } else {
                System.out.print("| " + frames[i] + " ");
            }
        }

        System.out.println("|");
    }


    // Print frames and their counters for LFU
    public static void printLFU(int[] frames, int[] counters) {

        for (int i = 0; i < frames.length; i++) {
            if (frames[i] == -1) {
                System.out.print("|   ");
            } else {
                System.out.print("| " + frames[i] + " ");
            }
        }

        System.out.print("|  Counters: ");

        for (int i = 0; i < counters.length; i++) {
            System.out.print(counters[i]);

            if (i < counters.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println();
    }

    // Print for Second Chance Page Replacement
    public static void printSecondChance(int[][] frames, int pointer) {

        for (int i = 0; i < frames.length; i++) {
            if (frames[i][0] == -1) {
                System.out.print("|   ");
            } else {
                System.out.print("| " + frames[i][0] + " ");
            }
        }

        System.out.println("|");

        // Print reference bits
        System.out.print("Reference bits: ");

        for (int i = 0; i < frames.length; i++) {
            System.out.print(frames[i][1]);

            if (i < frames.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println();
        System.out.println("Pointer: " + pointer);
    }
}
