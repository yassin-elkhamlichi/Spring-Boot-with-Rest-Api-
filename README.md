# Spring-Boot-Doc-Rest-Api-dev

---

# 1 Spring Mvc : 
is part of spring ecosystem.
is framework for building web applications.
hundle HTTP request and response. 
mvc =  model view controller
model : data + logic
view : what user see
controller : java (Middleman) 

---

### Controller :
```java
@Controller()
public class HomeController {
@RequestMapping("/")
public String index() {
return "index.html";
}
}
```
when you write @Controller annotation, spring will create bean for this class when you run your code,
when you write @RequestMapping annotation, spring will know that this method is responsible 
for handling HTTP request.

### tamplate engine :
!!! to make view dynamic we need use **tamplate engine** :
A tool  that helps you generate HTML pages dynamically.
ex : **thymeleaf** , freemarker , velocity , jsp , jstl
### Thymeleaf :
so we use **thymeleaf**
is same thing as jsp but more powerful
we need to add dependency in pom.xml 
and after add namespace in html file
```html
<meta charset="UTF-8" xmlns:th="http://www.thymeleaf.org">
```
and after you can  use the **thymeleaf** attributes 
for example : 
```html
<h1 th:text="'Hello ' + ${name} + '!'">Hello World!</h1>
``` 
so you should go to your controller and add name attribute to your model
```java
@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "yassine");
        return "index";
    }
}
```
make sure you are importing **org.springframework.ui.Model**