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
---
 java object  --> json named **serialization**
 json --> java object named **deserialization**
 **Customizing Response** :
 we have many annotation for controll json like :
 @JsonIgnoreProperties(ignoreUnknown = true)
 @JsonInclude(JsonInclude.Include.NON_NULL)
 @JsonProperty("name")
 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 
 for example here :
```jav
public class UserDto {
 private Long id;
 private String name;
 private String email;
 }
``` 
we cant remove id field from json because we need it for update user
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
also we can use @JsonInnclude annotation for ignore null fields
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
### Extracting Query Parameters :
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
this method use query parameter to sort users by email or name
-> the parametre required is tell teh url is sort is required or not
so you can use this url :
http://localhost:8080/users?sort=email
or just
http://localhost:8080/users
-> the parametre default value is tell the default value of sort parameter
-> the parametre name is tell the name of sort parameter
if you change sortBy to another word the url still work becouse you declared name = "sort"

---
### what i learn in exercice :
```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"category"})
    public List<Product> findByCategory_id(Byte categoryId);
}

###########
@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId" , source = "category.id" )
    public ProductDto toDto(Product product);
}
```
when you have rolation manytoone you can use @Mapping(target = "categoryId" , source = "category.id" )
this dont get category object , but get the id from gategory
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

### Extracting Request Headers :
```java
public List<ProductDto> getAllProducts(
@RequestHeader(required = false , name("X-API-KEY") String apiKey )
@RequestParam(required = false) Byte categoryId) {
}
```
this annotation is about req Header help us to extract request header and use it in our method
### Extracting Request body :
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
why we add RegisterUserRequest not userDTO ? becouse in dto we dont have 
password field so we need add new class in the DTO specific for create user
```java
@Data
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
}
```
and you should add new method in mapper :
```java
User toEntity(RegisterUserRequest registerUserRequest);
```
this get registerUserRequest and return user 
and save user in the bd
and after we call the toDto method for get userDTO
and after we fetch it in the postman
-> now we try to update User in bd based postman
first in postman you should choose 
Put or patch as request method
and after you should add methode in the controller like this 
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
and after you should add new method in mapper like this :
```java
void update(UpdateUserDto updateUserDto,@MappingTarget User user);
```

so this method update user with data from postman mapstruct generate automaticlly this 
method for you

🧠 How MapStruct “knows” what to do
1. Method Name Matters — But Not Alone
   MapStruct doesn’t just look at the name — it looks at:

✅ Method name (e.g., update, toDto)
✅ Parameter types
✅ Return type
✅ Annotations like @MappingTarget
dont forget create UpdateUserDto for choose wish field you want to update
-> if you want delete user you should add this method in controller :
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
🔹 What is ResponseEntity?
ResponseEntity<T> is a generic class in Spring that represents the entire HTTP response — not just the body, but also:

✅ Status code (e.g., 200 OK, 404 Not Found, 500 Internal Server Error)
✅ Headers (e.g., Content-Type, Location, custom headers)
✅ Response body (your actual data, like JSON, XML, etc.)
It gives you full control over what your REST endpoint returns.
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
and create new class in dto 
```java
@Data
public class ChangePasswordReqeust {
    private String oldPassword;
    private String newPassword;
}
```
---
-> i work on Product and in update exist same different
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
why the different happen?
becouse in user we RegisterUserDto class and this class  have password field
and not include id so here when i call class have id 
the implemantion for update method in mapper class 
Do change also for id field but for user do just for 2 fields email and 
password so for resolve this problem you should add this annotation :
```java
@Mapping(target = "id", ignore = true)
void update(ProductDto productDto ,@MappingTarget Product product);
```
this tell mapstruct to ignore id field when implement 
the update method from interface ProductMapper
---
# 4 Validation :
## 4.1 Jakarta validation :
instead of check fields manually we can use jakarta validation
for check for example if field emty or null or is string  ...
**String Validation**
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

**Number Validation**
```java
@Min(value = 0)
@Max(value = 100)
private int age;
@Positive
@PositiveOrZero
@Negative
@NegativeOrZero
```
**Date  Validation**
```java
@Past
@PastOrPresent
@Future
@FutureOrPresent
private Date birthDate;
@DateTimeFormat(pattern = "yyyy-MM-dd")
```
**General validation**
```java
@NotNull
```
!!! to use jakarta validation you should add this dependency in pom.xml
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
after useing this annotation you should go to the method use this class and add
@Valid annotation like this :
```java
@PostMapping
    public UserDto createUser(
            @Valid @RequestBody RegisterUserRequest data) {
                .....
    }
```
if some validation error happen you will see error in postman 
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
you dont call this method in create method becouse this 
method will call automatucly  when validation error happen
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

So **3 constraint violations** are found.

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
!!!!!! Problem : 
if i want do this in another controller i should repeat the same logic in all the
controllers !!!!!!!!
**Global Error Handling**
the idea instead write the same logic in many classes
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
somthing we need more control for the validation 
so you can create some annotation 
### 4.2 Implementing Custom Validation
First you should create new package named validation
and in this package you should create new annotation name the name you want to write it in 
the dto class 
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
and after you should  create new class named LowerCaseValidator
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
### Validating Business Rules :
if we want to check if email unique we need to use business rules
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
this check if email unique or no .
why we dont use custom  annotation :
becouse we need enter in the method and check if email unique or no
but when you use the validation jakarta tou dont enter the method and jakarata check it before
enter the method 