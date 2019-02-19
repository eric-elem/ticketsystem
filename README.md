# Assumptions
Seat hold duration is 30 seconds.
  - Change this by modifying Utility.HOLD_DURATION (in milliseconds)

# Test Instructions
1. Run unit tests using 'mvn surefire:test'
2. Run integration test using 'mvn failsafe:integration-test'
3. To view test coverage report;
   - Run both tests simultaneously using 'mvn package'
   - In the project folder, navigate to 'target/jacoco/index.html'
   - Open this file using a web browser

# Run project
1. In project directory, run 'mvn clean install'
2. In the project directory, run 
   'java -jar target\ticketsystem-0.0.1-SNAPSHOT-jar-with-dependencies.jar'