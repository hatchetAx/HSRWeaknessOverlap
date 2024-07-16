import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class WeaknessOverlap {

    //TODO: need to include weekly bosses

    static String[][] weaknesses;
    static String[] enemyNames;
    static String[] temp;
    static String[] types;
    static int count2 = 0;
    static int occurs = 0;
    static File enemies = new File("enemies.txt");
    static File weakness = new File("weakness.txt");

    static int dividePos = 0;
    static int highestOccur;
    static int lowestOccur;
    static String highestWeak = "";
    static String lowestWeak = "";
    static String answer = "";


    //for displaying, reset is just used as a stop point for the color that was used before it
    static String RESET = "\u001B[0m";
    static String RED = "\u001B[31m";
    static String GREEN = "\u001B[32m";

    //updates enemies.txt and weakness.txt
    public static void updateFiles() throws IOException {
        Runtime.getRuntime().exec("node index.js");
    }

    //cleaning up the text, and puts the data into enemyNames and temp (for weaknesses)
    public static void regex() throws FileNotFoundException {
        Scanner scan = new Scanner(enemies);
        Scanner scan2 = new Scanner(weakness);


        //for enemy names
        String data = scan.nextLine();
            
        //i dont understand regex

        data = data.replaceAll("\"", "");
        data = data.replaceAll("\\[", "");
        data = data.replaceAll("\\]", "");

        //for weakness types of enemies
        String data2 = scan2.nextLine();
            
        data2 = data2.replaceAll("\"", "");
        data2 = data2.replaceAll("\\[", "");
        data2 = data2.replaceAll("\\]", "");
        

        enemyNames = data.split(",");

        temp = data2.split(",");

        scan.close();
        scan2.close();
    }
            

    public static String[][] parseFiles() throws FileNotFoundException {

        regex();

        //count and temp.length are basically the same thing
        weaknesses = new String[temp.length][];
        
        //set each rows length
        for (int y = 0; y < temp.length; y++) {
            count2 = 0;
                for (int i = 0; i < temp[y].length(); i++) {
                    //iterates thru the string at types[x]
                    if (temp[y].charAt(i) == ' ') {
                        count2++;
                    }
            }
            weaknesses[y] = new String[count2 + 1];
        }


        //put types into 2d array
        for (int y = 0; y < temp.length; y++) {

            types = temp[y].split(" ");

            System.arraycopy(types, 0, weaknesses[y], 0, types.length);
        }


        return weaknesses;

    
    }


    //overloaded method, called if answer in main is equal to normal/elite
    public static String[][] parseFiles(String enemyType) throws FileNotFoundException {

        regex();

        //get the position of where normal enemies stop and elite starts
        for (int i = 0; i < enemyNames.length; i++) {
            if (enemyNames[i].equalsIgnoreCase("The Past Present and Eternal Show")) {
                dividePos = i;
                i = enemyNames.length;
            }
        }

        //changes the row length depending on how many normal/elite enemies there are
        if (enemyType.equalsIgnoreCase("normal")) {
            weaknesses = new String[dividePos][];
        } else {
            weaknesses = new String[temp.length - dividePos][];
        }
        
        
        if (enemyType.equalsIgnoreCase("normal")) {

            //set each rows length by seeing how many weaknesses are at temp[y]
            for (int y = 0; y < weaknesses.length; y++) {
                count2 = 0;
                    for (int i = 0; i < temp[y].length(); i++) {
                        //iterates thru the string at types[x]
                        if (temp[y].charAt(i) == ' ') {
                            count2++;
                        }
                }
                weaknesses[y] = new String[count2 + 1];
            } 
            //put types into 2d array
            for (int y = 0; y < dividePos; y++) {

                types = temp[y].split(" ");

                System.arraycopy(types, 0, weaknesses[y], 0, types.length);
            }   
            
        } else if (enemyType.equalsIgnoreCase("elite")) {

            //set each rows length, but changes temp position to where elite enemies start with dividePos
            for (int y = 0; y < weaknesses.length; y++) {
                count2 = 0;
                    for (int i = 0; i < temp[y + dividePos].length(); i++) {
                        //iterates thru the string at types[x]
                        if (temp[y + dividePos].charAt(i) == ' ') {
                            count2++;
                        }
                }
                weaknesses[y] = new String[count2 + 1];
            } 

            //copies type of weakness, but starts further up to where elites start
            for (int y = 0; y < weaknesses.length; y++) {

                types = temp[y + dividePos].split(" ");
                System.arraycopy(types, 0, weaknesses[y], 0, types.length);
            }
        }


        return weaknesses;

    
    }


    //weakness overlap
    //start with one element, then count how many other elements reoccur with it (in weaknesses array)
    //starting element is variable weak and remains the same, weakCompared will be all other elements
    public static int overlaps(String weak, String weakCompared) throws FileNotFoundException { 

        occurs = 0;
        
        for (int y = 0; y < weaknesses.length - 1; y++) {

            for (String theWeakness : weaknesses[y]) {
                if (theWeakness.equals(weak)) {

                    for (String weakness1 : weaknesses[y]) {
                        if (!weakness1.equals(weakCompared)) {
                        } else {
                            occurs++;
                        }
                    }

                }

            }
        }
        return occurs;
    }

    //gets highest occuring and lowest occuring weaknesses found with parameter String weakness
    public static void highestAndLowestOverlaps(ArrayList<String> otherTypes, String weakness) throws FileNotFoundException {
        highestWeak = "";
        lowestWeak = "";
        
        highestOccur = overlaps(weakness, otherTypes.get(0));
        lowestOccur = overlaps(weakness, otherTypes.get(0));
        //gets highest and lowest occurence
        for (int i = 0; i < 6; i++) {
            //min
            if (overlaps(weakness, otherTypes.get(i)) < lowestOccur) {
                lowestOccur = overlaps(weakness, otherTypes.get(i));
                lowestWeak = otherTypes.get(i);
            }   else if (overlaps(weakness, otherTypes.get(i)) == lowestOccur) {
                lowestWeak += otherTypes.get(i) + " ";
            }

            //max
            if (overlaps(weakness, otherTypes.get(i)) > highestOccur) {
                highestOccur = overlaps(weakness, otherTypes.get(i));
                highestWeak = otherTypes.get(i);
            } else if (overlaps(weakness, otherTypes.get(i)) == highestOccur) {
                highestWeak += otherTypes.get(i) + " ";
            }
        }
    }

    //formatting
    //prints the elements and number of times they occur with each other
    //also prints highest and lowest occuring element that occurs with parameter weakness
    public static void formatOverlap(String weakness) throws FileNotFoundException {
        ArrayList<String> otherTypes = new ArrayList<>();
        

        switch(weakness) {
            case "Physical" -> Collections.addAll(otherTypes, "Fire", "Ice", "Lightning", "Wind", "Quantum", "Imaginary");
            case "Fire" -> Collections.addAll(otherTypes, "Physical", "Ice", "Lightning", "Wind", "Quantum", "Imaginary");
            case "Ice" -> Collections.addAll(otherTypes, "Fire", "Physical", "Lightning", "Wind", "Quantum", "Imaginary");
            case "Lightning" -> Collections.addAll(otherTypes, "Fire", "Ice", "Physical", "Wind", "Quantum", "Imaginary");
            case "Wind" -> Collections.addAll(otherTypes, "Fire", "Ice", "Lightning", "Physical", "Quantum", "Imaginary");
            case "Quantum" -> Collections.addAll(otherTypes, "Fire", "Ice", "Lightning", "Wind", "Physical", "Imaginary");
            case "Imaginary" -> Collections.addAll(otherTypes, "Fire", "Ice", "Lightning", "Wind", "Quantum", "Physical");

        }

        highestAndLowestOverlaps(otherTypes, weakness);

        //prints weaknesses and how often they occur with another weakness
        for (int i = 0; i < 6; i++) {
            //since imaginary is two letters (to differentiate from Ice), have to check if it's weakness or compared weakness to print accordingly
            if (weakness.equals("Imaginary")) {
                System.out.print("Im+" + otherTypes.get(i).charAt(0) + ": " + overlaps(weakness, otherTypes.get(i)) + " ");
            } else if (otherTypes.get(i).equals("Imaginary")) {
                System.out.print(weakness.charAt(0) + "+Im: " + overlaps(weakness, otherTypes.get(i)) + " ");
            } else {
                System.out.print(weakness.charAt(0) + "+" + otherTypes.get(i).charAt(0) + ": " + overlaps(weakness, otherTypes.get(i)) + " ");
            }
            
        }

        //prints highest and lowest occurring weakness
        System.out.println(GREEN + "\nHighest: " + highestWeak + RESET + RED + " Lowest: " + lowestWeak + RESET);

        otherTypes.clear();
    }




	public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner userInput = new Scanner(System.in);
        

        //uncomment to get updated files
        /*
        System.out.println("Do you want to update the files? Y/N");
        answer = userInput.nextLine();

        if (answer.equalsIgnoreCase("y")) {
            updateFiles();
            System.out.println("Files updated.");
        }
        */
        
        System.out.println("normal or elite enemies? (default is all)");
        answer = userInput.nextLine();

        if (answer.length() > 0 && (answer.equalsIgnoreCase("normal") || answer.equalsIgnoreCase("elite"))) {
            weaknesses = WeaknessOverlap.parseFiles(answer);
        } else {
            weaknesses = WeaknessOverlap.parseFiles();
        }
        

        
        System.out.println("Which weakness do you want to check? (Physical, Fire, Ice, Lightning, Wind, Quantum, Imaginary, All)\nAnswer is case sensitive");
        answer = userInput.nextLine();

        if (answer.equals("All")) {
            formatOverlap("Physical");
            formatOverlap("Fire");
            formatOverlap("Ice");
            formatOverlap("Lightning");
            formatOverlap("Wind");
            formatOverlap("Quantum");
            formatOverlap("Imaginary");
        } else {
            formatOverlap(answer);
        }

        userInput.close();

	}

}