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

### 1.1 Controller :
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

### 1.2 tamplate engine :
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
<h1 th:text="'hello '+${name} "></h1>
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
---
### 1.3 Rest API :
Great question! Let's break it down clearly:

---

## 1.3.1 🔹 What is a **REST API**(Rest ful Api)?

**REST** stands for **Representational State Transfer**.  
It’s an **architectural style** for designing networked applications — especially web services.

A **REST API** (or RESTful API) is an API that follows REST principles, typically using **HTTP methods** (`GET`, `POST`, `PUT`, `DELETE`, etc.) to perform operations on **resources** (like users, products, orders).

### ✅ Key Characteristics of REST:
| Principle | Description |
|--------|-------------|
| **Stateless** | Each request from a client must contain all info needed. Server doesn’t store session data. |
| **Resource-based** | Everything is a *resource* (e.g., `/users/123`). |
| **Use HTTP methods** | `GET` = read, `POST` = create, `PUT/PATCH` = update, `DELETE` = delete. |
| **Use standard HTTP status codes** | `200 OK`, `201 Created`, `404 Not Found`, `500 Server Error`, etc. |
| **Returns data (not views)** | Usually in **JSON** or **XML** — not HTML pages. |

> 🌐 Example REST endpoint:  
> `GET /api/users/5` → returns JSON: `{ "id": 5, "name": "Alice" }`
> the users named end point is a resource
---

## 1.3.2 🔹 What is a **"Normal" API with `@Controller`**?

In Spring Boot (and Spring MVC), you can create two main types of controllers:

### 1. **`@Controller`** → Traditional Web Controller
- Used for **server-rendered web apps** (like returning HTML pages).
- Methods typically return **view names** (e.g., `"home"`) that map to Thymeleaf/FreeMarker templates.
- Often used with `Model` to pass data to the view.

```java
@Controller
public class UserController {
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "user-list"; // renders user-list.html
    }
}
```
> 🖥️ This returns an **HTML page**, not JSON.

---

### 2. **`@RestController`** → REST API Controller
- Specifically designed for **RESTful web services**.
- Every method **automatically returns data** (e.g., JSON), **not views**.
- It’s equivalent to `@Controller + @ResponseBody`.

```java
@RestController
public class UserApiController {
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.getAll(); // automatically serialized to JSON
    }
}
```
> 📡 This returns **JSON data** — perfect for mobile apps, frontend frameworks (React, Angular), or other services.

---

## ✅ When to Use Which?

- Use **`@Controller`** if you’re building a **traditional website** where the server renders HTML (e.g., admin dashboard with Thymeleaf).
- Use **`@RestController`** if you’re building a **backend API** for a **separate frontend** (React, Angular, mobile app) or for **microservices**.

---


## ✅ Summary

> - **REST API** = Data-only API using HTTP methods, returns JSON/XML.
> - **`@RestController`** = Spring annotation to build REST APIs easily.
> - **`@Controller`** = For server-rendered web pages (HTML), not APIs.

---
!!! Note :
```java
public List<User> getAllUsers() {
return userRepository.findAll();
}
```
the return type for method is Iterable and this is interface parent of List
so every list is Iterable but not every Iterable is List
so we use here Iterable instead of List

### 1.3.3 Postman :

### 🔹 What is **Postman**?

🌐 Browser = Good for quick GET checks.
🛠️ Postman = Essential for real API development (POST, PUT, auth, testing, teamwork).

**Postman** is a free tool (desktop + web) to **test, build, and document APIs** — especially **REST APIs**.

Think of it as a **"remote control" for your backend**.  
Instead of using a browser or writing code, you send HTTP requests (GET, POST, etc.) and see the response instantly.

---

### 🔹 Why Do You Need It?

✅ **Test your API** without a frontend  
✅ **Debug endpoints** fast (see status code, headers, JSON response)  
✅ **Send data** (like JSON in POST requests) easily  
✅ **Save & organize** your requests (great for teams)  
✅ **Automate tests** (e.g., check if login returns 200 OK)  
✅ **Document your API** automatically

> 🎯 Example:  
> You built a `/api/users` endpoint in Spring Boot?  
> Use Postman to **POST** a new user with JSON → see if it works in 10 seconds!

---

### 🔹 Basic Use Case

1. Open Postman
2. Choose method: `POST`
3. Enter URL: `http://localhost:8080/api/users`
4. Go to **Body → raw → JSON**
5. Paste:
   ```json
   { "name": "Ali", "email": "ali@example.com" }
   ```
6. Hit **Send** → See response (201 Created? Error? Data?)

---

### 🔹 Who Uses It?

- Backend devs (to test their own APIs)
- Frontend devs (to check how the API works before coding)
- QA testers (to automate API tests)
- DevOps (to monitor API health)

---

### ✅ Bottom Line

> **Postman = Your API playground.**  
> No coding needed. Just send requests & see what your server says.

---

```java
@AllArgsConstructor
@RestController
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id);
    }

}
```
we call users many tames so we try to remove users word and add it in the @RequestMapping
```java
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
  private final UserRepository userRepository;

    @GetMapping
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
     @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id);
    }

}
```
> @PathVariable is a Spring Framework annotation used in controller methods to extract values from the URI (URL) path.

