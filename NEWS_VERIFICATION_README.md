# News Verification & Fake News Detection API

## Overview

This Spring Boot application now includes a comprehensive news verification and fake news detection platform that integrates with the existing quiz system infrastructure. The platform provides REST API endpoints for verifying news claims and detecting misinformation.

## Features

- **News Verification**: Analyze news claims and return accuracy percentages
- **Source Analysis**: Cross-reference information with multiple credible sources
- **Autocomplete Suggestions**: Dynamic search suggestions for common news topics
- **Multilingual Support**: Primarily Vietnamese with support for other languages
- **Integration with AI**: Uses existing n8n/Gemini infrastructure for advanced analysis
- **Fallback Responses**: Provides mock responses when AI services are unavailable

## API Endpoints

### 1. Verify News Content
```http
POST /news-verification/verify
Content-Type: application/json

{
    "query": "Vắc xin COVID-19 gây vô sinh",
    "language": "vi",
    "maxSources": 8
}
```

**Response:**
```json
{
    "query": "Vắc xin COVID-19 gây vô sinh",
    "accuracyPercentage": 19,
    "confirmedSources": 2,
    "totalSources": 8,
    "explanation": "Dựa trên phân tích từ 8 nguồn tin đáng tin cậy, thông tin này có độ chính xác thấp...",
    "sources": [
        {
            "title": "Bộ Y tế thông tin về vắc xin COVID-19",
            "url": "https://moh.gov.vn/tin-tuc-su-kien",
            "publisher": "Bộ Y tế",
            "supportsQuery": false,
            "credibilityScore": 95
        }
    ],
    "verdict": "FALSE"
}
```

### 2. Get Autocomplete Suggestions
```http
GET /news-verification/autocomplete?q=vắc xin
```

**Response:**
```json
{
    "suggestions": [
        "Vắc xin COVID-19 gây vô sinh",
        "Vắc xin có an toàn cho trẻ em"
    ],
    "hasMore": true
}
```

### 3. Health Check
```http
GET /news-verification/health
```

**Response:**
```
News verification service is running
```

## Verification Verdicts

The system provides six levels of verification:

- **TRUE**: 90-100% accuracy
- **MOSTLY_TRUE**: 70-89% accuracy  
- **MIXED**: 50-69% accuracy
- **MOSTLY_FALSE**: 30-49% accuracy
- **FALSE**: 0-29% accuracy
- **INSUFFICIENT_DATA**: Unable to verify

## Integration with AI Services

The platform integrates with the existing n8n workflow automation system to leverage Gemini AI for news verification:

1. **Query Processing**: Sends news claims to n8n endpoint `/webhook-test/verify-news`
2. **Source Analysis**: AI analyzes multiple sources from Google search results
3. **Credibility Assessment**: Evaluates source reliability and accuracy
4. **Response Formatting**: Returns structured verification data

## Testing

### Running the Demo
```bash
mvn compile exec:java -Dexec.mainClass="com.myproject.quizzai.demo.NewsVerificationDemo"
```

### Example Demo Output
```
=== News Verification Platform Demo ===

1. Testing the example: 'Vắc xin 19 gây vô sinh'
Query: Vắc xin 19 gây vô sinh
Accuracy: 19%
Confirmed sources: 2/8
Verdict: FALSE
Explanation: Dựa trên phân tích từ 8 nguồn tin đáng tin cậy, thông tin này có độ chính xác thấp...

Sources:
  • Bộ Y tế thông tin về vắc xin COVID-19
    Publisher: Bộ Y tế
    URL: https://moh.gov.vn/tin-tuc-su-kien
    Supports claim: No
    Credibility: 95/100
```

## Technical Implementation

### Architecture
- **Controller Layer**: `NewsVerificationController` - REST API endpoints
- **Service Layer**: `NewsVerificationService` - Business logic and AI integration
- **DTO Layer**: Request/Response data transfer objects
- **Integration**: Uses existing RestTemplate and ObjectMapper beans

### Key Classes
- `NewsVerificationController`: REST API endpoints
- `NewsVerificationService`: Core verification logic
- `NewsVerificationRequestDto`: Input data structure
- `NewsVerificationResponseDto`: Output data structure
- `AutocompleteSuggestionDto`: Autocomplete response structure

### Error Handling
- Graceful fallback when n8n service is unavailable
- Input validation using Bean Validation annotations
- Proper HTTP status codes and error responses

## Future Enhancements

1. **Real-time Source Fetching**: Direct integration with Google Search API
2. **Machine Learning**: Train models on Vietnamese news data
3. **User Feedback**: Allow users to rate verification accuracy
4. **Caching**: Cache verification results for common queries
5. **Database Storage**: Store verification history and analytics
6. **Admin Dashboard**: Monitor verification statistics and trends

## Configuration

The service uses the same n8n configuration as the existing quiz system:
- Base URL: `http://localhost:5678/webhook-test`
- Timeout: 30 seconds for API calls
- Fallback: Mock responses when service unavailable

## Development Notes

This implementation follows the existing project patterns:
- Uses the same Spring Boot configuration
- Integrates with existing dependency injection setup
- Follows the same controller/service/dto architecture
- Uses the same validation and error handling patterns