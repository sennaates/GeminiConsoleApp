package com.narveri.geminifinal;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GeminiFinal {

    public static void main(String[] args) throws IOException {

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setOut(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        String projectId = "your-project-ide";
        String location = "us-central1";
        String modelName = "gemini-1.5-flash-002";
        Scanner scanner = new Scanner(System.in);
        String textPrompt = "";
        List<String> conversationHistory = new ArrayList<>(); // Geçmişi saklamak için liste

        while (true) {
            boolean stringControl = false;
            while (!stringControl) {
                System.out.print("Lütfen soruyu girin (çıkış için \"exit\" yazınız): ");
                textPrompt = scanner.nextLine();

                if (textPrompt.isEmpty()) {
                    System.err.println("Lütfen boş bırakmayınız.");
                } else {
                    stringControl = true;
                }
            }

            if ("exit".equals(textPrompt)) {
                System.out.println("Çıkış yapılıyor...");
                break;
            }

            // Geçmişi güncelle
            conversationHistory.add("Merhaba aşağıdaki konuşmayı seninle gerçekleştirdik. "
                    + "Diyaloğu oku, anla ve en son sorduğum soruya bu bağlama göre "
                    + "uyumlu ve mantıklı bir cevap ver.");
            conversationHistory.add("Soru: " + textPrompt);

            // Önceki soruları ve cevapları prompt olarak ekle
            StringBuilder previousPrompts = new StringBuilder();
            for (String entry : conversationHistory) {
                previousPrompts.append(entry).append("\n");
            }
            String output = textInput(projectId, location, modelName, previousPrompts.toString() + "Soru: " + textPrompt);
            // Geçmişi güncelle
            conversationHistory.add("Cevap: " + output);

            int boslukSayaci = 0;
            for (int i = 0; i < output.length(); i++) {

                System.out.print(output.charAt(i));
                if (output.charAt(i) == ' ') {
                    boslukSayaci++;

                }
                if (boslukSayaci == 18) {
                    System.out.println();
                    boslukSayaci = 0;
                }
            }
        }

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

