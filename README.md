# Getting Started
## AWS DynamoDB setup locally

```bash
  aws configure --profile local-dev
```
use the below suggested or whatever you want to set, make sure the same has also been set in the FF4JConfig.java
AWS_ACCESS_KEY_ID=test
AWS_SECRET_ACCESS_KEY=test
AWS_DEFAULT_REGION=us-east-1

To check if the profile has been set correctly,
```bash
  aws configure list --profile local-dev
```

## Run locally
```bash
  docker-compose down && docker-compose up -d
  ./gradlew bootRun
```

## CLI commands for testing
The application would create a table in dynamoDB called ff4j-features. And in the table it will create 3 toggles which will be disabled by default
1. DUMMY_TOGGLE_1
2. DUMMY_TOGGLE_2
3. DUMMY_TOGGLE_3

### Check Feature DB Table contents
```bash
  aws dynamodb scan --table-name ff4j-features --endpoint-url=http://localhost:4566
```
if it gives any token expired error, run the below
```bash
    export AWS_ACCESS_KEY_ID=test
    export AWS_SECRET_ACCESS_KEY=test
    export AWS_DEFAULT_REGION=us-east-1
```
### Check feature state
```bash
    curl -X GET http://localhost:8080/api/features/DUMMY_TOGGLE_1
```

### Enable feature state
```bash
    curl -X PUT http://localhost:8080/api/features/DUMMY_TOGGLE_1/enable
```

### Disable feature state
```bash
    curl -X PUT http://localhost:8080/api/features/DUMMY_TOGGLE_1/disable
```
