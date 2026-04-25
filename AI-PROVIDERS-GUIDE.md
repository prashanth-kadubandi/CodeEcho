# AI Providers Guide for CodeEcho

## 🤖 Supported AI Providers

Your application now supports multiple AI providers for code generation:

### 1. **GitHub Copilot** (Recommended for Proprietary Code)
- **Best for**: Enterprise/proprietary code analysis
- **Advantages**: 
  - ✅ Code-specific training
  - ✅ Better enterprise compliance
  - ✅ Uses your existing GitHub token
  - ✅ Designed for code generation
- **Model**: GPT-4 (GitHub's implementation)

### 2. **OpenAI ChatGPT**
- **Best for**: General use, public projects
- **Advantages**:
  - ✅ Well-documented API
  - ✅ Reliable service
  - ✅ Good for demos
- **Model**: GPT-3.5-turbo (cost-effective)

### 3. **Azure OpenAI** (Enterprise)
- **Best for**: Enterprise with Azure infrastructure
- **Advantages**:
  - ✅ Enterprise compliance
  - ✅ Data residency control
  - ✅ Integration with Azure services
- **Model**: GPT-3.5-turbo or GPT-4

## 🔧 Configuration

### Option 1: GitHub Copilot (Recommended)
```bash
# In your .env file:
AI_PROVIDER=github
GITHUB_TOKEN=your-github-token  # Same token used for GitHub API
```

### Option 2: OpenAI
```bash
# In your .env file:
AI_PROVIDER=openai
OPENAI_API_KEY=your-openai-key
```

### Option 3: Azure OpenAI
```bash
# In your .env file:
AI_PROVIDER=azure
AZURE_OPENAI_KEY=your-azure-key
AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com
```

## 🚀 Getting Started with GitHub Copilot

### Step 1: Enable GitHub Copilot API Access
1. **Go to GitHub Settings**: https://github.com/settings/copilot
2. **Enable Copilot** if not already enabled
3. **Check API access** (may require GitHub Copilot Business/Enterprise)

### Step 2: Update Configuration
```bash
# Edit your .env file:
AI_PROVIDER=github
```

### Step 3: Test the Application
```bash
mvn clean compile
mvn spring-boot:run
```

## 🎯 For Proprietary Code Analysis

### Why GitHub Copilot is Better:

1. **Code-Specific Training**: Trained specifically on code repositories
2. **Enterprise Agreements**: GitHub has proper enterprise licensing
3. **Same Token**: Uses your existing GitHub token (no additional API keys)
4. **Code Understanding**: Better at understanding programming patterns
5. **Compliance**: Better for corporate/proprietary code analysis

### Enterprise Setup:
1. **GitHub Copilot Business**: Required for API access
2. **SAML/SSO Integration**: Works with your organization's auth
3. **Audit Logs**: Better tracking for enterprise compliance
4. **Data Handling**: GitHub's enterprise data handling policies

## 🔄 Fallback Strategy

The application includes automatic fallback:
1. **Primary**: Try configured provider (GitHub/Azure)
2. **Fallback**: If primary fails, fall back to OpenAI
3. **Mock Mode**: Always available as final fallback

## 🎭 Demo Recommendations

### For Hackathon/Public Demo:
- Use **Mock Mode** for reliability
- Show **GitHub Copilot** integration as a feature
- Mention **Enterprise compliance** benefits

### For Enterprise Demo:
- Use **GitHub Copilot** with real proprietary code
- Highlight **compliance and security** features
- Show **Azure integration** if applicable

## 🛡️ Security Considerations

### GitHub Copilot:
- ✅ Uses existing GitHub enterprise agreements
- ✅ Respects organization SAML/SSO policies
- ✅ Code stays within GitHub ecosystem
- ✅ Enterprise audit trails

### OpenAI:
- ⚠️ Third-party service
- ⚠️ Data may be used for training (check terms)
- ⚠️ Less suitable for proprietary code

### Azure OpenAI:
- ✅ Enterprise-grade security
- ✅ Data residency control
- ✅ No data used for training
- ✅ Full compliance features

## 🚀 Quick Start Commands

```bash
# Test with GitHub Copilot
echo "AI_PROVIDER=github" >> .env
mvn spring-boot:run

# Test with OpenAI (if you have credits)
echo "AI_PROVIDER=openai" >> .env
mvn spring-boot:run

# Safe demo mode (always works)
cmd /c start-mock.bat
```

## 📋 Troubleshooting

### GitHub Copilot Issues:
- Ensure you have Copilot Business/Enterprise
- Check if API access is enabled
- Verify GitHub token has proper permissions

### OpenAI Issues:
- Check billing/quota limits
- Verify API key is valid
- Try gpt-3.5-turbo instead of gpt-4

### General Issues:
- Use mock mode for reliable demos
- Check application logs for detailed errors
- Verify .env file is properly loaded
