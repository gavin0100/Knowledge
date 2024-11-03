package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// Imports the Google Cloud client library
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        textToSpeechRestAPI();
    }

    public static void characterDuplicate(String input){
        String result = input.toLowerCase().chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> String.valueOf(entry.getKey()))
                .collect(Collectors.joining(", "));

        if (result.isEmpty()) {
            System.out.println("NO");
        } else {
            System.out.println(result);
        }
    }
    public static void characterDuplicateInAnotherOne(String A, String B){

        boolean areEqual = A.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toSet())
                .stream()
                .anyMatch(c -> B.indexOf(c) >= 0);

        if (areEqual) {
            System.out.println("The strings are equal.");
        } else {
            System.out.println("The strings are not equal.");
        }
    }

    public static boolean isAnagram(String A, String B){
        return A.equals(new StringBuilder(B).reverse().toString());
    }

    public static String reverseString(String A){
        return new StringBuilder(A).reverse().toString();
    }

    public static Map<Boolean, Long> countVowelsConsonants(String input){
        Map<Boolean, Long> counts =  input.toLowerCase().chars()
                .mapToObj(c -> (char) c)
                .filter(c -> Character.isLetter(c))  //.filter(Character::isLetter)
                .collect(Collectors.partitioningBy(
                        c-> "aeiou".indexOf(c) != -1, Collectors.counting()
                ));
        return counts;
    }

    public static void streamAPI(){
        //        List <Integer> numberListResult = new ArrayList<>();
//        List <Integer> numberList = Arrays.asList(10,15,20,22,24,25,26,27,28, 28,10,15,20);
//        numberList.add()

        // without using stream
//        for (int i: numberList){
//            if (i %2 == 0){
//                System.out.println(i);
//            }
//        }
//        System.out.println("====================");
//        numberList.stream().limit(7)
//                .distinct().filter(n -> n> 25).map(n -> n*100)
//                        .collect(Collectors.toList()).forEach(System.out::println);
//        System.out.println(numberList.stream().filter(n -> n < 20).count());
//        System.out.println("====================");
//        numberListResult = numberList.stream().filter(n -> n%2==0).collect(Collectors.toList());
//        System.out.println(numberListResult.toString());
//        numberList.stream().filter(n -> n%2!= 0).forEach( n -> {n = n + 100;
//            System.out.println(n);});
//
//        List<String> oldString = Arrays.asList("nguyen van a", "hihi", "tran quoc tuan");
//        List<String> newString = new ArrayList<>();
//
//        newString = oldString.stream()
//                .map(m -> m.toUpperCase())
//                .collect(Collectors.toList())
//                .stream()
//                .filter(n -> n.length() < 7)
//                .collect(Collectors.toList());
//        System.out.println(newString);
//        oldString.stream()
//                .map(n -> n.length())
//                .forEach(System.out::println);
//        System.out.println(oldString);
//        newString = oldString.stream()
//                .filter(n -> n.length() > 5)
//                .map(n -> n.toUpperCase() + "hihi")
//                .collect(Collectors.toList());
//        System.out.println(newString);
//        System.out.println("====================");
//        List<Integer> lst1 = Arrays.asList(1,2);
//        List<Integer> lst2 = Arrays.asList(3,4);
//        List<Integer> lst3 = Arrays.asList(5,6);
//
//        List<List<Integer>> finalResult = Arrays.asList(lst1, lst2, lst3);
//        List<Integer> newFinalResult = new ArrayList<>();
//        newFinalResult = finalResult.stream()
//                .flatMap(x -> x.stream().filter(n -> n%2!= 0))
//                .collect(Collectors.toList());
//        System.out.println(newFinalResult);
//        System.out.println("====================");
//        List<Integer> numberListMin = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
//        Optional <Integer> min = numberListMin.stream()
//                .min((val1, val2) -> {return val1.compareTo(val2);});
//        System.out.println(min.get());

        characterDuplicate("YourInputStringHere");
        characterDuplicateInAnotherOne("123", "YourSecondString");
        System.out.println(isAnagram("123", "321"));
        System.out.println(reverseString("123"));
        System.out.println(countVowelsConsonants("vovanduc123"));
    }

    public static void googleTextToSpeech() throws IOException {
        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText("Xe 49L1 - 19193 xuống hàng!").build();

            // Build the voice request, select the language code ("en-US") and the ssml voice gender
            // ("neutral")
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("vi-VN")
                            .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                            .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            // Perform the text-to-speech request on the text input with the selected voice parameters and
            // audio file type
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();
            ByteString audioContentsToBase64 = audioContents;

            // Write the response to the output file.
            try (OutputStream out = new FileOutputStream("output.mp3")) {
                out.write(audioContents.toByteArray());
                System.out.println("Audio content written to file \"output.mp3\"");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            byte[] audioBytes = audioContentsToBase64.toByteArray();
            String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
            System.out.println("Base64 Audio: " + base64Audio);
        }
    }
    public static void textToSpeechRestAPI(){
        try {
// Create the JSON request body
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, String> input = new HashMap<>();
            input.put("text", "Xe 49L1 19 1 93 xuống hàng!");
            requestBody.put("input", input);

            Map<String, String> voice = new HashMap<>();
            voice.put("languageCode", "vi-VN");
            voice.put("name", "vi-VN-Neural2-A");
            requestBody.put("voice", voice);

            Map<String, String> audioConfig = new HashMap<>();
            audioConfig.put("audioEncoding", "MP3");
            requestBody.put("audioConfig", audioConfig);

// Set up the headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth("ya29.c.c0A");

// Create the request entity
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

// Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

// Make the POST request
            String response = restTemplate.postForObject("https://texttospeech.googleapis.com/v1/text:synthesize", request, String.class);

// Save the response to a file
            System.out.println(response);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);
            System.out.println(responseMap.get("audioContent"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
