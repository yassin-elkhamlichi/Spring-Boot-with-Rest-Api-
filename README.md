# Spring Boot Documentation – REST API Development
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

## 1.1 Controller :
```java
@Controller()
public class HomeController {
@RequestMapping("/")
public String index() {
return "index.html";
}
}
```
when you write @Controller annotation, spring will create bean for this class in the runtime,
when you write @RequestMapping annotation, spring will know that this method is responsible
for handling HTTP request.

## 1.2 tamplate engine :
>To make view dynamic we need use **tamplate engine** :

So what is **tamplate engine**
A tool  that helps you generate HTML pages dynamically.<br>
ex : **thymeleaf**, freemarker, velocity, jsp, jstl...
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

## 1.3 Rest API :

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
---

## 1.3.2 🔹 What is Different Between a "Normal" API with `@Controller`?

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

- **REST API** = Data-only API using HTTP methods, returns JSON/XML.
- **`@RestController`** = Spring annotation to build REST APIs easily.
- **`@Controller`** = For server-rendered web pages (HTML), not APIs.

---
**Note** :
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

🔹 **What is ResponseEntity?**
ResponseEntity<T> is a Spring class that represents the entire HTTP response — including:

Status code (e.g., 200 OK, 404 Not Found, 500 Internal Server Error)
Response headers
Response body (your actual data, like a User object or error message)
It gives you full control over what your REST endpoint returns.
---

## 3.4 DTO (Data Transfer Object)

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
**This method**: <br>
Fetches all users from DB → List<User> <br>
Turns it into a stream → Stream<User> <br>
Converts each User to a safe UserDto → Stream<UserDto> <br>
Collects results into a list → List<UserDto> <br>
Returns it as Iterable<UserDto> (valid for Spring REST)

---
**Poblem** : <br>
this method use the manually mapping.<br>
So we need to use auto mapping. <br>
so we choose library for mapping **mapstruct** :<br>
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
And after  we need to create interface for mapping
```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
```
Spring automatically create implementation for this interface
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
---
**serialization & deserialization**  <br><br>
java object  --> json named **serialization** <br>
json --> java object named **deserialization** <br>
<br>
**Customizing Response** : <br><br>
We have many annotation for controll json like : <br>
@JsonIgnoreProperties(ignoreUnknown = true) <br>
@JsonInclude(JsonInclude.Include.NON_NULL) <br>
@JsonProperty("name") <br>
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
<br>
For example here :
```jav
public class UserDto {
 private Long id;
 private String name;
 private String email;
 }
``` 
We can't remove id field from json because we need it for update user
but we can ignore it using @JsonIgnore
```java
public class UserDto {
    @JsonIgnore
    private Long id;
}
```
we also have jsonproperty annotation for change name of field in json
```java
public class UserDto {
@JsonProperty("full_name")
    private String name;
}
```
also we can use @JsonInclude annotation for ignore null fields
```java
   public class UserDto {
    @JsonProperty("full_name")
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
} 
```
also we have @JsonFormat annotation for change date format
```java
public class UserDto {
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
private Date createdAt;
}
```

---

## 1.4 Extracting Query Parameters :
```java
 @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false,  defaultValue = "" , name = "sort") String sortBy) {
        if(!Set.of("email","name").contains(sortBy))
            sortBy = "name";
        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

```
This method use query parameter to sort users by email or name <br>
-> the parameter required is tell the url is sort is required 
or not<br>
so you can use this url :<br>
http://localhost:8080/users?sort=email<br>
or just<br>
http://localhost:8080/users<br>
-> the parametre default value is tell the default value of sort parameter
<br>-> the parametre name is tell the name of sort parameter
if you change sortBy to another word the url still work becouse you declared name = "sort"

---
### What i learn in exercise :
```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"category"})
    public List<Product> findByCategory_id(Byte categoryId);
}

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId" , source = "category.id" )
    public ProductDto toDto(Product product);
}
```
when you have relation manytoOne you can use <br> @Mapping(target = "categoryId" , source = "category.id" )
<br>This don't get category object , but get the id from category
and in Dto you should write this :
```java
@AllArgsConstructor
@Getter
@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
```
---

## 1.4 Extracting Request Headers :
```java
public List<ProductDto> getAllProducts(
@RequestHeader(required = false , name("X-API-KEY") String apiKey )
@RequestParam(required = false) Byte categoryId) {
}
```
This annotation is about req Header help us to extract request header and use it in our method

## 1.5 Extracting Request body :
you can in the postman use body to test create ressources like user
but not create ressource in bd
if you want to create ressource in bd you need to use post method
```java
 @PostMapping
    public UserDto createUser(@RequestBody RegisterUserRequest data) {
        var user = userMapper.toEntity(data);
        userRepository.save(user);
        var userDto = userMapper.toDto(user);
        return userDto;
    }
```
Why we add RegisterUserRequest not userDTO ? because in dto we dont have
password field so we need add new class in the DTO specific for create user
```java
@Data
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
}
```
And you should add new method in mapper :
```java
User toEntity(RegisterUserRequest registerUserRequest);
```
This get registerUserRequest and return user <br>
and save user in the bd<br>
and after we call the toDto method for get userDTO<br>
and after we fetch it in the postman<br>
>now we try to update User in bd based postman<br>:

first in postman you should choose<br>
Put or patch as request method<br>
and after you should add methode in the controller like this<br>
```java
 @PutMapping("/{id}")
    public  ResponseEntity updateUser(
        @PathVariable(name = "id") Long id, 
        @RequestBody UpdateUserDto data // this  is the data from postman in the request body
    ){
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build(); // return 404 in postman if user dosn't exist
        }
        userMapper.update(data,user); // update user with data from postman
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));

    }
```
And after you should add new method in mapper like this :
```java
void update(UpdateUserDto updateUserDto,@MappingTarget User user);
```

So this method update user with data from postman mapstruct generate automaticlly this
method for you

### 🧠 How MapStruct “knows” what to do
1. Method Name Matters — But Not Alone<br>
   MapStruct doesn’t just look at the name — it looks at:<br>

