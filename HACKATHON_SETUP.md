# 🎪 CodeEcho - Hackathon Setup Guide

## 🚀 Quick Start for Judges/Reviewers

### **Option 1: Demo Mode (No API Keys Required)**
```bash
# Clone the repository
git clone <your-repo-url>
cd CodeEcho

# Start in mock mode (instant demo)
cmd /c start-mock.bat

# Access the application
# http://localhost:8080/swagger-ui.html
```

### **Option 2: Full Mode (Requires API Keys)**
```bash
# 1. Copy environment template
cp .env.example .env

# 2. Edit .env with your API keys
# GITHUB_TOKEN=your-github-token
# OPENAI_API_KEY=your-openai-key

# 3. Start application
mvn spring-boot:run

# Access: http://localhost:8080/swagger-ui.html
```

## 🎯 Demo Endpoints

### **Health Check**
```
GET http://localhost:8080/api/pr-analyzer/health
```

### **Test Endpoint**
```
GET http://localhost:8080/api/pr-analyzer/test-url
```

### **Main Analysis Endpoint**
```
POST http://localhost:8080/api/pr-analyzer/analyze-and-generate
Content-Type: application/json

{
  "prUrl": "https://github.com/intacct/ia-ds-fa/pull/2360",
  "requirements": "Generate similar changes in another GLKey"
}
```

## 🛡️ Security Notes

- **No API keys included** in this repository
- **Mock mode available** for demo without external dependencies
- **Environment variables** properly configured in `.env.example`
- **All sensitive data** excluded via `.gitignore`

## 🏆 Hackathon Highlights

- **AI-Powered PR Analysis** with GPT-4o
- **Pattern Recognition** across codebases
- **Enterprise Security** with data abstraction
- **Professional UI** with structured responses
- **Mock Mode** for reliable demos

## 📞 Contact

- **Team:** CodeEcho Team
- **Email:** prashanth.kadubandi@sage.com, suresh.adiserla@sage.com
