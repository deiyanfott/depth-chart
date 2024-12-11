# Getting Started

### Pre-requisites
* Ensure the lombok plugin is installed in your preferred IDE. See guide to install lombok [here](https://projectlombok.org)

### Team Mapping
* 1 - Tampa Bay Buccaneers
* 2 - New York Jets

## Positions
* QB and LWR for now

### Design Patterns
* Composite Pattern - The structure of teams and sports is similar to a tree where sports are the top node, teams are the branches, and the players are the leaves.

### Data Structures
* ArrayList - I used positionDepth as an entity variable and use it to order the retrieved lists accordingly.

### Decisions
* Http Status Codes are the indicators for a success or failure in API calls
* Enum for player positions - Opted for this instead of saving data in tables (which I would ideally do, then cache using redis or some other mechanism)
* In memory database - Used H2 db to store data
* Preloaded two teams when application starts (see DepthChartApplication.java)
* Added placeholder for different sports and teams in the API path variables
* Logging - Logging using Slf4j and only via console (no log file creation)
* Swagger - To avoid using Postman
* Lombok - To reduce boilerplate code
* Adding sports and different teams - Initial load can be done through a create.sql file, and APIs, objects, etc. can be created after. All these are hypothetical and no concrete implementation yet.
* Thread safety - Assumption is that this is a single user system, so it was not taken into consideration
* Player Number - Used String instead of int as there can be '00' and '0' as player numbers. Uniqueness is based on team id + player number combination
* Position Depth - Default value of -1 if not specified in request

### Potential Issues that I de-prioritised
* Used post instead of get for get backups API (not much of a param value fan)
* Depth Chart - I am not displaying the team names so it can be quite confusing if the same positions are available in all teams
* Position Depth - This is shown in all request bodies but is only needed in the add-players API. Please remove it for the other APIs as it may cause unexpected behaviours
* Remove player and get backups - There is no validation check for the name. As long as number is valid and existing, it should remove appropriate record or retrieve the backups, if any.

### Validation Checks
* Player number - Must be a valid integer that ranges from 0-99
* Player position - Only QB and LWR will be accepted for now
* Position Depth - Value can be anything if position in that team has no players yet, but once a depth chart exists, it should be between -1 to the max list size of the depth chart that corresponds to the team and position

### Starting the application
This is a spring boot application with default port 8080. Just run DepthChartApplication.java in your IDE. Alternatively, you can do a one time configuration set up for the java class

### Endpoints
{leagueId} is just a placeholder for any int value. This is not used in the application yet. Change {teamId} to the appropriate values. See team id mapping above.
* [Add player to depth chart](http://localhost:8080/api/v1/depth-chart/{leagueId}/{teamId}/add-player)
* [Remove player from depth chart](http://localhost:8080/api/v1/depth-chart/{leagueId}/{teamId}/remove-player)
* [Get player backup](http://localhost:8080/api/v1/depth-chart/{leagueId}/{teamId}/get-backups)
* [Get full depth chart](http://localhost:8080/api/v1/depth-chart/{leagueId}/{teamId}/get-full-depth-chart)

### Http Status Codes Meaning
* 200 - Removal of player or retrieval of depth chart or backups were successful
* 201 - Adding player to depth chart was successful
* 204 - <NO LIST> requirement. If player request input was not removed, there are no backups, or no depth chart is available
* 400 - There is something wrong with the request body input

### URLs
* [H2DB UI](http://localhost:8080/h2-console)
* [Swagger UI](http://localhost:8080/swagger-ui/index.html)