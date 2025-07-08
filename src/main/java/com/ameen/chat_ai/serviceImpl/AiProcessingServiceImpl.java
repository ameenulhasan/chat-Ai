package com.ameen.chat_ai.serviceImpl;

import com.ameen.chat_ai.service.AiProcessingService;
import org.springframework.stereotype.Service;

@Service
public class AiProcessingServiceImpl implements AiProcessingService {

    @Override
    public String processHomeCommand(String command) {
        // Convert command to lowercase for better matching
        String cmd = command.toLowerCase();
        // Smart home actions (can be expanded with real IoT integration)
        switch (cmd) {
            case "turn on light":
                return "Light is now ON!";
            case "turn off light":
                return "Light is now OFF!";
            case "open door":
                return "Door is now OPEN!";
            case "close door":
                return "Door is now CLOSED!";
            case "temperature":
                return "Current temperature is 24°C.";
            case "play music":
                return "Playing your favorite playlist!";
            default:
                return "I don't understand the command: " + command;
        }
    }

}
