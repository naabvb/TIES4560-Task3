# Clubs and courses service
RESTful service using jersey

## Prerequisites
```
Tomcat 9.0 or newer
```

## Examples

### Users
Adding a user:
```
POST localhost:8080/register/webapi/users
```
With a JSON body of:
```
{
	"firstName": "Etunimi",
	"lastName": "Sukunimi",
	"login": "usernimi",
	"password": "salasana"
}
```
Get all users:
```
GET localhost:8080/register/webapi/users
```
Get user by id:
```
GET localhost:8080/register/webapi/users/{usersId}
```
### Courses
Adding a course:
```
POST localhost:8080/register/webapi/courses
```
With a JSON body of:
```
{
	"title": "testikurssi2",
	"teacher": "vesa"
}
```
Update a course:
```
PUT localhost:8080/register/webapi/courses/{courseId}
```
With a JSON body of:
```
{
	"title": "updated_title",
	"teacher": "vesa_uusi"
}
```
Delete a course:
```
DELETE localhost:8080/register/webapi/courses/{courseId}
```
Get all courses:
```
GET localhost:8080/register/webapi/courses
```
Get courses with a certain teacher:
```
GET localhost:8080/register/webapi/courses?teacher={teacher}
```
Get x amount of courses:
```
GET localhost:8080/register/webapi/courses?size={x}
```
Get course by id:
```
GET localhost:8080/register/webapi/courses/{courseId}
GET localhost:8080/register/webapi/courses/3
```
### Students

Add student to course:
```
POST localhost:8080/register/webapi/courses/{courseId}/students
```
With a JSON body of:
```
{
	"name": "Oppi oppilas"
}
```
Get students of a course:
```
GET localhost:8080/register/webapi/courses/{courseId}/students
```

Get a certain student (of a certain course):
```
GET localhost:8080/register/webapi/courses/{courseId}/students/{studentId}
```
### Feedback
Add feedback to course:
```
POST localhost:8080/register/webapi/courses/{courseId}/feedback
```
With a JSON body of:
```
{
	"feedbackSender": "Mielensapahoittanut",
	"feedbackText": "Kyllä nyt pahoitin mieleni niiin että"
}
```
Get feedback of a course:
```
GET localhost:8080/register/webapi/courses/{courseId}/feedback
```

Get a certain feedback (of a certain course):
```
GET localhost:8080/register/webapi/courses/{courseId}/feedback/{feedbackId}
```


