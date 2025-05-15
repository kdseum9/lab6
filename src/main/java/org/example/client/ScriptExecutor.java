package org.example.client;

import org.example.common.Request;
import org.example.common.Response;
import org.example.common.model.Ticket;
import org.example.client.TicketInput;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.util.*;

public class ScriptExecutor {

    private final Set<String> executedScripts = new HashSet<>();

    public ScriptExecutor() {
    }

    public void executeScript(String filePath, SocketChannel socket) {
        if (executedScripts.contains(filePath)) {
            System.out.println("Recursion detected. Skipping script: " + filePath);
            return;
        }

        executedScripts.add(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String command = line.split(" ")[0];
                System.out.println("Executing command from script: " + command);

                Request request = null;
                switch (command) {
                    case "add":
                    case "add_if_max":
                        Ticket ticket = TicketInput.generateTicketFromScript(reader);
                        request = new Request(command, new String[0], ticket);
                        break;
                    case "update":
                        String idLine = reader.readLine();
                        long id = Long.parseLong(idLine.trim());
                        Ticket updatedTicket = TicketInput.generateTicketFromScript(reader);
                        request = new Request(command, new String[]{String.valueOf(id)}, updatedTicket);
                        break;
                    case "remove_by_id":
                        String removeId = reader.readLine().trim();
                        request = new Request(command, new String[]{removeId}, null);
                        break;
                    case "remove_greater":
                    case "remove_lower":
                        Ticket compareTicket = TicketInput.generateTicketFromScript(reader);
                        request = new Request(command, new String[0], compareTicket);
                        break;
                    case "execute_script":
                        String nestedPath = reader.readLine().trim();
                        executeScript(nestedPath, socket);
                        continue;
                    default:
                        request = new Request(command, new String[0], null);
                }

                if (request != null) {
                    Client.sendRequest(socket, request);
                    Response response = Client.receiveResponse(socket);
                    System.out.println("Server response: " + response.getMessage());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error executing script: " + e.getMessage());
        }

        executedScripts.remove(filePath);
    }
}