✅ Method name (e.g., update, toDto)<br>
✅ Parameter types<br>
✅ Return type<br>
✅ Annotations like @MappingTarget<br>
<br>
Don't forget create UpdateUserDto for choose wish field you want to update
---
- If you want delete user you should add this method in controller :
```java
@DeleteMapping("/{id}")
public ResponseEntity deleteUser(
@PathVariable(name = "id") Long id
){
var user = userRepository.findById(id).orElse(null);
if(user == null){
return ResponseEntity.notFound().build();
}
userRepository.delete(user);
return ResponseEntity.noContent().build();
}
```
---
🔹 **What is ResponseEntity?** <br>
ResponseEntity<T> is a generic class in Spring that represents the entire HTTP response — not just the body, but also:<br>
✅ Status code (e.g., 200 OK, 404 Not Found, 500 Internal Server Error)<br>
✅ Headers (e.g., Content-Type, Location, custom headers)<br>
✅ Response body (your actual data, like JSON, XML, etc.)<br>
It gives you full control over what your REST endpoint returns.<br>
---
### Action_based updates :
we use Put and Patch for update resources but 
some updates represent an "action"
like changing password , submit approval request
so better use a Post request for this case
we try to update password so we go to controller and write this :
```java
@PostMapping("/{id}/Change_password")
    public ResponseEntity changePassword(
            @PathVariable(name = "id") Long id,
            @RequestBody ChangePasswordReqeust data
    ){
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        if(!user.getPassword().equals(data.getOldPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            // help us to know what is the error here when i use Unautauthorized
            //that mean that the old password is not correct
        }
        user.setPassword(data.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
```
And create new class in dto
```java
@Data
public class ChangePasswordReqeust {
    private String oldPassword;
    private String newPassword;
}
```
---
-> I work on Product and in update exist same different
```java
@PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto data
    ){
        var product = productRepository.findById(id).orElse(null);

        productMapper.update(data,product);
        var category = categoryRepository.findById(data.getCategoryId()).orElse(null);
        if(product == null || category == null){
            return ResponseEntity.notFound().build();
        }
        product.setCategory(category);
        productRepository.save(product);
        return  ResponseEntity.ok(productMapper.toDto(product));
    }
```
**why the different happen?** <br>
becouse in user we RegisterUserDto class and this class  have password field
and not include id so here when i call class have id
the implemantion for update method in mapper . class
Do change also for id field but for user do just for 2 fields email and
password so for resolve this problem you should add this annotation :
```java
@Mapping(target = "id", ignore = true)
void update(ProductDto productDto ,@MappingTarget Product product);
```
this tell mapstruct to ignore id field when implement
the update method from interface ProductMapper
---
# 2 Validation :
## 2.1 Jakarta validation :
instead of check fields manually we can use jakarta validation
for check for example if field emty or null or is string  ...<br>
**String Validation** :
```java
@NotBlank
private String name;
@NotEmpty
private String description;
@Size(min = 1, max = 5)
private String code;
@Email
private String email;
@Pattern(regexp = "^\\d{10}$")
private String phone;
```
@Not blank mean that field can not be null or empty

**Number Validation** : 
```java
@Min(value = 0)
@Max(value = 100)
private int age;
@Positive
@PositiveOrZero
@Negative
@NegativeOrZero
```
**Date  Validation** :
```java
@Past
@PastOrPresent
@Future
@FutureOrPresent
private Date birthDate;
@DateTimeFormat(pattern = "yyyy-MM-dd")
```
**General validation** : 
```java
@NotNull
```
- To use jakarta validation you should add this dependency in pom.xml
write in the search validation and you see this :
```xml
  <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
```
example :
```java
@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Max(value = 50, message = "Name must be less than 50 characters")
    private String name;
    @Email(message = "Email is invalid")
    private String email;
    @NotBlank(message = "Password is required")
    @Size( min= 6,max = 12, message = "Password must be bettwen 6 and 12 characters")
    private String password;
}
```
After useing this annotation you should go to the method use this class and add
@Valid annotation like this :
```java
@PostMapping
    public UserDto createUser(
            @Valid @RequestBody RegisterUserRequest data) {
                .....
    }
```
If some validation error happen you will see error in postman
but without meessage error so if you want
see what exactly error happen you should add this method in the same controller  :
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> HundleValidationErrors(
            MethodArgumentNotValidException exception
    ){
        var errors = new HashMap<String,String>();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });
        return ResponseEntity.badRequest().body(errors);
    }
```
You dont call this method in create method because this
method will call automaticlly  when validation error happen
---

---

### 🔁 Step-by-step For how this hundle errors worked

#### 1. **Incoming HTTP POST Request**
A client sends a POST request to your endpoint with a JSON body, e.g.:
```json
{
  "name": "",
  "email": "not-an-email",
  "password": "123"
}
```

#### 2. **Spring Maps JSON → `RegisterUserRequest`**
Spring’s `HttpMessageConverter` (like Jackson) deserializes the JSON into an instance of `RegisterUserRequest`.

At this point, the object exists in memory, but **no validation has happened yet**.

#### 3. **`@Valid` Triggers Validation**
Because your method parameter is annotated with:
```java
@Valid @RequestBody RegisterUserRequest data
```
Spring sees `@Valid` and **automatically triggers Jakarta Bean Validation** on the `data` object **before** your method body executes.

> 💡 **Key point**: `@Valid` is the signal that tells Spring: *"Validate this object using the constraints declared on its fields."*

#### 4. **Validation Engine Checks Constraints**
The validation engine (usually Hibernate Validator, the reference implementation of Jakarta Validation) inspects your `RegisterUserRequest` class:

```java
@NotBlank(message = "Name is required")
@Max(value = 50, message = "Name must be less than 50 characters")
private String name;

@Email(message = "Email is invalid")
private String email;

@NotBlank(message = "Password is required")
@Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
private String password;
```

It checks each field:
- `name` is blank → violates `@NotBlank`
- `email` is not a valid email → violates `@Email`
- `password` is only 3 chars → violates `@Size(min=6)`

So if  **3 constraint violations** .

#### 5. **Validation Fails → Throws `MethodArgumentNotValidException`**
Since validation failed, **Spring does NOT call your `createUser` method body**.

Instead, it **throws a `MethodArgumentNotValidException`**, which is a built-in Spring exception that wraps:
- The invalid object (`RegisterUserRequest`)
- The `BindingResult` containing all validation errors

> 🚫 Your `userRepository.save(user);` line **never runs** if validation fails.

#### 6. **Spring Looks for an Exception Handler**
Spring now searches for a method that can handle `MethodArgumentNotValidException`.

It finds your method:
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String,String>> handleValidationErrors(...) { ... }
```

> ✅ This works because:
> - It's in the same controller (`@Controller` or `@RestController`)
> - Or in a `@ControllerAdvice` class (global handler)
> - And it declares the correct exception type

#### 7. **Your Handler Extracts Error Messages**
Inside your handler:
```java
exception.getBindingResult().getFieldErrors().forEach(
    error -> {
        errors.put(error.getField(), error.getDefaultMessage());
    });
```

- `getFieldErrors()` returns a list of `FieldError` objects.
- For each error:
    - `error.getField()` → e.g., `"name"`, `"email"`, `"password"`
    - `error.getDefaultMessage()` → the **exact message you wrote** in your annotations:
        - `"Name is required"`
        - `"Email is invalid"`
        - `"Password must be between 6 and 12 characters"`

So your `errors` map becomes:
```java
{
  "name": "Name is required",
  "email": "Email is invalid",
  "password": "Password must be between 6 and 12 characters"
}
```

#### 8. **Response Sent to Client**
You return:
```java
return ResponseEntity.badRequest().body(errors);
```

So the client gets a **400 Bad Request** with JSON body:
```json
{
  "name": "Name is required",
  "email": "Email is invalid",
  "password": "Password must be between 6 and 12 characters"
}
```

---

### ✅ Summary: The Chain of Events

| Step | What Happens |
|------|--------------|
| 1 | Request arrives with invalid JSON |
| 2 | Spring deserializes JSON → `RegisterUserRequest` |
| 3 | Sees `@Valid` → runs Jakarta Validation |
| 4 | Validator checks your annotations (`@NotBlank`, `@Email`, etc.) |
| 5 | Violations found → throws `MethodArgumentNotValidException` |
| 6 | Spring catches it and looks for `@ExceptionHandler` |
| 7 | Your handler extracts **your custom messages** from the DTO |
| 8 | Returns 400 + error map to client |

