package com.example.persona.Interfaces;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;

public interface replybot {
    void callback(DetectIntentResponse returnResponse);
}
