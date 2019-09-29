# Clubs and courses service
RESTful service using jersey

## Prerequisites
```
Tomcat 9.0 or newer
```

## Examples

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
Get course by id:
```
GET localhost:8080/register/webapi/courses/{courseId}
GET localhost:8080/register/webapi/courses/3
```
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