---
**Problem :**
If i want do this in another controller i should repeat the same logic in all the
controllers !!!!!!!! <br>
#### **Global Error Handling** <br>
The idea instead write the same logic in many classes
we write it in one class
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> HundleValidationErrors(
            MethodArgumentNotValidException exception
    ){
        var errors = new HashMap<String,String>();
        exception.getBindingResult().getFieldErrors().forEach(
                error -> {
                    errors.put(error.getField(), error.getDefaultMessage());
                });
        return ResponseEntity.badRequest().body(errors);
    }
}
```
---

Somthing we need more control for the validation
so you can create some annotation

### 2.2 Implementing Custom Validation
First you should create new package named validation
and in this package you should create new annotation name, the name you want to write it in
the dto class. <br>
in my example we named it LowerCase
```java
@Target(ElementType.FIELD) // this annotation will be used in field because you are choosing field
@Retention(RetentionPolicy.RUNTIME) // here you choose when this annotation work
@Constraint(validatedBy =  LowerCaseValidator.class) // this annotation will choose the class content logic validation
public @interface LowerCase {
    String message() default "Must be lowercase";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```
And after you should  create new class named LowerCaseValidator
```java
public class LowerCaseValidator implements ConstraintValidator<LowerCase, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) return true;
        return s.equals(s.toLowerCase());
    }
}
```
<LowerCase, String> the first is annotation you create and
String is the type the field you applied this annotation
---
### 2.3 Validating Business Rules :
If we want to check if email unique we need to use business rules
```java
    @PostMapping
    public ResponseEntity<?> registerUserRequest(
            @Valid @RequestBody RegisterUserRequest data
    ) {

        if(userRepository.existsByEmail(data.getEmail()))
            return ResponseEntity.badRequest().body(
                    Map.of("email","Email already exists"));
        var user = userMapper.toEntity(data);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }
```
this check if email unique or no .<br>
why we dont use custom  annotation :<br>
because we need enter in the method and check if email unique or no
<br>but when you use the validation jakarta you dont enter the method and jakarata check it before
enter the method<br>
---
### Romarque in Exercice :
Sometimes when you add entities for example named Cart even if you wrote
@Table(name = "Cart") hibernate will search on table named cart
so for resolve this problem you should add this line in
yaml file
```yaml
  jpa:
    show-sql: true // just for see  the sql in your console
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
          // this tell hibernete  to use this naming strategy exact with dont  change the name
```

For store id type uuid to string in db you should add this
in your entity
```java
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id" , columnDefinition = "CHAR(36)")
    private UUID  id;
```

---
If you want to get value to field in the dto you can write this
```java
    @Mapping(target = "totalPrice", expression = "java(itemCart.getTotalPrice())")
    ItemCartDto toDto(ItemCart itemCart);
```
java(itemCart.getTotalPrice()) this call the methode and save return in totalPrice
```java
    @Mapping(target = "product", expression = "java(itemCart.getProduct() != null ? toDto(itemCart.getProduct()) : null)")
    ItemCartDto toDto(ItemCart itemCart);
    CartProductDto toDto(Product product);
```
when dto include another dto you should tell mapstruct what should do
for example in my case i have itemCartDto include productDto
so i should add new method to convert product to productDto
and call it mapping in itemCartDto

**When you try to convert toDto or toEntity** map struct change just the field has
same name if you have field with different name you should add mapping :
```aiignore
@Mapping(target = "productId", source = "id")
```
target for the object you return and source for the object you want to convert

-> when i want to get cart i should tell mapstruct items in cartDto is itemCart in cart
and tell mapstruct to calcul total price based on method i write it in my class cart
this method is this :
```java
 public BigDecimal getTotalPrice() {
        return itemCart.stream().map(ItemCart::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
```
first we get each itemCart and get total price and after we sum them

-> everything is good but we have little problem rolated with the number the query send
so to make just send one query to get all carts and all items in carts
we do this go to CartRepository and add this
```java
    @EntityGraph(attributePaths = "itemCart.product")
    @Query("select c from Cart c where c.id = :cartId ")
    Optional<Cart> getCartWithItemsAndProducts(@Param("cartId") UUID cartId);
```
the first line tell spring to get all items in carts and all products in carts
and the second is the qeury to get cart

### Information expert principle :
when you write a code in controller 
and repeat same logic in many methods in the same
controller you should to respect this principle
so you should move this logic to service
or to Expert class (in my case Cart is expert on ItemCart)
so the method calculus Total price for all items should be
exist in the cart class
and if you want to get just one ItemCart by id
you can add this method in Cart class and call it when you need without
repeat the same logic in controller or in any other place
```java
public ItemCart getItemCart(Long Product) {
    retrun   itemCart.stream()
            .filter(item -> item.getProduct().getId().equals(Product)).
            findFirst().
            orElse(null);
}
```
### Anemic Domain :
summery is principe tell : classes that only have data .<br>
so that mean "information expert principle " should wrote in service rolated with expert class

### Rich Domain :
summery is principe tell : classes that  have data and behavior :<br>
**Data** = State/Properties
id, items, status, total
product, quantity, price<br>

**Behavior** = Business Logic/Methods
addItem(), completeOrder(), calculateTotal()<br>
Validation rules, business rules, calculations
State transitions, invariants

!!!!! the controller most content just req and res so to clean controller
we should add the logic in service
but what about ResponseEntity?
easy just create new package for exception
this code before use service and exception :
```java
 @PostMapping("/{id}/item")
    public ResponseEntity<ItemCartDto> addToCart(
            @PathVariable UUID  id,
            @RequestBody AddItemToCartReqeustDto data
            ) {
        var cart = cartRepository.findById(id).orElse(null);
        var product = productRepository.findById(productId).orElse(null);
        if (cart == null)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Cart not found")
            );
        if (product == null)
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error" , "Product not found")
            );
        var itemCart = cart.addItemCart(product);
        cartRepository.save(cart);
        var cartItemCartDto = cartMapper.toDto(itemCart);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemCartDto);
    }
```
And after :
```java
 @PostMapping("/{id}/item")
    public ResponseEntity<ItemCartDto> addToCart(
            @PathVariable UUID  id,
            @RequestBody AddItemToCartReqeustDto data
            ) {
        var cartItemCartDto = cartService.addToCart(id, data.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemCartDto);
    }
```
With code in the service :
```java
  public ItemCartDto addToCart(UUID id, Long productId) {
        var cart = cartRepository.findById(id).orElse(null);
        var product = productRepository.findById(productId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();
        if (product == null)
            throw new ProductNotFoundException();
        var itemCart = cart.addItemCart(product);
        cartRepository.save(cart);
        return cartMapper.toDto(itemCart);
    }
```
And don't forget what you should write in the exception classes
```java
public class CartNotFoundException extends RuntimeException {}
public class ProductNotFoundException extends RuntimeException {}
```
And in Controller class you should add this :
```java
@ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartException(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error" , "Cart not found")
        );
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleProductException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error" , "Product not found")
        );
    }
```

---

## 📘 Documenting APIs with Swagger

A tool that automatically generates documentation for your APIs.

---

### 🔧 Add Dependency

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.8.13</version>
</dependency>
```

---

### 🌐 Access Swagger UI

After starting your application, go to:

