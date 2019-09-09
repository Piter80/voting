


# RestaurantVotingRESTfullApplication

Simple REST-based application with voting system which can be used by society to determine which restaurant provides the best daily menu and prices.


## Basic techical features: 

* Role-based access with admin and user roles.
* Separate REST links for admin and user usage.
* Authorization provided by Basic authorization.
* HSQLDB embedded allows to start working with app without additional installation of database.
* Most-requestable information is cached by Hibernate 2nd level cache.
* Has built-in exception handler.
* Works with JSON (consumes and provides as well).


## Basic usability features:
* Allows admin to make CRUD actions with votes, restaurants, dishes and users.
* Allows unathorized user to register and then update a profile, get one restaurant menu, get all restaurants with menus and make a vote.
* Users can vote once per day and change their decision to 11:00 AM.


#### Vote:
* Admins can make simple CRUD actions.
* Admins can get daily votes, get votes for specific restaurant, get votes for specific period (optionally with user) and get votes of specific user.
#### Dish:
* Admins can make simple CRUD actions.
#### Restaurant:
* Admins can make simple CRUD actions.
#### User:
* Admins can make simple CRUD actions.

# How to handle? 
User and admin can interact with application by `GET/POST/PUT/DELETE` requests with request params and entity bodies in JSON as stated below:

### User voting:

