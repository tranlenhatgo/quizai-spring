# Testing the News Verification API

## Sample API Calls

Once the Spring Boot application is running, you can test the news verification endpoints using curl:

### 1. Verify News Content
```bash
curl -X POST http://localhost:8080/news-verification/verify \
  -H "Content-Type: application/json" \
  -d '{
    "query": "Vắc xin COVID-19 gây vô sinh",
    "language": "vi",
    "maxSources": 8
  }'
```

**Expected Response:**
```json
{
  "query": "Vắc xin COVID-19 gây vô sinh",
  "accuracyPercentage": 19,
  "confirmedSources": 2,
  "totalSources": 8,
  "explanation": "Dựa trên phân tích từ 8 nguồn tin đáng tin cậy, thông tin này có độ chính xác thấp. Các nghiên cứu khoa học hiện tại không hỗ trợ tuyên bố này. Vắc xin COVID-19 đã được thử nghiệm kỹ lưỡng và được các tổ chức y tế uy tín khuyến nghị.",
  "sources": [
    {
      "title": "Bộ Y tế thông tin về vắc xin COVID-19",
      "url": "https://moh.gov.vn/tin-tuc-su-kien",
      "publisher": "Bộ Y tế",
      "supportsQuery": false,
      "credibilityScore": 95
    },
    {
      "title": "WHO - Thông tin chính thức về vắc xin",
      "url": "https://www.who.int/vietnam",
      "publisher": "WHO Vietnam",
      "supportsQuery": false,
      "credibilityScore": 98
    }
  ],
  "verdict": "FALSE"
}
```

### 2. Get Autocomplete Suggestions
```bash
curl "http://localhost:8080/news-verification/autocomplete?q=vắc%20xin"
```

**Expected Response:**
```json
{
  "suggestions": [
    "Vắc xin COVID-19 gây vô sinh"
  ],
  "hasMore": false
}
```

### 3. Health Check
```bash
curl http://localhost:8080/news-verification/health
```

**Expected Response:**
```
News verification service is running
```

## Running the Application

To run the full Spring Boot application, you'll need to configure Firebase credentials first:

1. **Set up Firebase Service Account Key:**
   - Download your Firebase service account key JSON file
   - Place it in the resources directory or update the path in `FirebaseConfiguration.java`

2. **Start the Application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Access Swagger UI:**
   - Open http://localhost:8080/swagger-ui.html
   - You'll see the news verification endpoints documented alongside the existing quiz endpoints

## Frontend Integration

For a React frontend, you would integrate these endpoints like this:

```javascript
// News verification function
async function verifyNews(query) {
  const response = await fetch('/news-verification/verify', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      query: query,
      language: 'vi',
      maxSources: 8
    })
  });
  return await response.json();
}

// Autocomplete function
async function getAutocompleteSuggestions(input) {
  const response = await fetch(`/news-verification/autocomplete?q=${encodeURIComponent(input)}`);
  return await response.json();
}

// Usage in React component
const [verificationResult, setVerificationResult] = useState(null);
const [suggestions, setSuggestions] = useState([]);

const handleSubmit = async (query) => {
  const result = await verifyNews(query);
  setVerificationResult(result);
};

const handleInputChange = async (input) => {
  if (input.length > 2) {
    const suggestions = await getAutocompleteSuggestions(input);
    setSuggestions(suggestions.suggestions);
  }
};
```

This implementation provides exactly what was requested in the problem statement - a backend API for news verification with accuracy percentages, source analysis, and autocomplete functionality.