```
http://localhost:8080/swagger-ui.html
```

You can view and test all your APIs there.

---

### 🏷️ Change Controller Name in Swagger

Add this annotation to your controller:

```java
@Api(tags = "Cart")
public class CartController {}
```

---

### 📝 Add Description to Endpoints

```java
import io.swagger.v3.oas.annotations.Operation;

@PostMapping("/{id}/item")
@Operation(summary = "Add item to cart")
public ResponseEntity<ItemCartDto> addToCart() { ... }
```

---

### 📄 Add Description for Parameters

```java
@Operation(summary = "Add item to cart")
@PostMapping("/{id}/item")
public ResponseEntity<ItemCartDto> addToCart(
        @Parameter(description = "id of cart")
        @PathVariable UUID id,
        @RequestBody AddItemToCartReqeustDto data
) {
    var cartItemCartDto = cartService.addToCart(id, data.getProductId());
    return ResponseEntity.status(HttpStatus.CREATED).body(cartItemCartDto);
}
```

---

## 📂 Handling Files in Requests

When sending a request in **Postman**:

* Use **POST** method.
* Choose **form-data** in the Body.
* Set key type to **File** for the file field.
* Add other fields as **text**.
* Convert JSON string to object in controller.

Example:

```java
@Value("${server.port}") String port;
@Value("${server.server}") String server;

@PostMapping("/upload")
public ResponseEntity<Map<String,String>> uploadFile(
        @RequestPart("file") MultipartFile file,
        @RequestPart("client") String clientString
) {
    ObjectMapper objectMapper = new ObjectMapper();
    Client client = objectMapper.readValue(clientString, Client.class);
    String path = "src/resources/static/uploads/" + client.getId() + ".jpg";
    file.transferTo(Path.of(path));
    client.setImage("http://" + server + ":" + port + "/uploads/" + client.getId());
    return client;
}
```

> `ObjectMapper` (from Jackson) maps JSON strings to objects.

---
(you are here)
### 📸 Serve Uploaded Images

You need an endpoint to serve the stored images:

```java
import org.springframework.web.bind.annotation.GetMapping;

@GetMapping("photos/{id}")
public ResponseEntity getPhoto(@PathVariable("id") Long id) {
    String path = "src/resources/static/uploads/" + id + ".jpg";
    SystemFileResource fileResource = new SystemFileResource(path);
    if (!fileResource.exists())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileResource);
}
```

> `SystemFileResource` is a custom class used to retrieve the file from the system.

---

# 🛡️ 5. Spring Security

First, add **Spring Security** dependency in your `pom.xml`.

After that, Spring automatically creates a **login page** and default user credentials.

---

## 🔐 Authentication Methods

### 1. Session-based Authentication

* User sends `POST /login`
* Server validates credentials
* Server creates a **session** and stores it
* Client stores **session ID** in cookies

> ⚠️ Not scalable — more users mean more storage on server.

---

### 2. Token-based Authentication (JWT)

> REST APIs use this method.

* Client sends `POST /login`
* Server validates credentials
* Server generates **token (JWT)** and sends it back
* Client stores token and sends it with each request (Authorization header)

💡 **Difference:**

* Token-based: data stored on **client**
* Session-based: data stored on **server**

