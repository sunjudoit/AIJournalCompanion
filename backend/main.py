from fastapi import FastAPI
from pydantic import BaseModel
import httpx
import json
import re

app = FastAPI()

class JournalRequest(BaseModel):
    content: str

class JournalResponse(BaseModel):
    emotion: str
    advice: str

OLLAMA_URL ="http://localhost:11434/api/generate"

@app.post("/analyze")
async def analyze_content(request: JournalRequest):
    
    prompt = f"""
Read the diary entry and write an appropriate one-line piece of advice.
Also, analyze the emotion in the diary entry as exactly one of the following: Happiness, Sadness, Gratitude, Anxiety, Anger,Tiredness, Surprise.
Do not include any descriptions or additional text; use only the JSON object.

Journal: {request.content}

Respond in this exact format:
{{"emotion": "EMOTION_HERE", "advice": "ONE_LINE_ADVICE_HERE"}}
"""

    async with httpx.AsyncClient(timeout=60.0) as client:
        response = await client.post(
            OLLAMA_URL,
            json={
                "model":"gemma:2b",
                "prompt": prompt,
                "stream": False
            }
        )
    result = response.json()["response"]
    match = re.search(r'\{.*\}', result, re.DOTALL)

    #Jason to dictionary
    if match :
        data = json.loads(match.group())
        return JournalResponse(
            emotion = data.get("emotion","Unknown"),
            advice = data.get("advice","No advice")

        )
    return JournalResponse(
        emotion = "Could not analyze.",
        advice = "Could not analyze."
    )