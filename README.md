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
---
### Romarque in Exercice :
somthing when you add entitie for example named Cart even if you wrote
@Table(name = "Cart") hibernete will searche on table named cart
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

For store id type uuid to string in db yiou should add this 
in your entity
```java
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id" , columnDefinition = "CHAR(36)")
    private UUID  id;
```

---
if you want to get value to fielad in the dto you can write this
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
so to make just send one queryto get all carts and all items in carts
we do this go to CartRepository and add this 
```java
    @EntityGraph(attributePaths = "itemCart.product")
    @Query("select c from Cart c where c.id = :cartId ")
    Optional<Cart> getCartWithItemsAndProducts(@Param("cartId") UUID cartId);
```
the first line tell spring to get all items in carts and all products in carts 
and the second is the qeury to get cart

### Information expert principle : 
when you write a code in controle and 
and repeat same logic in many methods in the same 
controller you should to respect this princiiple
so you should move this logic to service
or to Expert class (in my case Cart is expert on ItemCart)
so the method calcul Total price for all ietms should be
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
summery is principe tell : classes that only have data 
so that mean "information expert principle " should wrote in service rolated with expert class

### Rich Domain :
summery is principe tell : classes that  have data and behavior :
Data = State/Properties
id, items, status, total

product, quantity, price

Behavior = Business Logic/Methods
addItem(), completeOrder(), calculateTotal()

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
and after : 
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
with code in the service :
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
and don't forget what you should write in the exception classes
```java
public class CartNotFoundException extends RuntimeException {}
public class ProductNotFoundException extends RuntimeException {}
```
and in Controller class you should add this :
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

## Documenting Apis with Swagger : 
A tool that automatically generate documentation for your apis
first ypu should add this depandency :
```xml
   <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>2.8.13</version>
   </dependency>
```
and after to acces to swagger you should 
go to http://localhost:8080/swagger-ui.html
and after you can see all the Apis in your project
and also you can test each Api
if you want to change the names of Controller in swagger 
we should go to controller and add this annotation
```java
@Api(tags = "Cart")
public class CartController {}
```
and i can also add description for each endpoint
just add this annotation

```java
import io.swagger.v3.oas.annotations.Operation;

@PostMapping("/{id}/item")
@Operation(summary = "Add item to cart")
public ResponseEntity<ItemCartDto> addToCart()
```
and also you can add desc for param using @Parameter annotation

```java
    @Operation(summary = "Add item to cart")
    @PostMapping("/{id}/item")
    public ResponseEntity<ItemCartDto> addToCart(
            @Parameter(description = "id of cart")
            @PathVariable UUID  id,
            @RequestBody AddItemToCartReqeustDto data
            ) {
        var cartItemCartDto = cartService.addToCart(id, data.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemCartDto);
    }
```

---

## How i can work with  Files IN REQ :
 first when i send the req in postman should be Post 
 and in the body reqeust i dont choose send data as json 
 i choose data form option and first in the key i chose tyoe to file instead text 
 and after in the value i select the file
 and after if i want add object i add it 
 but i need to map it from string to object
 like this in controller :
 ```java
    @Value("${server.port}") String port;
    @Value("${server.server}") String server;
 @PostMapping("/upload")
public ResponseEntity<Map<String,String>> uploadFile(
        @RequestPart("file") MultipartFile file,
        @RequestPart("client") String ClientString
        ){
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = objectMapper.readValue(ClientString, Client.class);
        String path = "src/ressoruces/static/uploads/"+cllient.getId()+".jps";
        path.transferTo(Path.of(path));
        client.setImage("http//:"+server+"/"+port+"/uploads/"+cllient.getId());
        return client
}
 ```
the ObjectMapper is a class from jackson library include in spring
can mapper  json to object
the url for image not work you should add endpoint for it

```java
import org.springframework.web.bind.annotation.GetMapping;

@GetMapping("photos/{id}")
public ResponseEntity getPhoto(@PathVariable("id") Long id){
    String path = "src/ressoruces/static/uploads/"+id+".jps";
    SystemFileRessource fileRessource = new SystemFileRessource(path);
    if(!fileRessource.exists())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    return ResponseEntity.type(MediaType.IMAGE_JPEG).ok(fileRessource);
}
```
SystemFileRessource is a class that i create to get the file from the system

---
# 5 Spring Security : 
First you should add Spring security depandency in your pom file
and after you get endpoint login automatically generate page login 
and pass and name for user

