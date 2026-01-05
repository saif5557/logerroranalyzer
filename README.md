# Log Error Analyzer

A comprehensive Spring Boot application designed to parse log files, extract error codes, and match them with predefined error descriptions and solutions. This tool provides intelligent error analysis with a user-friendly web interface for efficient troubleshooting and system monitoring.

## 🚀 Features

### Core Functionality
- **Smart Log Parsing**: Automatically detects and parses various log formats
- **Error Code Matching**: Intelligent matching of errors with predefined solutions
- **Multiple Input Methods**: Support for file upload and direct text input
- **Real-time Analysis**: Instant error analysis and reporting
- **Export Capabilities**: Export results to CSV and HTML formats

### Advanced Features
- **Pattern Recognition**: Uses regex patterns and keyword matching for error detection
- **Severity Classification**: Automatic severity assessment (Critical, High, Medium, Low)
- **Component Identification**: Identifies system components from log entries
- **Statistical Dashboard**: Real-time statistics and error distribution
- **Filtering & Search**: Advanced filtering options for large log files
- **Responsive Design**: Mobile-friendly web interface

### Error Management
- **Predefined Error Codes**: Comprehensive database of common system errors
- **Custom Error Definitions**: Add, edit, and manage custom error codes
- **Solution Repository**: Detailed solutions and troubleshooting steps
- **Error Type Classification**: Categorizes errors by type (System, Hardware, Network, etc.)

## 🛠 Technology Stack

- **Backend**: Spring Boot 2.7.14, Java 11
- **Database**: H2 (In-memory) / Configurable for PostgreSQL, MySQL
- **Frontend**: Thymeleaf, Bootstrap 5, jQuery, DataTables
- **Build Tool**: Maven
- **Additional Libraries**: Jackson for JSON processing, Apache Commons

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- Git (for cloning the repository)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/log-error-analyzer.git
cd log-error-analyzer
```

### 2. Build the Application
```bash
mvn clean compile
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

### 4. Access the Application
Open your browser and navigate to: `http://localhost:8080`

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── loganalyzer/
│   │           ├── LogErrorAnalyzerApplication.java
│   │           ├── config/
│   │           │   ├── DatabaseConfig.java
│   │           │   └── WebConfig.java
│   │           ├── controller/
│   │           │   ├── LogAnalysisController.java
│   │           │   ├── ErrorCodeController.java
│   │           │   └── WebViewController.java
│   │           ├── service/
│   │           │   ├── LogParserService.java
│   │           │   ├── ErrorAnalysisService.java
│   │           │   ├── ErrorCodeService.java
│   │           │   └── ReportService.java
│   │           ├── repository/
│   │           │   └── ErrorCodeRepository.java
│   │           ├── entity/
│   │           │   ├── ErrorCode.java
│   │           │   └── LogEntry.java
│   │           ├── dto/
│   │           │   ├── LogEntryDto.java
│   │           │   ├── ErrorAnalysisResult.java
│   │           │   ├── ErrorCodeDto.java
│   │           │   └── LogAnalysisRequest.java
│   │           ├── enums/
│   │           │   ├── LogLevel.java
│   │           │   ├── ErrorSeverity.java
│   │           │   └── ErrorType.java
│   │           ├── exception/
│   │           │   ├── LogAnalysisException.java
│   │           │   └── GlobalExceptionHandler.java
│   │           └── util/
│   │               ├── LogPatternMatcher.java
│   │               └── DateTimeUtil.java
│   └── resources/
│       ├── application.yml
│       ├── data.sql
│       └── templates/
│           └── index.html
```

## 🔧 Configuration

### Database Configuration
The application uses H2 in-memory database by default. To use a persistent database, update `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/loganalyzer
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

### File Upload Configuration
Configure maximum file size in `application.yml`:

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB
```

## 📚 API Documentation

### Log Analysis Endpoints

#### Upload File for Analysis
```http
POST /api/analysis/upload
Content-Type: multipart/form-data

Parameters:
- file: Log file to analyze
```

#### Analyze Text Content
```http
POST /api/analysis/text
Content-Type: application/json

Body:
{
  "logContent": "log content string",
  "fileName": "optional filename"
}
```

#### Export Results
```http
POST /api/analysis/export/csv
POST /api/analysis/export/html
Content-Type: application/json

Body: Array of ErrorAnalysisResult objects
```

### Error Code Management Endpoints

#### Get All Error Codes
```http
GET /api/error-codes
```

#### Create New Error Code
```http
POST /api/error-codes
Content-Type: application/json