* Rest link: [http://localhost:8080/voting_war_exploded/rest/user/register](http://localhost:8080/voting_war_exploded/rest/user/register)
* **Register new user**: 
curl -s -X PUT -d '{"name":"CreatedName", "email":"user@yandex.ru", "password":"createdPass"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting_war_exploded/rest/user/register

* **Edit existing user**: 
curl -s -X POST -d '{"name":"CreatedName", "email":"user@yandex.ru", "password":"createdPass"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting_war_exploded/rest/user/update --user user@yandex.ru:createdPass

* **Get menu of specific restaurant**: 
curl -s http://localhost:8080/voting_war_exploded/rest/user/menu/1000 --user user@ya.ru:pass


* **Get all available restaurants with menu**: 
curl -s http://localhost:8080/voting_war_exploded/rest/user/ --user user@ya.ru:pass

* **Give vote for restaurant**: user/1000 where 1000 is restaurant id
curl -s http://localhost:8080/voting_war_exploded/rest/user/1000 --user user@ya.ru:pass

### Admin Vote Management:
* Rest link: [http://localhost:8080/voting_war_exploded/rest/admin/vote/](http://localhost:8080/voting_war_exploded/rest/admin/vote/)
* **Get specific vote**: 
curl -s http://localhost:8080/voting_war_exploded/rest/admin/vote/1000 --user admin@ya.ru:pass

* **Delete specific vote**: 
curl -s -X DELETE http://localhost:8080/voting_war_exploded/rest/admin/vote/1000 --user admin@ya.ru:pass

* **Get all votes by user**: 
curl -s http://localhost:8080/voting_war_exploded/rest/admin/vote/user/1000 --user admin@ya.ru:pass

* **Get daily votes**: 
curl -s http://localhost:8080/voting_war_exploded/rest/admin/vote/daily --user admin@ya.ru:pass

* **Get restaurant votes**: 
curl -s http://localhost:8080/voting_war_exploded/rest/admin/vote/restaurant/1000 --user admin@ya.ru:pass

* **Get between dates**: 
curl -s "http://localhost:8080/voting_war_exploded/rest/admin/vote/between?startDate=2019-05-30&endDate=2019-05-31" --user admin@ya.ru:pass

* **Get between dates with user**: 
curl -s "http://localhost:8080/voting_war_exploded/rest/admin/vote/between/1000?startDate=2019-05-30&endDate=2019-05-31" --user admin@ya.ru:pass


### Admin User Management:
* Rest link: [http://localhost:8080/voting_war_exploded/rest/admin/user/](http://localhost:8080/voting_war_exploded/rest/admin/user/)
* **Get all users**: 
curl -s http://localhost:8080/voting_war_exploded/rest/admin/user/ --user admin@ya.ru:pass

* **Get specific user**: 
curl -s http://localhost:8080/voting_war_exploded/rest/admin/user/1000 --user admin@ya.ru:pass

* **Create user**:
curl -s -X PUT -d '{"name":"AdminCreatedName", "email":"userFromAdmin@yandex.ru", "password":"adminCreatedPass"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting_war_exploded/rest/admin/user/ --user admin@ya.ru:pass

* **Edit existing user**: 
curl -s -X POST -d '{"name":"AdminEditedName", "email":"userEditedAdmin@yandex.ru", "password":"adminEditedPass"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting_war_exploded/rest/admin/user/1000 --user admin@ya.ru:pass

* **Delete existing user**: 
curl -s -X DELETE http://localhost:8080/voting_war_exploded/rest/admin/user/1000 --user admin@ya.ru:pass

* **Get user by email**: 
curl -s "http://localhost:8080/voting_war_exploded/rest/admin/user/by?email=admin@ya.ru" --user admin@ya.ru:pass

### Admin Dish Management:
* Rest link: [http://localhost:8080/voting_war_exploded/rest/admin/dish/](http://localhost:8080/voting_war_exploded/rest/admin/dish/)
* **Get all dishes by restaurant**: 
curl -s http://localhost:8080/voting_war_exploded/rest/admin/dish/restaurant/1000 --user admin@ya.ru:pass

* **Get specific dish**: `GET request on http://localhost:8080/voting_war_exploded/rest/admin/dish/id where id is dish id. Returns 200 code.`
curl -s http://localhost:8080/voting_war_exploded/rest/admin/dish/1000 --user admin@ya.ru:pass

* **Delete existing dish with id 1000**:
curl -s -X DELETE http://localhost:8080/voting_war_exploded/rest/admin/dish/1000 --user admin@ya.ru:pass

* **Create dish for restaurant with id 1000**:
 curl -s -X PUT -d '{"name":"Puted dish", "price":"55.55"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting_war_exploded/rest/admin/dish/1000 --user admin@ya.ru:pass

* **Edit existing dish**: 
curl -s -X POST -d '{"id":"1000", "name":"Puted dish", "price":"55.55", "created":"2018-01-25T21:34:55"}' -H 'Content-Type:application/json;charset=UTF-8' "http://localhost:8080/voting_war_exploded/rest/admin/dish/1000?restId=1000" --user admin@ya.ru:pass


* 
### Admin Restaurant Management:
* Rest link: [http://localhost:8080/voting_war_exploded/rest/admin/restaurant/](http://localhost:8080/voting_war_exploded/rest/admin/restaurant/)
* **Get all restaurants**: 
curl -s http://localhost:8080/voting_war_exploded/rest/admin/restaurant/ --user admin@ya.ru:pass

* **Get specific restaurant**: 
curl -s http://localhost:8080/voting_war_exploded/rest/admin/restaurant/1000 --user admin@ya.ru:pass

* **Delete existing restaurant**:
curl -s -X DELETE http://localhost:8080/voting_war_exploded/rest/admin/restaurant/1000 --user admin@ya.ru:pass

* **Create new restaurant**:
curl -s -X PUT -d '{"name":"CreatedRestaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting_war_exploded/rest/admin/restaurant/ --user admin@ya.ru:pass

* **Edit existing restaurant**: `POST request on http://localhost:8080/voting_war_exploded/rest/admin/restaurant/. Consumes JSON body with params: name. Returns 200 code.`
curl -s -X POST -d '{"name":"UpdatedRestaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting_war_exploded/rest/admin/restaurant/1000 --user admin@ya.ru:pass

**In case of any body or field error exception with full description would be thrown. Application returns it in JSON with url and message.**


