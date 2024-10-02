package com.narveri.geminifinal;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GeminiFinal {

    public static void main(String[] args) throws IOException {
        
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        String projectId = "my-project-id";
        String location = "us-central1";
        String modelName = "gemini-1.5-flash-002";
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Lütfen soruyu girin: ");
        String textPrompt = scanner.nextLine();

        String output = textInput(projectId, location, modelName, textPrompt);
        System.out.println("Cevap: " + output);
        
        scanner.close();  
    }

    // Passes the provided text input to the Gemini model and returns the text-only response.
    // For the specified textPrompt, the model returns a list of possible store names.
    public static String textInput(
            String projectId, String location, String modelName, String textPrompt) throws IOException {
        // Initialize client that will be used to send requests. This client only needs
        // to be created once, and can be reused for multiple requests.
        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);

            GenerateContentResponse response = model.generateContent(textPrompt);
            String output = ResponseHandler.getText(response);
            return output;
        }
    }
}