Body:
{
  "errorCode": "ERROR_001",
  "errorType": "SYSTEM_ERROR",
  "description": "Error description",
  "solution": "Solution steps",
  "severity": "HIGH",
  "keywords": "keyword1,keyword2"
}
```

#### Update Error Code
```http
PUT /api/error-codes/{id}
Content-Type: application/json
```

#### Delete Error Code
```http
DELETE /api/error-codes/{id}
```

## 🔍 Supported Log Formats

The application can parse various log formats:

1. **Standard Format**: `LogLevel"Component" Message Timestamp`
2. **Simple Format**: `LogLevel: Message`
3. **Timestamp First**: `Timestamp LogLevel Message`
4. **Custom Patterns**: Configurable regex patterns

### Example Supported Log Entries
```
Debug"PSMTE" PS sendCommand "SKLI0,1;SET\r" 12 12 2025-08-13 01:20:30
Warning: OpenType support missing for "Ubuntu"
Fatal: Laser is not connected!!
2025-08-13 01:20:30 Critical RMQ connection failed
```

## 🎯 Usage Examples

### 1. Analyzing a Log File
1. Navigate to the main page
2. Click "Browse Files" or drag-drop your log file
3. Click "Analyze File"
4. Review the results in the generated table

### 2. Adding Custom Error Codes
1. Go to Error Code Management section
2. Click "Add New Error Code"
3. Fill in the error details:
   - Error Code: `CUSTOM_001`
   - Type: `HARDWARE_ERROR`
   - Description: `Custom hardware failure`
   - Solution: `Check hardware connections`
   - Severity: `HIGH`
   - Keywords: `hardware,failure,connection`

### 3. Filtering Results
1. After analysis, click the "Filter" button
2. Select desired filters:
   - Severity level
   - Log level
   - Matched/Unmatched status
   - Component name

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn integration-test
```

### Sample Test Log File
Create a test log file with the following content:
```
Debug"PSMTE" PS sendCommand "SKLI0,1;SET\r" 2025-08-13 01:20:30
Warning: OpenType support missing for "Ubuntu"
Fatal: Laser is not connected!!
Critical: RMQ connection failed to 192.168.56.3:5672
Error: Serial device not open on /dev/ttyUSB0
```

## 🔧 Development

### Setting up Development Environment
1. Clone the repository
2. Import into your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)
3. Ensure Java 11+ and Maven are configured
4. Run the main application class: `LogErrorAnalyzerApplication`

### Adding New Error Patterns
1. Update `LogPatternMatcher.java` with new patterns
2. Add corresponding error codes to `DatabaseConfig.java`
3. Test with sample log files

### Database Console
Access H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:loganalyzer`
- Username: `sa`
- Password: (leave empty)

## 🚀 Production Deployment

### 1. Build Production JAR
```bash
mvn clean package -Pprod
```

### 2. Run with Production Profile
```bash
java -jar target/log-error-analyzer-1.0.0.jar --spring.profiles.active=prod
```

### 3. Environment Variables
Set the following environment variables for production:
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/loganalyzer
export SPRING_DATASOURCE_USERNAME=username
export SPRING_DATASOURCE_PASSWORD=password
export SERVER_PORT=8080
```

### 4. Docker Deployment
```dockerfile
FROM openjdk:11-jre-slim
COPY target/log-error-analyzer-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📊 Monitoring and Metrics

The application includes built-in monitoring capabilities:
- Spring Boot Actuator endpoints
- Error analysis statistics
- System health indicators
- Database connection monitoring

Access actuator endpoints at: `http://localhost:8080/actuator`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style Guidelines
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Write unit tests for new functionality
- Maintain consistent indentation (4 spaces)

## 🐛 Troubleshooting

### Common Issues

#### 1. File Upload Fails
- Check file size limits in `application.yml`
- Ensure file has proper read permissions
- Verify file format is supported (.log, .txt)

#### 2. Database Connection Error
- Verify database credentials
- Check if database server is running
- Ensure network connectivity

#### 3. Out of Memory Error
- Increase JVM heap size: `-Xmx2g`
- Process large files in chunks
- Consider using streaming approach

#### 4. Pattern Matching Issues
- Verify regex patterns in error codes
- Check keyword matching configuration
- Test with sample log entries

### Logging Configuration
Enable debug logging for troubleshooting:
```yaml
logging:
  level:
    com.loganalyzer: DEBUG
    org.springframework.web: INFO
```


## 🙏 Acknowledgments

- Spring Boot community for excellent framework
- Bootstrap team for responsive UI components
- H2 Database for embedded database solution
- DataTables for advanced table functionality

## 📞 Support

If you encounter any issues or have questions:
1. Check the [Issues](https://github.com/saif5557/log-error-analyzer/issues) page
2. Create a new issue with detailed description
3. Contact: saifm5557@gmail.com

---

⭐ **Star this repository if you find it helpful!**
