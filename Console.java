package br.com.atividade6;

/**
 *
 * @author denis.vieira
 */

import java.util.Scanner;

public class Console {

    public static String readCommand(Scanner console) {
        
        String command = "";
        boolean invalid = true;

        while (invalid) {
            command = console.nextLine();

            if (!command.isEmpty()) {
                invalid = false;
            }
        }
        return command;
    }
}