in this way when i enter for example id not exist in DB 
the restfull return 200 ok with null
so we need to hundle this and when user is null return 404 not found
for do this we need to change type return in  the method
to ResponseEntity<T>
```java
GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        var user =  userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
```
🔹 What is ResponseEntity?
ResponseEntity<T> is a Spring class that represents the entire HTTP response — including:

Status code (e.g., 200 OK, 404 Not Found, 500 Internal Server Error)
Response headers
Response body (your actual data, like a User object or error message)
It gives you full control over what your REST endpoint returns.

---
### DTO (Data Transfer Object)

### 🔹 What is a DTO?

**DTO = Data Transfer Object**

It’s a **simple Java class** (often called a "POJO" – Plain Old Java Object) that **carries data** between layers of your application (e.g., from controller → service → external API, or database → API response).

A DTO:
- Has **fields** (usually private)
- Has **getters and setters** (or uses Lombok)
- **No business logic**
- **No database annotations** (like `@Entity`)
- Often **serializable** (to JSON/XML)

---

### 🔹 Example: Without DTO (Problem!)

Imagine you have a JPA `User` entity:

```java
@Entity
public class User {
    @Id
    private Long id;
    private String email;
    private String password;        // 🔒 Sensitive!
    private String role;            // e.g., "ADMIN"
    private LocalDateTime createdAt;
    // ... getters/setters
}
```

Now, if you return this directly from your REST controller:

```java
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userRepository.findById(id).orElseThrow();
}
```

🚨 **Problems:**
1. You’re exposing **sensitive data** like `password` and `role` to the client!
2. You can’t easily **rename fields** (e.g., `createdAt` → `created_at` for JSON).
3. If you change your database entity, your **API contract breaks**.
4. You’re tightly coupling your **database model** to your **API model**.

---

### 🔹 Solution: Use a DTO

Create a `UserDto`:

```java
public class UserDto {
    private Long id;
    private String email;
    private String createdAt; // formatted as string, e.g., "2024-06-01"

    // Constructors, getters, setters
    // Or use Lombok: @Data
}
```

Now map your entity to DTO in the controller:

```java
@GetMapping("/{id}")
public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    UserDto dto = new UserDto();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setCreatedAt(user.getCreatedAt().toString()); // format as needed
    return ResponseEntity.ok(dto);
}
```

✅ **Benefits:**
- **No password leaked!**
- Full control over **what data is exposed**
- Can **format/transform** data (e.g., dates, enums)
- API stays stable even if DB changes

---

### 🔹 When Should You Use DTOs?

✅ **Use DTOs when:**

| Scenario | Why |
|--------|-----|
| **Exposing data via REST API** | Hide sensitive/internal fields (`password`, `internalId`) |
| **Receiving data from clients** (e.g., `@RequestBody`) | Validate only what you need; avoid overposting |
| **Talking to external services** | Match their expected data format |
| **Returning computed or aggregated data** | e.g., `UserWithOrderCountDto` |
| **Avoiding lazy-loading issues** | Don’t send Hibernate proxies over the wire! |
| **Versioning your API** | `UserDtoV1`, `UserDtoV2` |

❌ **You might skip DTOs when:**
- Building a **very simple internal prototype**
- The entity **exactly matches** what the client needs (rare in real apps)
- Using **GraphQL** (where clients request only needed fields)

> 💡 **Rule of thumb**: If your API is public or used by frontend/mobile teams — **always use DTOs**.

---

### 🔹 Types of DTOs

1. **Response DTO** – for sending data **to client**
   ```java
   public class UserResponseDto { ... }
   ```

2. **Request DTO** – for receiving data **from client**
   ```java
   public class CreateUserRequestDto {
       private String email;
       private String password; // will be hashed later
   }
   ```

3. **Internal DTO** – for communication between microservices

---

> 🛡️ **Think of DTOs as your API’s "public face" — never expose your internal guts directly!**


```java
@GetMapping
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }
@GetMapping("/{id}")
public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
    var user =  userRepository.findById(id).orElse(null);
    if (user == null){
        return ResponseEntity.notFound().build();
    }
    var userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
    return ResponseEntity.ok(userDto);
}
```
this method:
Fetches all users from DB → List<User>
Turns it into a stream → Stream<User>
Converts each User to a safe UserDto → Stream<UserDto>
Collects results into a list → List<UserDto>
Returns it as Iterable<UserDto> (valid for Spring REST)

---
this method use the manually mapping
so we need to use auto mapping
so we choose library for mapping **mapstruct**
first we need add depandency for this library 
```xml
 <dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.6.2</version>
</dependency>
<dependency>
<groupId>org.mapstruct</groupId>
<artifactId>mapstruct-processor</artifactId>
<version>1.6.3</version>
</dependency>
```
and after  we need to create interface for mapping
```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
```
spring automatically create implementation for this interface
now we can use this interface in our controller
```java
   @GetMapping
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user =  userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
       return ResponseEntity.ok(userMapper.toDto(user));
    }
```