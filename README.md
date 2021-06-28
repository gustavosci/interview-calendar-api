# interview-calendar-api
Interview Calendar API


## Challenge
Build an interview calendar API.
There may be two roles that use this API, a candidate and an interviewer. A typical scenario is when:
1. An interview slot is a 1-hour period of time that spreads from the beginning of any hour until the beginning of the next hour. For example, a time span between 9am and 10am is a valid interview slot, whereas between 9:30am and 10:30am it isnot.
2. Each of the interviewers sets their availability slots. For example, the interviewer David is available next week each day from 9am through 4pm without breaks and the interviewer Ingrid is available from 12pm to 6pm on Monday and Wednesday next week, and from 9am to 12pm on Tuesday and Thursday.
3. Each of the candidates sets their requested slots for the interview. For example, the candidate Carl is available for the interview from 9am to 10am any weekday next week and from 10am to 12pm on Wednesday.
4. Anyone may then query the API to get a collection of periods of time when it’s possible arrange an interview for a particular candidate and one or more interviewers. In this example, if the API is queries for the candidate Carl and interviewers Ines and Ingrid, the response should be a collection of 1-hour slots: from 9am to 10am on Tuesday, from 9am to 10am on Thursday.

## Usage API Guide

### Prerequisites
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install)
- [Gradle](https://gradle.org/install)
- Ports 8080 and 800 are not being used by any process

### Running Locally
- Clone this repository (either via SSH or HTTPS). For example:
```  
    git clone git@github.com:gustavosci/interview-calendar-api.git
```  

- Execute the following command on the project's root path:
```  
    docker-compose up -d
```

After that, all the required containers will start up and the API is ready to be used on port 8080. 

- Are you tired of testing and want to stop the application? You should just run the following command:
```  
    docker-compose down
```

After that, all the containers will be stopped and then the used ports will be unblocked. 

### Endpoints
The API provides 4 endpoints to achieve the challenge's requirements:

1. POST /v1/users - responsible to register a new user
2. POST /v1/interviewers/availability - responsible to set the interviewers available slots.
3. POST /v1/candidates/availability - responsible to set the candidates available slots.
4. GET /v1/candidates/availability - responsible to get the available slots of a given candidate and one or more interviewers. It's also possible to define a limit date in the future for the search. By default, the service considers slots from the current day to 7 days in the future.

**Additional information:**
- For all the endpoints (except `/v1/users`) the header `username` is mandatory. That emulates the `Authorization` header that a real application would probably have. That's the user identifier and will be the base of all other actions.   
- For further details regarding the API's contracts (parameters, request/response models, etc), please access our swagger by [clicking here](http://localhost:8080/swagger-ui/). PS: The application must be running.

## Technical Details

### Stack
- Java 11
- Spring Boot 2.5.1
- DynamoDB 
- Gradle

### Contributing
In order to contribute with this amazing project, please follow the instructions below:

1. Fork the Repository
2. Create your Feature Branch (git checkout -b feature/AnyFeature)
3. Commit your Changes (git commit -m 'Added some AmazingFeature')
4. Push to the Branch (git push origin feature/AnyFeature)
5. Open a Pull Request