## Authintication Methods :
1 . **Session_based** : 
user send Post/login to server and server check credentials if is valid server will
create session file where stored information about user and return session id to
user and after this browser (client side) storage session id in the cookie
is not scarbed becouse more users mean more storage in the server 
here we where need token based  authentication
2. **Token_based** : (JWT) (to see how exactly token like you can go to this wibsite https://www.jwt.io/)
Rest Api use this method .
so token work like this : client send Post/login to server and server check credentials if is valid
server will craete token(like password centent all the thing about user) and return it to user
and after if user want to send and other req  is autimatically send req with  auth = {token}
so for summery the key different is in token the token saved in client and server just decoudage it and extract info 
and after compar it with the data in bd
---
now we need to create package named confiig
and add file to config the srping security
so when we went to config spring security 
we nedd to use 3 things Csrf and **stateless session** so what this ?
when we just learn we don't use the session/coockies
so that named stateless session and the RestApi don't use session
named Stateless RestApi 
and **CSRF** is mean CSRF (Cross-Site Request Forgery) is an attack where a malicious site tricks a user’s browser 
into making unwanted requests to your app using the user’s active session
so when we dont use session we need to disbale csrf (in case we use jwt or dont use anything)
in the package config we create class named SecurityConfig
and the third option is **Authorization**
## 3.1 what is the different between Authentication and Authorization ? :
### 🔑 1. **Authentication = "Who are you?"**

> ✅ **Verifying identity** — proving you are **who you say you are**.

#### Examples:
- Logging in with **email + password**
- Scanning your **fingerprint**
- Using **Google Sign-In**
- Entering a **one-time code (2FA)**

#### In code:
When you send:
```json
{ "email": "you@example.com", "password": "secret123" }
```
→ The server checks: *"Is this user real? Is the password correct?"*  
✅ If yes → **Authenticated!**

> 🧠 Think: **AuthN = "Are you really you?"**

---

### 🚪 2. **Authorization = "What are you allowed to do?"**

> ✅ **Checking permissions** — now that we know **who you are**, what can you access?

#### Examples:
- A regular user **can’t delete** another user’s post
- An **admin** can access `/api/admin/users`
- A **guest** can only view public pages
- A **paid user** can download premium content

#### In code:
After login, you try to access:
```
DELETE /api/posts/123
```
→ Server checks: *"Is this user the owner of post 123? Or an admin?"*  
✅ If yes → **Authorized!**  
❌ If no → **403 Forbidden**

> 🧠 Think: **AuthZ = "Are you allowed to do this?"**

---
### code sending if each one fild :
> 💡 **401 = "You’re not logged in (or token is bad)"**  
> 💡 **403 = "You’re logged in, but you don’t have permission"**

---


### 💬 summery:
- **Authentication** = **Login** (proving you are you)
- **Authorization** = **Permissions** (what you’re allowed to do after login)

You **always authenticate first**, then authorize.
---
let's return now to configuration file 
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // first we need to make session stateless
        //second Disable csrf
        //third disable default login page
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
this method run automatically in the runtime
authorizeHttpRequests(c ->
c.anyRequest().permitAll()); this line give us access for  all the endpoint  
in our project without authintictaion 
if we want to add specifice reqeust 
```java
  http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/carts/**").permitAll()
                         .requestMatchers("/error").permitAll()  // IMPORTANT!
                         .anyRequest().authenticated()  // Change back to authenticated
                 );
return http.build();
```
this line tell spring security cart/** is public but any other 
request need to be authenticated
Wow this  works :
When Spring Security blocks a request with 403, it tries to redirect to the /error endpoint to show the error page.
But /error was ALSO blocked by .anyRequest().authenticated(),
so it created an infinite loop or just showed "Forbidden"!
The fix:
java.requestMatchers("/error").permitAll()  // ✅ Allows error page to show
Now when there's an error, Spring can actually display it instead of blocking the error page itself!
---
### Hashing :
to make your password more secure you can use hash
first you should add new bean in you config file like this :
```java
 @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
```
PasswordEncoder is an interface that provides methods to encode and decode passwords.
and BCryptPasswordEncoder is a class that implements PasswordEncoder and uses the BCrypt algorithm to encode and decode passwords.
and after you should add this line in the controller before you save the user :
```java
user.setPassword(passwordEncoder.encode(user.getPassword()));
```

## 5.2 Authentication Management
I use service to manage authantification but spring security 
can automatically manage authantification for you
instead of this : 
```java
@Service
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    public String authUser(AuthUserDto authUserDto , UserRepository userRepository){
        var user = userRepository.findByEmail(authUserDto.getEmail());
        if(user == null)
            throw new UserNotFoundException();

        if(!passwordEncoder.matches(authUserDto.getPassword(), user.getPassword()))
            throw new InvalidPasswordException();

        return "Authenticated";
    }
}
```
this logic exist in spring security 
we have the  **AuthenticationManager** this interfac super interface for all authentication managers
and we have **AuthenticationProvider** implemented from it and we have **DaoAuthenticationProvider**
extends from the last one and this last one has to fields : 
**userDetailsService** and **passwordEncoder** and the methode authenticate() 
**userDetailsService** is an object find the user by String (byUserName)
but you can use another field like email
so for simple userDetialsService get the user from bd if exist
and after the DaoAuthenticationProvider use the passwordEncoder to compare the password
and after generate response automatically and also hundle exception  for you
userDetialsService this method have methode named loedByUsername()
and this method return **UserDetails**
so what is **UserDetails** ? 
is in interface has all the methods you need for user like 
isAccountNonExpired(), isAccountNonLocked(), isCredentialsNonExpired(), isEnabled() ...
and also 2 fields : username and password 
soo the new version of our service is : 
```java
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        var user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found"));
        return new User(user.getEmail(),
                user.getPassword(), 
                Collections.emptyList());
    }
}
```
but we need some things in config file to make it work
```java
 public final UserDetailsService userDetailsService;
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
```
so how this work?
first we give the passwordEncoder from the Bean we already wrote 
and what about UserDetailsService ? 
we declared userService as service and is implement from
UserDetailsService  so in the runtime spring will injected  our UserSrvice into
SecuirtyCnfig and save it in the field userDetailsService.
After this we need to  add this line to the config file :
```java
   @Bean
public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
) throws Exception {
    return  config.getAuthenticationManager();
}
```
This method retrieves the pre-configured AuthenticationManager from Spring Security,
which uses your DaoAuthenticationProvider and UserDetailsService internally
And after we need to add this in the controller :
private final AuthenticationManager authenticationManager;
```java
    private finale AuthenticationManager authenticationManager;
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
we should add management field in the controll becouse is repsonsable for 
authintication 
so in summery this work like this :
The controller sends the credentials to the AuthenticationManager.

The AuthenticationManager delegates the work to the configured DaoAuthenticationProvider.

The DaoAuthenticationProvider uses:

your custom UserDetailsService to load the user from the database.

your configured PasswordEncoder to validate the password.

If authentication succeeds, Spring Security marks the user as authenticated in the security context automatically.
If it fails, Spring throws exceptions like:

UsernameNotFoundException → user not found

BadCredentialsException → invalid password

---
## 5.3  Generate Json Web Tokens(JWT) : 
first you should add this depandency :
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
and after you should add new service named  JwtService
```java
@Service
public class JwtService {
    final long tokenExpiration = 86400; // token well expires in 24 hours
    public String generateToken(String email){
    return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
            .signWith(Keys.hmacShaKeyFor("secret".getBytes()))
            .compact();

    }
}
```
Expliation of the code : 
```java
public String generateToken(String email){
```
> Declares a method that takes an `email` (used as the user identifier) and returns a JWT as a `String`.

```java
    return Jwts.builder()
```
> Starts building a new JWT using the **Java JWT (jjwt)** library’s fluent API.

```java
            .subject(email)
```
> Sets the **`sub` (subject)** claim of the JWT to the user’s email — typically used to identify the principal (user).

```java
            .issuedAt(new Date())
```
> Sets the **`iat` (issued at)** claim to the current timestamp, indicating when the token was created.

```java
            .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
```
> Sets the **`exp` (expiration)** claim: the token will expire after `tokenExpiration` seconds (since `1000 * tokenExpiration` converts seconds to milliseconds).

```java
            .signWith(Keys.hmacShaKeyFor("secret".getBytes()))
```
> Signs the JWT using **HMAC-SHA256** with a hardcoded secret key (`"secret"`).  
> is convert constant value "secret" to Bytes
> ⚠️ **Warning**: In production, never hardcode secrets — use environment variables or a secure config.

```java
            .compact();
```
> Finalizes and **serializes the JWT** into a compact, URL-safe string (e.g., `xxxxx.yyyyy.zzzzz`).

```java
}
```
> End of method.

✅ **Result**: A signed, time-limited JWT string that represents an authenticated user (identified by email).

and after you should add new dto named JwtResponseDto
```java
@Data
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
}
```
and after we return to authController and 
change the return type of auth method to JwtResponseDto
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
but this type of secret show error becouse is not secure