Check tokens visually: [https://jwt.io](https://jwt.io)

---

## ⚙️ Spring Security Configuration

Create a package `config`, then add a class `SecurityConfig`.

---

### 📋 Stateless Session & CSRF

Since REST APIs are **stateless** (no session or cookies),
we must **disable CSRF** (Cross-Site Request Forgery protection).

---

### 📘 Example Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(c ->
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(c ->
                c.anyRequest().permitAll());

        return http.build();
    }
}
```

> This allows access to all endpoints (no authentication yet).

---

### 🔒 Restrict Specific Endpoints

```java
http
    .csrf(csrf -> csrf.disable())
    .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/carts/**").permitAll()
        .requestMatchers("/error").permitAll() // ✅ Important!
        .anyRequest().authenticated());
return http.build();
```

> `/carts/**` → public
> `/error` → must be public (prevents redirect loop)
> All others → require authentication.

---

## 🔑 Authentication vs Authorization

| Concept            | Meaning          | Example                   | Response Code      |
| ------------------ | ---------------- | ------------------------- | ------------------ |
| **Authentication** | Who are you?     | Login with email/password | `401 Unauthorized` |
| **Authorization**  | What can you do? | Access admin endpoints    | `403 Forbidden`    |

💬 Summary:

* **AuthN = Login (prove identity)**
* **AuthZ = Permissions (check access)**

---

## 🔐 Password Hashing

To secure passwords, add a `PasswordEncoder` bean:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

And use it before saving a user:

```java
user.setPassword(passwordEncoder.encode(user.getPassword()));
```

---

## 5.2 Authentication Management

![AuthenticationManagement](https://github.com/yassin-elkhamlichi/Spring-Boot-Doc-Rest-Api-dev-/blob/06b0ec93b47bbb25f55a2dc904571d1ce9293d1a/AuthentecationManagement.jpeg)

---

### 🧩 Manual Authentication Example

```java
@Service
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    public String authUser(AuthUserDto authUserDto, UserRepository userRepository) {
        var user = userRepository.findByEmail(authUserDto.getEmail());
        if (user == null)
            throw new UserNotFoundException();

        if (!passwordEncoder.matches(authUserDto.getPassword(), user.getPassword()))
            throw new InvalidPasswordException();

        return "Authenticated";
    }
}
```

---

### ⚙️ AuthenticationManager & DaoAuthenticationProvider

Spring provides these classes automatically:

* **AuthenticationManager** → orchestrates authentication
* **DaoAuthenticationProvider** → handles credentials
* **UserDetailsService** → loads user from DB
* **PasswordEncoder** → verifies password

---

### 🧠 UserDetailsService Implementation

```java
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
```

---

### 🧩 DaoAuthenticationProvider Bean

```java
public final UserDetailsService userDetailsService;

@Bean
public DaoAuthenticationProvider authenticationProvider() {
    var provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userDetailsService);
    return provider;
}
```

Spring automatically injects your `UserService` (because it implements `UserDetailsService`).

---

### 🧱 AuthenticationManager Bean

```java
@Bean
public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
) throws Exception {
    return config.getAuthenticationManager();
}
```

> Retrieves Spring’s built-in `AuthenticationManager` that uses your provider and encoder.

---

### 🚀 Controller Authentication Example

```java
private final AuthenticationManager authenticationManager;

@PostMapping("login")
public ResponseEntity<Void> auth(
        @Valid @RequestBody AuthUserDto authUserDto
) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            authUserDto.getEmail(),
            authUserDto.getPassword()
        )
    );
    return ResponseEntity.ok().build();
}
```

---

### ⚙️ How It Works

1. Controller sends credentials to `AuthenticationManager`.
2. `AuthenticationManager` delegates to `DaoAuthenticationProvider`.
3. Provider uses:

    * `UserDetailsService` → load user
    * `PasswordEncoder` → verify password
4. If successful → user is authenticated
5. If failed → Spring throws:

    * `UsernameNotFoundException`
    * `BadCredentialsException`


---

## 5.3 Generate JSON Web Tokens (JWT)

First, add these dependencies:

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```

---

### 🔹 JwtService

```java
@Service
public class JwtService {
    final long tokenExpiration = 86400; // token expires in 24 hours

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor("secret".getBytes()))
                .compact();
    }
}
```

---

### 🧠 Explanation

```java
public String generateToken(String email) {
```

> Declares a method that takes an `email` (used as the user identifier) and returns a JWT string.

```java
return Jwts.builder()
```

> Starts building a new JWT using the **JJWT** library’s fluent API.

```java
.subject(email)
```

> Sets the **`sub` (subject)** claim — identifies the user.

```java
.issuedAt(new Date())
```

> Sets the **`iat` (issued at)** claim.

```java
.expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
```

> Sets the **`exp` (expiration)** claim — token expires after 24 hours.

```java
.signWith(Keys.hmacShaKeyFor("secret".getBytes()))
```

> Signs the JWT with **HMAC-SHA256** using a secret key.
> ⚠️ Don’t hardcode this key — use environment variables in production.

```java
.compact();
```

> Builds the token and returns it as a compact string (`xxxxx.yyyyy.zzzzz`).

✅ **Result:** a signed JWT identifying the authenticated user.

---

### 🔹 JwtResponseDto

```java
@Data
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
}
```

---

### 🔹 AuthController (JWT Return)

```java
@PostMapping("login")
public ResponseEntity<JwtResponseDto> auth(
        @Valid @RequestBody AuthUserDto authUserDto
) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    authUserDto.getEmail(),
                    authUserDto.getPassword()
            )
    );
    String token = jwtService.generateToken(authUserDto.getEmail());
    return ResponseEntity.ok(new JwtResponseDto(token));
}
```

---

### ⚠️ Making the Secret Secure

Instead of hardcoding `"secret"`, store it in a `.env` file and use **spring-dotenv**.

#### Add the dependency

```xml
<dependency>
    <groupId>me.paulschwarz</groupId>
    <artifactId>spring-dotenv</artifactId>
    <version>{version}</version>
</dependency>
```

#### Example `.env` file

```env
DB_URL=jdbc:mysql://localhost:3306/mydb
DB_USERNAME=root
DB_PASSWORD=secret123
JWT_SECRET=my-super-secret-key
```

#### In `application.properties`

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
app.jwt.secret=${JWT_SECRET}
```

✅ This keeps secrets **outside your code**.

You can add `.env.example` (without values) to share required keys safely with your team.

---

## 5.4 Validate JWT

Add a validation method to your `JwtService`:

```java
public boolean validateToken(String token) {
    try {
        var claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getExpiration().after(new Date());
    } catch (JwtException e) {
        return false;
    }
}
```

---

### 🔹 Manual Validation Endpoint

```java
@PostMapping("validate")
public boolean validate(
        @RequestHeader("Authorization") String token
) {
    var tokenWithoutBearer = token.replace("Bearer", "");
    return jwtService.validateToken(tokenWithoutBearer);
}
```

---

## 🔹 Using Filters for Automatic JWT Validation

A **Filter** runs before your controller to inspect, validate, or modify requests.

### Example: Logging Filter

```java
@Component
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Request: " + request.getRequestURI());
        System.out.println("Method: " + request.getMethod());
        filterChain.doFilter(request, response);
        System.out.println("Response: " + response.getStatus());
    }
}
```

---

### JWT Authentication Filter

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = authHeader.replace("Bearer ", "");

        if (!jwtService.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                jwtService.getEmail(token),
                null,
                null
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
```

---

### 🔍 Explanation

1. Gets the `Authorization` header.
2. Checks if it starts with `Bearer`.
3. Extracts and validates the JWT.
4. If valid → creates an `Authentication` object and stores it in Spring’s context.
5. Proceeds with the filter chain.

✅ After this filter runs, Spring Security knows who the current user is.

---

### 🔹 Add Filter to SecurityConfig

```java
http
    .csrf(AbstractHttpConfigurer::disable)
    .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests(auth -> auth
            .requestMatchers("/carts/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/users").permitAll()
            .requestMatchers("/auth/validate").permitAll()
            .requestMatchers("/auth/login").permitAll()
            .requestMatchers("/error").permitAll()
            .anyRequest().authenticated()
    )
    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

return http.build();
```

---
### Accessing the Current User :
ths the way how you can get you User data in the Url :
in the AuthController :
```java
 @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var email = (String) authentication.getPrincipal();

        var user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto =userMapper.toDto(user);

        return ResponseEntity.ok(userDto);
    }
```
---
### Access Token & Refresh Token :
when we generate token we should generate two tokens 
the first name access token should be short to expire (5 to 10 min)
and the second is  refresh token this is the token will send in the req when 
the token access expire should be have long time to expire (7 to 30 days)
so you should go to jwtService and write this : 

```java
public String generateAccessToken(UserDto userDto) {
        long tokenExpiration = 300; // token well expires in 5min
        return generateToken(userDto, tokenExpiration);
    }

    public String generateRefershToken(UserDto userDto) {
        long tokenExpiration = 60480; // token well expires in 7d
        return generateToken(userDto, tokenExpiration);

    }

    private String generateToken(UserDto userDto, long tokenExpiration) {
        return Jwts.builder()
                .subject(Long.toString(userDto.getId()))
                .claim("email", userDto.getEmail())
                .claim("name", userDto.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
```
and in the authController you should add the refresh token in the cookie (not the normal cookie)
this cookie is don't accessible for code javascript 
to do this we should add some config :

```java
  @PostMapping("login")
    public ResponseEntity<JwtResponseDto> auth(
            @Valid @RequestBody AuthUserDto authUserDto,
            HttpServletResponse response \\ add this  for add the cookie in the response

    ) {
        var user = userMapper.toDto(userRepository.findByEmail(authUserDto.getEmail()).orElse(null));
        if (user == null) {
            throw new UserNotFoundException();
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authUserDto.getEmail(),
                        authUserDto.getPassword()));

        String accessToken = jwtService.generateAccessToken(user); //generate the access token
        String refreshToken = jwtService.generateRefershToken(user); //generate the refresh token

        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // make the cookie accessible just for http req
        cookie.setPath("/auth/refresh");
        cookie.setSecure(true); // just for https
        cookie.setMaxAge(60480); // 7d
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponseDto(accessToken));
    }
```
Instead of use  hardcoded like this **cookie.setMaxAge(60480)**
we can save the variable in the .env to make it easy to change  
and instead of write many variable in the class we can create class 
content all the variable related with jwt : "JwtConfig" in the config file

```java
@Configuration
@ConfigurationProperties(prefix = "spring.jwt")
@Data
public class JwtConfig {
    private String secret;
    private int tokenRExpiration;// token well expires in 7d || token refresh expiration
    private int tokenAExpiration;/// token well expires in 5min

}
```
### Refreshing the token :
to refresh the token we need add new method can refresh the token and get new token access 
now i write the methode in the controller :

```java
@PostMapping("refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(value = "refreshToken") String refreshToken) {
        if (!jwtService.validateToken(refreshToken))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("error", "the token is invalid"));
        var userId = jwtService.getId(refreshToken);
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken = jwtService.generateAccessToken(userMapper.toDto(user));
        return ResponseEntity.ok(new JwtResponseDto(accessToken));

    }
```
and don t forget add the token refresh as parameter in the http cookies in postman 

---
### Adding Role To Users :
we need to divise users to ADMIN or User.
In the big Application we should add new table named Roles but in small project no need it we can just
add column in user table this is the first step 
and the second step is add the Role enum in the entities package :
```java

public enum Role {
    USER,
    ADMIN
}
``` 
and add field Role in the User entity 
```java
 @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
```
and after this  you should go to the jwt service to insert this Role 
in json web token 
```java
  return Jwts.builder()
                .subject(Long.toString(user.getId()))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("Role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(jwtConfig.getSecretKey())
                .compact();
```
andd finally you should go  to userController and in the registerUser
set the 'User' to new users :
```java
  user.setRole(Role.USER);
```

---
#### Role based Authorization 

first we should add this two exception hundler in the security config :

```java
 .exceptionHandling(c -> {
                                        c.authenticationEntryPoint(
                                                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                                        c.accessDeniedHandler(((request, response, accessDeniedException) -> response
                                                        .setStatus(HttpStatus.FORBIDDEN.value())));
                                });
```

explaining for this two methods :

### 🔐 1. `.authenticationEntryPoint(...)`

```java
c.authenticationEntryPoint(
    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
);
```

#### ✅ **When is it triggered?**
→ When an **unauthenticated user** tries to access a **protected endpoint**.

#### 📌 Example:
- You call `GET /admin/dashboard`  
- But you **don’t send a JWT** (or send an invalid/expired one)  
- Spring Security sees: “This user is **not logged in**, but the endpoint requires login.”

#### 🎯 What does it do?
- Sends a **401 Unauthorized** HTTP response.
- **Does not redirect** to a login page (which is good for REST APIs!).
- Clean, stateless response: just `401` + empty body (or you can customize it).

> 💡 Without this, Spring Security would try to redirect to `/login` (which doesn’t make sense for an API).

#### ✅ Why you use it:
> To tell **unauthenticated users**: “You need to log in first (send a valid token).”

---

### 🔒 2. `.accessDeniedHandler(...)`

```java
c.accessDeniedHandler((request, response, accessDeniedException) -> 
    response.setStatus(HttpStatus.FORBIDDEN.value())
);
```

#### ✅ **When is it triggered?**
→ When an **authenticated user** tries to access a resource they’re **not allowed to**.

#### 📌 Example:
- You’re logged in as a **regular user** (role: `USER`)
- You call `DELETE /admin/users/123` (which requires `ADMIN` role)
- Spring Security checks your role → **you don’t have permission**

#### 🎯 What does it do?
- Sends a **403 Forbidden** HTTP response.
- Unlike 401, this means: “You’re logged in, but **you don’t have rights** to do this.”

#### ✅ Why you use it:
> To tell **authenticated but unauthorized users**: “You can’t do that — access denied.”

---

### 🆚 Key Difference: `401` vs `403`

| Status | Meaning | Who? | Spring Security Trigger |
|-------|--------|------|--------------------------|
| **401 Unauthorized** | ❌ Not authenticated | Anonymous user | `AuthenticationEntryPoint` |
| **403 Forbidden** | ✅ Authenticated, but ❌ not authorized | Logged-in user with insufficient privileges | `AccessDeniedHandler` |

---

### 🧠 Behind the Scenes

Your config:

```java
.exceptionHandling(c -> {
    c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    c.accessDeniedHandler((req, res, ex) -> res.setStatus(HttpStatus.FORBIDDEN.value()));
});
```

Tells Spring Security:

> “When something goes wrong:
> - If the user **isn’t logged in** → return **401**
> - If the user **is logged in but lacks permission** → return **403**”

---
lets back to "Role based Authorization" :
the first thing the admin should have controller 
named AdminController
and after this  you should add new autherization 
```java
 .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
```
and after you should add method to get the role from token 
we use it after in the jwtAuthenticationfilter 
```java
 public Role getRoleFromToken(String token) {
        return Role.valueOf(getPayload(token).get("Role", String.class));
    }
```
and finally we need add the authority in the jwtAuthenticationfilter
```java
 var role = jwtService.getRoleFromToken(token);

 var authentication = new UsernamePasswordAuthenticationToken(
         userId,
         null,
         List.of(new SimpleGrantedAuthority("ROLE_" + role)));
```
### Refactoring the JwtService : 
instead of write all the code in the jwtService we can divise into 2 part
part in the services/Jwt  class
```java
public class Jwt {
    private final Claims claims;
    private final SecretKey key;

    public Jwt(Claims claims, SecretKey key) {
        this.claims = claims;
        this.key = key;
    }

    public boolean isValid() {
        return claims.getExpiration().after(new Date());
    }

    public Role getRoleFromToken(String token) {
        return Role.valueOf(claims.get("Role", String.class));
    }

    public Long getId() {
        String subject = claims.getSubject();
        // defensive checks: subject may be null, empty or the literal string "null"
        if (subject == null || subject.isEmpty() || "null".equalsIgnoreCase(subject.trim())) {
            throw new JwtException("Invalid token subject");
        }

        try {
            return Long.parseLong(subject.trim());
        } catch (NumberFormatException ex) {
            throw new JwtException("Invalid token subject: not a valid id", ex);
        }
    }

    public String toString() {
        return Jwts.builder().claims(claims).signWith(key).compact();
    }

}
```
and the other part in the jwtServcie :
```java

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getTimeOutA());
    }

    public Jwt generateRefershToken(User user) {
        return generateToken(user, jwtConfig.getTimeOutR());
    }

    public Jwt generateToken(User user, long tokenExpiration) {
        var claims = Jwts.claims()
                .subject(Long.toString(user.getId()))
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("Role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();
        return new Jwt(claims, (jwtConfig.getSecretKey()));
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Jwt parse(String token) {
        try {
            Claims claims = getClaims(token);
            return new Jwt(claims, jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }

    }

}
```
### Logging out users :
their two approaches to log out in jwt :
1. Client_side logout : 
->Delete the access token from memory of storage
->Remove the refresh token(by clearing the cookie)
->Simple to implement
->Tokens are valid until they expire !!!!!!!!
->Best for low-risk scenarios
2. Server_side logout :
->Store a list of active or removed tokens in a databases or cache
->when a user logs out , mark their token as invalid
->During each request,check if the token is blacklist
->Provides a true logout experience
->Adds complexity and require token look up on each request
->Best for high-security apps
---
### Using Auth Providers :
Instead of config security manully you can use Auth Providers like
Auth0 , Amazon Cognito , firbase authentication , Okta (is a third part)
this give you automatically use the feature like "email verification , password resets,account lockouts,social login ..."
but you have the choice !!!!!
---
we should add new exception handler to handle the exception about can't get the object in java 
"desirialization" exceptition 
so we add in the GlobalExceptionHandler class this :
```java
@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<Map<String,String>> handleUnreadableMessage()
{
return ResponseEntity.badRequest().body(
Map.of("error" , "Invalid request body")
);
}
```

---

## **5. Payment :**
we work with **Stripe** <br>
thier two ways to use Stripe : <br>
**Stripe Chekout** : <br>
-> Simpler, hosted solution .<br>
-> Redirect the user to Stripe<br>
-> Stripe handles all the complexity <br>
**Stripe elements**<br>
-> build our own checkout form<br>
-> Gives us full control<br>
-> More complex <br>

---

### 5.1 Overview of the checkout process :

![PaymentProcess](https://github.com/yassin-elkhamlichi/Spring-Boot-Doc-Rest-Api-dev-/PaymentProcess.jpeg)

**Decription** :

### 🌐 **The Full Journey: From "Checkout" to "Order Confirmed"**
*(All pieces connected: sessions, SDK, webhooks, security)*

#### **1. You Click "Checkout"**
- You add items to your cart and click **"Pay Now"**.
- Your browser sends this request to the **Spring Boot server**.
- The server:  
  → Creates a **temporary session** (like a digital "table number" for your visit).  
  → Saves your order in the **database** as `PENDING` (with a unique `order_id`).  
  → *Why session?* To remember your cart *during this browser session* (e.g., if you refresh the page).

---

#### **2. Server Asks Stripe: "Create a Payment Page!"**
- The server uses the **Stripe SDK** (a pre-built toolkit) to talk to Stripe:  
  → Attaches your `order_id` as **metadata** (a secret note only servers see).  
  → Requests a secure payment link.
- **Stripe responds** with:  
  → A unique **payment session ID** (Stripe’s internal reference).  
  → A **payment URL** (e.g., `https://checkout.stripe.com/pay/cs_abc123`).
- *Why SDK?* It handles encryption, API errors, and security automatically – no manual HTTP calls.

---

#### **3. You’re Redirected to Stripe’s Payment Page**
- The server tells your browser: **"Go here to pay!"** (via redirect).
- Your **session ID** travels with you (in a cookie) so the server remembers you.
- You see Stripe’s trusted page:  
  → Enter card details → Click "Pay $10".  
  → *Never* enters your site – Stripe handles PCI compliance.

---

#### **4. Stripe Processes Payment (Behind the Scenes)**
- **If payment succeeds**:  
  → Stripe **immediately sends a secret notification** (`webhook`) to your server.  
  → Stripe redirects your browser to: `yoursite.com/success?session_id=cs_abc123`.
- **If you cancel**:  
  → Stripe redirects to `yoursite.com/cancel`.

---

#### **5. Critical Step: Webhook Updates the Order (Server-to-Server)**
- **This happens even if you close the browser!**
- Stripe pings your server at `/webhook` with:  
  → Event type: `checkout.session.completed`.  
  → Your `order_id` (from metadata) and payment proof.
- Server **verifies the webhook** (using a secret key) to block hackers.
- Server **updates the database**:  
  → Changes order `#1001` from `PENDING` → `PAID`.  
  → *No session used here* – this is permanent and reliable.

the weebhook is when to sides notify it auto :
![Webhook](https://github.com/yassin-elkhamlichi/Spring-Boot-Doc-Rest-Api-dev-/WebHook.jpeg)]

---

#### **6. You Return to the App (Browser Redirect)**
- Your browser lands on `yoursite.com/success?session_id=cs_abc123`.
- Server:  
  → Uses the **Stripe SDK** to fetch payment status for `cs_abc123`.  
  → Checks the **database** (not session!) for order `#1001` status.  
  → *Why database?* In case the webhook arrived first (it usually does).
- Shows you: **"Payment successful! Order #1001 confirmed 🎉"**
- Your **session** is used only for display (e.g., showing your order summary).

---

### 🧩 **Why Every Piece Matters**
- **Session**: Short-term memory for *your browser experience* (e.g., cart persistence).
- **Stripe SDK**: The "magic translator" between your server and Stripe (handles complexity).
- **Webhook**: The **most critical piece** – ensures orders update *reliably*, even with bad internet or closed tabs.
- **Database**: The **source of truth** – sessions and webhooks both write to it, but *only the database matters* for order status.
- **Metadata**: The glue linking Stripe events (`cs_abc123`) to your orders (`#1001`).

---

### 5.2 **Stripe SDK** :
---

#### 5.2.1 Adding Stripe to the project:
first we add the depandency in the pom.xml :
```xml
<dependency>
            <groupId>com.stripe</groupId>
            <artifactId>stripe-java</artifactId>
            <version>27.1.0</version>
        </dependency>
```
and after we should get key secret from Stripe in the browser 
and inject it in our project (save it in the .env)
and after we should config the strip in the StripeConfig class :
```java
@Configuration
public class StripeConfig {
    @Value("${spring.stripe.key}")
    private String Key;
    
    @PostConstruct
    public void init(){
        Stripe.apiKey = Key;
    }
}
```

#### 5.2.2 Creating a checkout Session:
 in the OrderService in Checkout method we write this :
```java

@RequiredArgsConstructor // (this for don't create beans for webSitUrl') well create just for fields has final constraint
class{
@Value("${spring.webSiteUrl}")
private String webSiteUrl; // if use the localhost you should use the ngrok to transform you 
// local url to public url becouse stripe dont work with localhost  
public CheckOutResponseDto CheckingOut(CheckOutRequestDto data) throws StripeException {
        
        ...........................................
        ...........................................
        ...........................................
    ordersRepository.save(order);

    try {
        //Create a checkout session
        var builder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(webSiteUrl + "/checkout-success?orderId=" + order.getId())
                .setCancelUrl(webSiteUrl + "/checkout-cancel?orderId=" + order.getId());
        order.getOrder_items().forEach(item -> {
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("usd")
                                    .setUnitAmountDecimal(
                                            item.getUnit_price()
                                                    .multiply(BigDecimal.valueOf(100)))
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(item.getProduct().getName())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
            builder.addLineItem(lineItem);
        });
        var session = Session.create(builder.build());
        cartRepository.delete(cart);
        return new CheckOutResponseDto(order.getId(), session.getUrl());
    } catch (StripeException e) {
        ordersRepository.delete(order);
        throw new RuntimeException(e);
    }
}
}
```

--- 
### Hundle the Stripe Exception :
thier many exception related with stripe like : <br>
- Invalid Api Key
- Network issues
- Bad requests (e.g. negative amount)
- Stripe service downtime
so we need to hundle all this cases 
----
### Decoupling from Stripe :
the last way is bad becouse is not easy to maintenence
so we should to add new Interface in the service name "IPaymentGateway"

```java
public interface IPaymentGateway {
    CheckoutSession createCheckoutSession(Orders order);
}
```
then create class " CheckoutSession" 
```java
@AllArgsConstructor
@Getter
public class CheckoutSession {
    private String checkoutUrl;
}
```
and finally add class for the specific paymentGateway lake stripe,paybal..
```java
@Service
@RequiredArgsConstructor
public class StripePaymentGateway implements IPaymentGateway{
    @Value("${spring.webSiteUrl}")
    private String webSiteUrl;
    @Override
    public CheckoutSession createCheckoutSession(Orders order) {
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(webSiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(webSiteUrl +  "/checkout-cancel?orderId="+ order.getId());
            order.getOrder_items().forEach(item -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmountDecimal(
                                                item.getUnit_price()
                                                        .multiply(BigDecimal.valueOf(100)))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(item.getProduct().getName())
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();
                builder.addLineItem(lineItem);
            });
           var session =  Session.create(builder.build());
           return new CheckoutSession(session.getUrl());
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            throw new PaymentException();
        }
    }
}

```

and in the orderService will add the IPaymentGateway as field 
and use it :
```java
    private final IPaymentGateway paymentGateway;
 @Transactional
    public CheckOutResponseDto CheckingOut(CheckOutRequestDto data)  {
        var cart = cartRepository.findById(data.getCartId()).orElse(null);
        if(cart == null ){
            throw new CartNotFoundException();
        }
        if(cart.getItemCart().isEmpty()){
            throw new CartEmptyException();
        }
        Orders order = Orders.builder()
                .order_items(new ArrayList<>())
                .build();
        cart.getItemCart().forEach( itemCart ->
        {
            Order_items itemOrder = Order_items.builder()
                    .order(order)
                    .product(itemCart.getProduct())
                    .quantity(itemCart.getQuantity())
                    .unit_price(itemCart.getProduct().getPrice())
                    .total_amount(cart.getTotalPrice())
                    .build();
            order.getOrder_items().add(itemOrder);
        }
        );
        var user =authService.getCurrentUser();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setTotalAmount(cart.getTotalPrice().doubleValue());
        ordersRepository.save(order);

        try {
            //Create a checkout session
            var session = paymentGateway.createCheckoutSession(order);
            cartRepository.delete(cart);
            return new CheckOutResponseDto(order.getId() ,  session.getCheckoutUrl());
        } catch (PaymentException e) {
            ordersRepository.delete(order);
            throw e;
        }
    }
```
so if you Remarque here nothing about stripe and also control doesn't now anything about srtipe 
all the logic about stripe is inside the StripPaymentGateway
```java
@PostMapping
    public CheckOutResponseDto checkOut(
            @Valid @RequestBody CheckOutRequestDto request
            )
    {
        return orderService.CheckingOut(request);

    }
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException(){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a checkout session"));
    }
```
as a final enhance the code in the Stripe class we can make it like this for more cleans :

```java
package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Order_items;
import com.codewithmosh.store.entities.Orders;
import com.codewithmosh.store.exception.PaymentException;
import com.codewithmosh.store.payement.CheckoutSession;
import com.codewithmosh.store.payement.IPaymentGateway;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StripePaymentGateway implements IPaymentGateway {
    @Value("${spring.webSiteUrl}")
    private String webSiteUrl;

    @Override
    public CheckoutSession createCheckoutSession(Orders order) {
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(webSiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(webSiteUrl + "/checkout-cancel?orderId=" + order.getId());
            order.getOrder_items().forEach(item -> {
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });
            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            throw new PaymentException();
        }
    }

    private SessionCreateParams.LineItem createLineItem(Order_items item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item)
                )
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(Order_items item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(
                        item.getUnit_price().multiply(BigDecimal.valueOf(100)))
                .setProductData(
                        createProductData(item)
                )
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(Order_items item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName())
                .build();
    }
}

```
#### 5.2.3 Webhook EndPoint:
### Build the webhook endPoint :
in the controller we write this : 
```java
 @PostMapping("/webhook")
    public ResponseEntity<Void>  handleWebhook(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    ){
        try {
            var event = Webhook.constructEvent(payload,signature,webhookKey);
            System.out.println(event.getType());
            var stripObject = event.getDataObjectDeserializer().getObject().orElse(null);
            switch (event.getType()){
                case "payment_intent.succeeded"->{
                    var paymentIntent = (PaymentIntent) stripObject;
                    if (paymentIntent != null) {
                        var orderId = paymentIntent.getMetadata().get("order_id");
                        var order = ordersRepository.findById(Long.valueOf(orderId)).orElseThrow();
                        order.setStatus(Status.PAID);
                        ordersRepository.save(order);
                    }
                }
                case "payment_intent.failed" ->{

                }
            }
            return ResponseEntity.ok().build();
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
```
### Testing the webhook endPoint :
and we need install the stripe cli to test if the webhook work 
first install it and run this commends : 
```bash
 stripe login    
```
and get the authentication and after run this commend
```bash
stripe listen --forward-to http://localhost:8080/checkout/webhook
```
you will get the webhook secret api get it and add in .env

and after this let this terminal open and give him name like "Stripe server "
and open other terminal to test Stripe events
and run this to test :
```java
stripe trigger payment_intent.succeeded  
```
and after enter this to make webhook change in the database :
```java
stripe trigger payment_intent.succeeded  --add "payment_intent:metadata[order-id]=16"

```
---
!!! ALL THE LOGIC ABOUT CHECKOUT MOVE FROM ORDERSERVICE TO CHECKOUTSERVICE
---
I do some modification on the code in the payment feature i add parseWebhookRequest as 
method in the interface and after i will refactor code for it
---
### Organizing Code By Feature :
in the large application we find many packages and we can't find the related files 
easy so based from the **Cohesion** we use the Feature-based  to organizing our code instead of 
the layer-Based
**Cohesion** :
Things that are closely related should live together.
we applied this for the payment feature

---
## 4 Deploying :

---

## 4.1 Deploying The Database : 
their many platforms help you to deployment like : <br>
Amazon Web Services(AWS), Heroku , Digital Ocean , Railway<br>
We use **Railway** because have free plan
<br> So we go to site and connect it with our github account 
and in the search looking for mysql and click it, and after you should waiiiiiiiiit 
and you nice deploy you **MySql Server** and after we run the flyway to create schema in it

---
### 4.1.2 Managing Environments With Spring Profiles :

if you want to deploy  your database so you will have to environment (local and cloud )
so the file the configuration should divise to :  first wan include the general setting the second for dev and the third for production 
<br> application.yaml <br>
```java
spring:
  application:
    name: store

  datasource:
    url: jdbc:mysql://localhost:3306/store_api?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: ${USER_NAME}
    password: ${PASSWORD}

  jpa:
    show-sql: true
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

  jwt:
    secret: ${JWT_SECRET}
    TimeOutR: ${Refresh_Token_Expire}
    TimeOutA: ${Access_Token_Expire}

  profiles:
    active: dev // first we applied the dev settings

  stripe:
    secretKey: ${STRIPE_SECRET_KEY}
    webhookSecretKey: ${STRIPE_WEBHOOK_SECRET_KEY}

  webSiteUrl: "https://dorthey-unlosable-irrecoverably.ngrok-free.dev/:8080"

```
application-dev.yaml : 
```java
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/store_api?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: ${USER_NAME}
    password: ${PASSWORD}

  jpa:
    show-sql: true
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

  webSiteUrl: "https://dorthey-unlosable-irrecoverably.ngrok-free.dev/:8080"

```
application-prod.yaml :
```java
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${USER_NAME}
    password: ${PASSWORD}

  webSiteUrl: https://mystore.com
```

---

### 4.1.3 packaging the App for Production : 

We need to add this plugin in the maven file 
```maven
  <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>1.6.3</version>
                        </path>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${lombok.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
```

#### 🛠️ Think of annotation processors like code chefs:

Lombok and MapStruct are tools that help generate Java code while you're building your app.
Once they’ve done their job (during mvn compile or mvn package), they leave the kitchen.
What gets deployed is the final cooked meal (your .class files inside a JAR/WAR) — no chefs needed at the dinner table (i.e., in production).
##### 🔧 So why is it in your pom.xml?

Because Maven needs to know:

“While compiling, please use these tools to auto-generate code.”

But after compilation, those tools are completely useless — your app runs just fine without them.