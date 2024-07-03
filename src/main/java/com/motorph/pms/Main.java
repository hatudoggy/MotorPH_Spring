package com.motorph.pms;

import com.motorph.pms.PmsApplication;
import org.springframework.boot.SpringApplication;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    private static Process frontendProcess;

    public static void main(String[] args) {
        // Start the backend Spring Boot application
        new Thread(() -> SpringApplication.run(PmsApplication.class, args)).start();

        // Start the frontend React application
        startFrontend();

        // Wait a bit for the frontend to start
        try {
            Thread.sleep(5000); // Wait for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Open the frontend React application in the default web browser
        openFrontend();

        // Add shutdown hook to stop the frontend when Java application stops
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (frontendProcess != null) {
                frontendProcess.destroy();
            }
        }));
    }

    private static void startFrontend() {
        String npmCommand = "npm.cmd"; // Adjust for Windows
        ProcessBuilder processBuilder = new ProcessBuilder(npmCommand, "run", "dev");
        processBuilder.directory(new java.io.File("frontend"));
        processBuilder.redirectErrorStream(true);

        try {
            frontendProcess = processBuilder.start();

            // Read the output in a separate thread
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(frontendProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void openFrontend() {
        if (Desktop.isDesktopSupported()) {
            int initialPort = 3000; // Starting port number to check
            int maxAttempts = 10; // Maximum number of attempts to check ports

            for (int attempt = 0; attempt < maxAttempts; attempt++) {
                int portNumber = initialPort + attempt;
                try {
                    URI uri = new URI("http://localhost:" + portNumber);
                    Desktop.getDesktop().browse(uri);
                    return; // Exit the method if successful
                } catch (IOException | URISyntaxException e) {
                    // Log the exception or handle it as needed
                    System.out.println("Failed to open browser on port " + portNumber + ": " + e.getMessage());
                }
            }

            // If all attempts fail
            System.out.println("Failed to open browser automatically after " + maxAttempts + " attempts.");
        } else {
            System.out.println("Desktop is not supported, cannot open browser automatically.");
        }
    }
}