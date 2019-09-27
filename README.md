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
With a JSON body
```
{
	"title": "testikurssi2",
	"teacher": "vesa",
	"students": []
}
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
