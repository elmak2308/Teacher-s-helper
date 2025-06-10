import openai
import requests 
api_key = "sk-EcxgoVSImbFwlmUyyoXz5fiqGRPvr7KdwEVYfPcimNZi2l4uQX9rZZag5EVH"

input = {
"is_sync": True, 
 "messages": [
  {
   "role": "user",
   "content": "Напиши'!'"
  }
 ]
}

headers = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    'Authorization': f'Bearer {api_key}'
 }

url_endpoint = "https://api.gen-api.ru/api/v1/networks/deepseek-v3"
response = requests.post(url_endpoint, json=input, headers=headers)
print(response.json())