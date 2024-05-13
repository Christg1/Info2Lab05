package Lab5;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;

public class ZuulGame {
	
    private BufferedReader inputReader;
    private PrintWriter outputWriter;
    private Map<String, List<String>> exits;
    private HashMap<String, String> items;
    private String currentRoom;

    public ZuulGame(BufferedReader inputReader, PrintWriter outputWriter) {
        this.inputReader = inputReader;
        this.outputWriter = outputWriter;
        exits = new HashMap<>();
        items = new HashMap<>();
        currentRoom = "hallway"; // Initialize current room
        createRooms();
    }
   
    private void createRooms() {
        exits.put("hallway", Arrays.asList("kitchen","laboratory"));
        exits.put("kitchen", Arrays.asList("hallway","laboratory"));
        exits.put("laboratory", Arrays.asList("hallway","kitchen"));
        items.put("key", "A shiny key");
        items.put("map", "A map of the mansion");
    }

    private void printWelcome() {
        outputWriter.println("Welcome to Zuul!");
        outputWriter.println("Zuul is a simple text-based adventure game.");
        outputWriter.println("Type 'help' if you need help.");
        outputWriter.println();
        outputWriter.println("You are in the hallway.");
        outputWriter.println("Exits: kitchen");
    }

    private void printHelp() {
        outputWriter.println("You are lost. You are alone.");
        outputWriter.println("You wander around the mansion.");
        outputWriter.println("Your command words are:");
        outputWriter.println("   go quit help");
    }

    public String processCommand(String command) {
        System.out.println("Received command: " + command); // Debug statement to see what I was actually sending when trying to play the zuulgame
        //using the ZuulServer and the ChatClient class
        
        String[] words = command.split(" ");
        if (words[0].equals("help")) {
            printHelp();
        } else if (words[0].equals("quit")) {
            return "quit";
        } else if (words[0].equals("go")) {
            if (words.length < 2) {
                return "Please specify a direction.";
            }
            String direction = words[1];
            System.out.println("Direction: " + direction); // Debug statement since i had a problem with the directions
            return goRoom(direction);
        } else {
            return "I don't know what you mean...";
        }
        return "";
    }


    private String goRoom(String direction) {
        System.out.println("Direction provided by player: " + direction);
        List<String> possibleExits = exits.get(currentRoom);
        System.out.println("Possible exits from " + currentRoom + ": " + possibleExits);
        if (possibleExits == null || !possibleExits.contains(direction)) {
            return "There is no door in that direction!";
        } else {
            // Update current room based on the direction provided
            currentRoom = direction;
            return "You go to the " + currentRoom + ".";
        }
    }




    public void play() {
        printWelcome();
        Scanner scanner = new Scanner(inputReader);
        boolean finished = false;
        while (!finished) {
            outputWriter.print("> ");
            String command = scanner.nextLine();
            String response = processCommand(command);
            outputWriter.println(response);
            if (response.equals("quit")) {
                finished = true;
            }
        }
        scanner.close();
    }
}
