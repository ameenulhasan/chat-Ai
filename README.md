# # Chat-Ai 🤖

A Java Spring Boot-based AI Chat Service using OpenAI API (ChatGPT).  
Provides RESTful APIs to interact with OpenAI models, built with clean architecture and secret management.

## 🔧 Tech Stack

- 🧠 OpenAI GPT (Chat)
- ☕ Java 17
- 🌱 Spring Boot
- 🔐 JWT Auth
- 🗃️ MySQL / MongoDB (optional)
- 📦 Maven

## 🚀 Features

- ✅ Chat with OpenAI (GPT)
- 🔑 Secure with JWT Authentication
- 📄 Clean DTO-based request/response model
- 🌐 REST API ready for integration
- 📁 Secret keys managed outside code (via environment variables)

## 🛠️ How to Run

```bash
# Set your environment variable
export OPENAI_API_KEY=your-secret-key

# Then run the app
./mvnw spring-boot:run

