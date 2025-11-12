# **Spring Boot Documentation – REST API Development**

---

## **1. Spring MVC**

Spring MVC is part of the **Spring ecosystem**.
It’s a **framework for building web applications** that handles **HTTP requests and responses**.

**MVC = Model, View, Controller**

* **Model:** Data + logic
* **View:** What the user sees
* **Controller:** Java class acting as the *middleman*

---

### **1.1 Controller**

```java
@Controller
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }
}
```

* `@Controller` → tells Spring to create a bean for this class at runtime.
* `@RequestMapping` → defines which method handles a given HTTP request.

---

### **1.2 Template Engine**

To make views **dynamic**, we need a **template engine** — a tool that generates HTML pages dynamically.

Examples:
**Thymeleaf**, FreeMarker, Velocity, JSP, JSTL.

#### **Using Thymeleaf**

Thymeleaf is similar to JSP but more powerful.

**Step 1:** Add Thymeleaf dependency in `pom.xml`.
**Step 2:** Add the namespace in your HTML file:

```html
<meta charset="UTF-8" xmlns:th="http://www.thymeleaf.org">
```

**Step 3:** Use Thymeleaf attributes:

```html
<h1 th:text="'Hello ' + ${name}"></h1>
```

**Step 4:** Pass data to the model in your controller:

```java
@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "Yassine");
        return "index";
    }
}
```

✅ Make sure to import:

```java
import org.springframework.ui.Model;
```

---

## **1.3 REST API**

---

### **1.3.1 What is a REST API (RESTful API)?**

**REST** = *Representational State Transfer*
It’s an architectural style for designing web services using **HTTP methods** (`GET`, `POST`, `PUT`, `DELETE`, etc.) to work with **resources** (like users, products, orders).

#### **Key Characteristics of REST**

| Principle                      | Description                                                                             |
| ------------------------------ | --------------------------------------------------------------------------------------- |
| **Stateless**                  | Each request contains all necessary information; no session state stored on the server. |
| **Resource-based**             | Everything is treated as a *resource* (e.g., `/users/123`).                             |
| **Uses HTTP methods**          | `GET` = read, `POST` = create, `PUT/PATCH` = update, `DELETE` = delete.                 |
| **Uses standard status codes** | `200 OK`, `201 Created`, `404 Not Found`, etc.                                          |
| **Returns data (not views)**   | Usually JSON or XML — not HTML.                                                         |

> Example:
> `GET /api/users/5` → returns `{ "id": 5, "name": "Alice" }`
> Here, `/users` is the *resource endpoint*.

---

### **1.3.2 Normal API vs REST API in Spring**

In Spring Boot, you can build two main types of controllers:

#### **1. `@Controller` → Traditional Web Controller**

* Used for **server-rendered web apps** (returns HTML views).
* Methods return **view names** mapped to templates.
* Uses `Model` to pass data to the view.

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

> Returns **HTML**, not JSON.

---

#### **2. `@RestController` → REST API Controller**

* Designed for **RESTful web services**.
* Automatically returns **data (JSON)** instead of HTML views.
* Equivalent to `@Controller + @ResponseBody`.

```java
@RestController
public class UserApiController {
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.getAll(); // automatically serialized to JSON
    }
}
```

> Returns **JSON** — ideal for mobile apps, React, Angular, etc.

---

### **✅ When to Use Which**

| Use Case                                                  | Controller Type   |
| --------------------------------------------------------- | ----------------- |
| Building a **website** with HTML views                    | `@Controller`     |
| Building a **backend API** for frontends or microservices | `@RestController` |

---

### **✅ Summary**

* **REST API:** Data-only API using HTTP methods (returns JSON/XML)
* **`@RestController`:** Builds REST APIs easily
* **`@Controller`:** Used for server-rendered HTML pages

---

### **Note**

```java
public List<User> getAllUsers() {
    return userRepository.findAll();
}
```

The return type of `findAll()` is **Iterable**, which is a parent interface of `List`.
So:

> Every `List` is an `Iterable`, but not every `Iterable` is a `List`.
> That’s why `Iterable` works perfectly here.

---

## **1.3.3 Postman**

### **What is Postman?**

* 🌐 Browser → good for quick `GET` checks
* 🛠️ Postman → essential for real API testing (`POST`, `PUT`, `DELETE`, etc.)

**Postman** is a free tool for **testing, building, and documenting REST APIs**.

Think of it as a **remote control for your backend** — you can send requests and see responses instantly.

---

### **Why Use It?**

✅ Test your API without frontend
✅ Debug endpoints quickly
✅ Send JSON data easily
✅ Save and organize requests
✅ Automate API tests
✅ Generate documentation

---

### **Example**

You built `/api/users` in Spring Boot.
In Postman:

1. Method: `POST`
2. URL: `http://localhost:8080/api/users`
3. Body → raw → JSON

   ```json
   { "name": "Ali", "email": "ali@example.com" }
   ```
4. Click **Send** → see response (`201 Created`, etc.)

---

### **Who Uses Postman?**

* Backend developers
* Frontend developers
* QA testers
* DevOps engineers

---

### **✅ Bottom Line**

> **Postman = Your API playground.**
> Send requests, check responses, no frontend required.

---

## **Example: User REST Controller**

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
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
```

> `@PathVariable` extracts a value from the URI path.
> `ResponseEntity<T>` represents the full HTTP response (status, headers, body),
> giving you full control over what the endpoint returns.

---

# **DTO (Data Transfer Object)**

---

### 🔹 **What is a DTO?**

**DTO = Data Transfer Object**

It’s a **simple Java class** (often a *POJO* — Plain Old Java Object) used to **carry data** between layers of your application
(e.g., from controller → service → API → database).

A **DTO**:

* Has **fields** (usually private)
* Has **getters and setters** (or uses **Lombok**)
* Contains **no business logic**
* Has **no database annotations** (like `@Entity`)
* Is often **serializable** (to JSON/XML)

---

### 🔹 **Example: Without DTO (Problem!)**

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

If you return this directly from your REST controller:

```java
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userRepository.findById(id).orElseThrow();
}
```

🚨 **Problems:**

1. You expose **sensitive data** (`password`, `role`) to the client.
2. You can’t easily **rename fields** (e.g., `createdAt` → `created_at`).
3. Changing your entity structure can **break your API**.
4. You’re tightly coupling **database model** and **API model**.

---

### 🔹 **Solution: Use a DTO**

Create a simple `UserDto`:

```java
public class UserDto {
    private Long id;
    private String email;
    private String createdAt; // formatted as "2024-06-01"
    // Constructors, getters, setters, or use @Data (Lombok)
}
```

Map your entity to DTO inside the controller:

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

* No password leaked!
* Full control over exposed data
* Easy to format/transform (dates, enums, etc.)
* API remains stable even if DB changes

---

### 🔹 **When Should You Use DTOs?**

✅ **Use DTOs when:**

| Scenario                                     | Why                                      |
| -------------------------------------------- | ---------------------------------------- |
| Exposing data via REST API                   | Hide sensitive fields (`password`, etc.) |
| Receiving data from clients (`@RequestBody`) | Validate only what’s needed              |
| Communicating with external APIs             | Match expected formats                   |
| Returning computed or aggregated data        | e.g., `UserWithOrderCountDto`            |
| Avoiding lazy-loading issues                 | Prevent Hibernate proxies                |
| Versioning your API                          | e.g., `UserDtoV1`, `UserDtoV2`           |

❌ **You might skip DTOs when:**

* It’s a very **simple prototype**
* The **entity perfectly matches** client needs (rare)
* You’re using **GraphQL** (clients choose fields)

> 💡 **Rule:** If your API is public or used by a frontend — **always use DTOs**.

---

### 🔹 **Types of DTOs**

1. **Response DTO** – for sending data **to the client**

   ```java
   public class UserResponseDto { ... }
   ```

2. **Request DTO** – for receiving data **from the client**

   ```java
   public class CreateUserRequestDto {
       private String email;
       private String password; // will be hashed later
   }
   ```

3. **Internal DTO** – for **microservice communication**

---

> 🛡️ **Think of DTOs as your API’s public face — never expose your internal models directly!**

---

### **Example with Manual Mapping**

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
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    var userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
    return ResponseEntity.ok(userDto);
}
```

**Explanation:**

1. Fetch all users from DB → `List<User>`
2. Convert to stream → `Stream<User>`
3. Map each `User` → `UserDto`
4. Collect to list → `List<UserDto>`
5. Return as `Iterable<UserDto>` (valid for Spring REST)

---

### **Using Auto Mapping with MapStruct**

Instead of manual mapping, you can use **MapStruct** for automatic conversion.

#### **Add Dependencies**

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

#### **Create Mapper Interface**

```java
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
```

Spring will automatically generate the implementation for this interface.

#### **Use in Controller**

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
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(userMapper.toDto(user));
}
```

---

### **Serialization vs Deserialization**

* **Java → JSON** → *Serialization*
* **JSON → Java** → *Deserialization*

---

### **Customizing JSON Response**

We can control JSON output using annotations such as:

| Annotation                                    | Purpose                    |
| --------------------------------------------- | -------------------------- |
| `@JsonIgnoreProperties(ignoreUnknown = true)` | Ignore unknown JSON fields |
| `@JsonInclude(JsonInclude.Include.NON_NULL)`  | Exclude `null` fields      |
| `@JsonProperty("name")`                       | Rename fields in JSON      |
| `@JsonFormat`                                 | Format dates/times         |

#### Example

```java
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
```

We might want to hide or rename some fields:

```java
public class UserDto {
    @JsonIgnore
    private Long id; // Hide ID in JSON output
}
```

```java
public class UserDto {
    @JsonProperty("full_name")
    private String name;
}
```

```java
public class UserDto {
    @JsonProperty("full_name")
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
}
```

Format date fields:

```java
public class UserDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdAt;
}
```

---

✅ **Summary**

* DTOs separate **API models** from **database models**.
* Prevent **data leaks** and **tight coupling**.
* **MapStruct** simplifies conversion between objects.
* Jackson annotations (`@JsonIgnore`, `@JsonInclude`, etc.) help customize JSON responses.

---

## Extracting Query Parameters

```java
@GetMapping
public Iterable<UserDto> getAllUsers(
        @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy) {
    if (!Set.of("email", "name").contains(sortBy))
        sortBy = "name";
    return userRepository.findAll(Sort.by(sortBy))
            .stream()
            .map(userMapper::toDto)
            .toList();
}
```

This method uses a query parameter to sort users by **email** or **name**.

* The parameter `required` tells if the URL parameter `sort` is required or not.
* Example URLs:

    * `http://localhost:8080/users?sort=email`
    * `http://localhost:8080/users`
* The parameter `defaultValue` defines the default value for the sort parameter.
* The parameter `name` specifies the query parameter name.

  > If you rename `sortBy` to another word, the URL still works because `name = "sort"` is declared.

---

## What I Learned in This Exercise

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"category"})
    public List<Product> findByCategory_id(Byte categoryId);
}

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    public ProductDto toDto(Product product);
}
```

When you have a **many-to-one** relation, you can use:

```java
@Mapping(target = "categoryId", source = "category.id")
```

This doesn’t fetch the full category object — it only gets the **category ID**.

In your DTO:

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

## Extracting Request Headers

```java
public List<ProductDto> getAllProducts(
    @RequestHeader(required = false, name = "X-API-KEY") String apiKey,
    @RequestParam(required = false) Byte categoryId) {
}
```

`@RequestHeader` extracts a request header and makes it usable inside the method.

---

## Extracting Request Body

You can use **Body** in Postman to test creating resources like a user —
but if you want to actually create one in the DB, you must use **POST**.

```java
@PostMapping
public UserDto createUser(@RequestBody RegisterUserRequest data) {
    var user = userMapper.toEntity(data);
    userRepository.save(user);
    var userDto = userMapper.toDto(user);
    return userDto;
}
```

Why use `RegisterUserRequest` instead of `UserDto`?
Because in the DTO we don’t have the password field, so we create a new class:

```java
@Data
public class RegisterUserRequest {
    private String name;
    private String email;
    private String password;
}
```

In the mapper:

```java
User toEntity(RegisterUserRequest registerUserRequest);
```

---

## Updating a User (PUT / PATCH)

In Postman, choose **PUT** or **PATCH**, then add this method:

```java
@PutMapping("/{id}")
public ResponseEntity updateUser(
    @PathVariable(name = "id") Long id,
    @RequestBody UpdateUserDto data
) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    userMapper.update(data, user);
    userRepository.save(user);
    return ResponseEntity.ok(userMapper.toDto(user));
}
```

Add this in the mapper:

```java
void update(UpdateUserDto updateUserDto, @MappingTarget User user);
```

MapStruct auto-generates this method.

🧠 **How MapStruct knows what to do**

1. **Method name**
2. **Parameter types**
3. **Return type**
4. **Annotations like `@MappingTarget`**

Don’t forget to create `UpdateUserDto` to define which fields to update.

---

## Deleting a User

```java
@DeleteMapping("/{id}")
public ResponseEntity deleteUser(@PathVariable(name = "id") Long id) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    userRepository.delete(user);
    return ResponseEntity.noContent().build();
}
```

---

## 🔹 What is ResponseEntity?

`ResponseEntity<T>` represents the entire HTTP response:

* ✅ Status code (e.g., 200 OK, 404 Not Found)
* ✅ Headers
* ✅ Response body (your JSON/XML/etc.)

It gives full control over the REST response.

---

## Action-Based Updates

We use **PUT** and **PATCH** for updates, but some updates represent an **action**,
like *changing a password* or *submitting an approval request*.
In such cases, it’s better to use **POST**.

Example:

```java
@PostMapping("/{id}/change_password")
public ResponseEntity changePassword(
        @PathVariable(name = "id") Long id,
        @RequestBody ChangePasswordRequest data
) {
    var user = userRepository.findById(id).orElse(null);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    if (!user.getPassword().equals(data.getOldPassword())) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    user.setPassword(data.getNewPassword());
    userRepository.save(user);
    return ResponseEntity.ok().build();
}
```

DTO:

```java
@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
```

---

## Updating Product Example

```java
@PutMapping("/{id}")
public ResponseEntity<ProductDto> updateProduct(
        @PathVariable Long id,
        @RequestBody ProductDto data
) {
    var product = productRepository.findById(id).orElse(null);
    productMapper.update(data, product);
    var category = categoryRepository.findById(data.getCategoryId()).orElse(null);
    if (product == null || category == null) {
        return ResponseEntity.notFound().build();
    }
    product.setCategory(category);
    productRepository.save(product);
    return ResponseEntity.ok(productMapper.toDto(product));
}
```

### Why the difference?

Because in `User`, `RegisterUserDto` has no `id` field,
but `ProductDto` **does** include it — so MapStruct updates it too.

To fix this:

```java
@Mapping(target = "id", ignore = true)
void update(ProductDto productDto, @MappingTarget Product product);
```

This tells MapStruct to **ignore the ID field** when updating.

---

# 4. Validation

## 4.1 Jakarta Validation

Instead of manually checking fields, use **Jakarta Validation** to validate automatically.

### String Validation

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

### Number Validation

```java
@Min(value = 0)
@Max(value = 100)
private int age;

@Positive
@PositiveOrZero
@Negative
@NegativeOrZero
```

### Date Validation

```java
@Past
@PastOrPresent
@Future
@FutureOrPresent
private Date birthDate;

@DateTimeFormat(pattern = "yyyy-MM-dd")
```

### General Validation

```java
@NotNull
```

---

### Add Dependency

In `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

### Example

```java
@Data
public class RegisterUserRequest {
    @NotBlank(message = "Name is required")
    @Max(value = 50, message = "Name must be less than 50 characters")
    private String name;

    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
    private String password;
}
```

Use `@Valid` in your controller:

```java
@PostMapping
public UserDto createUser(@Valid @RequestBody RegisterUserRequest data) {
    ...
}
```

If validation fails, Postman will show an error —
but to display messages, add this method:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, String>> handleValidationErrors(
        MethodArgumentNotValidException exception
) {
    var errors = new HashMap<String, String>();
    exception.getBindingResult().getFieldErrors().forEach(
            error -> errors.put(error.getField(), error.getDefaultMessage())
    );
    return ResponseEntity.badRequest().body(errors);
}
```

This is automatically called when validation fails.

---

## 🔁 How This Error Handling Works

1. **Incoming HTTP POST Request**

   ```json
   {
     "name": "",
     "email": "not-an-email",
     "password": "123"
   }
   ```

2. **Spring Maps JSON → `RegisterUserRequest`**
   The object is created, but validation hasn’t run yet.

3. **`@Valid` Triggers Validation**
   Spring validates before executing the method.

4. **Validation Engine Checks Constraints**

    * `@NotBlank` → fails
    * `@Email` → fails
    * `@Size(min=6)` → fails

✅ **Result:** 3 constraint violations found.

---

## 5. Validation Fails → Throws `MethodArgumentNotValidException`

Since validation failed, **Spring does NOT call your `createUser` method body**.

Instead, it **throws a `MethodArgumentNotValidException`**, which is a built-in Spring exception that wraps:

* The invalid object (`RegisterUserRequest`)
* The `BindingResult` containing all validation errors

> 🚫 Your `userRepository.save(user);` line **never runs** if validation fails.

---

### 6. Spring Looks for an Exception Handler

Spring now searches for a method that can handle `MethodArgumentNotValidException`.

It finds your method:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String,String>> handleValidationErrors(...) { ... }
```

> ✅ This works because:
>
> * It's in the same controller (`@Controller` or `@RestController`)
> * Or in a `@ControllerAdvice` class (global handler)
> * And it declares the correct exception type

---

### 7. Your Handler Extracts Error Messages

```java
exception.getBindingResult().getFieldErrors().forEach(
    error -> {
        errors.put(error.getField(), error.getDefaultMessage());
    });
```

* `getFieldErrors()` returns a list of `FieldError` objects.
* For each error:

    * `error.getField()` → e.g., `"name"`, `"email"`, `"password"`
    * `error.getDefaultMessage()` → your custom message:

        * `"Name is required"`
        * `"Email is invalid"`
        * `"Password must be between 6 and 12 characters"`

So your `errors` map becomes:

```json
{
  "name": "Name is required",
  "email": "Email is invalid",
  "password": "Password must be between 6 and 12 characters"
}
```

---

### 8. Response Sent to Client

```java
return ResponseEntity.badRequest().body(errors);
```

The client gets a **400 Bad Request** with this JSON:

```json
{
  "name": "Name is required",
  "email": "Email is invalid",
  "password": "Password must be between 6 and 12 characters"
}
```

---

### ✅ Summary: The Chain of Events

| Step | What Happens                                                    |
| ---- | --------------------------------------------------------------- |
| 1    | Request arrives with invalid JSON                               |
| 2    | Spring deserializes JSON → `RegisterUserRequest`                |
| 3    | Sees `@Valid` → runs Jakarta Validation                         |
| 4    | Validator checks your annotations (`@NotBlank`, `@Email`, etc.) |
| 5    | Violations found → throws `MethodArgumentNotValidException`     |
| 6    | Spring catches it and looks for `@ExceptionHandler`             |
| 7    | Your handler extracts **your custom messages**                  |
| 8    | Returns 400 + error map to client                               |

---

## Problem

If you want to do this in another controller, you would have to **repeat the same logic** in all controllers ❌

---

## Global Error Handling

To avoid repetition, use a global handler:

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

Sometimes we need more control for the validation,
so we can create **custom annotations**.

---

## 4.2 Implementing Custom Validation

1. Create a new package named `validation`.
2. Create a new annotation — e.g., `@LowerCase`.

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LowerCaseValidator.class)
public @interface LowerCase {
    String message() default "Must be lowercase";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

Then create a validator class:

```java
public class LowerCaseValidator implements ConstraintValidator<LowerCase, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;
        return value.equals(value.toLowerCase());
    }
}
```

> `<LowerCase, String>` → first is your custom annotation, second is the field type.

---

## Validating Business Rules

To check if email is unique (business rule):

```java
@PostMapping
public ResponseEntity<?> registerUserRequest(
        @Valid @RequestBody RegisterUserRequest data
) {
    if (userRepository.existsByEmail(data.getEmail()))
        return ResponseEntity.badRequest().body(
                Map.of("email", "Email already exists"));
    var user = userMapper.toEntity(data);
    userRepository.save(user);
    return ResponseEntity.ok(userMapper.toDto(user));
}
```

> ❗We don’t use a custom annotation here because we need to **enter the method** and query the DB.
> Jakarta validation runs **before entering** the method.

---

## Remark in Exercise

Sometimes when you add an entity (e.g. `Cart`),
even with `@Table(name = "Cart")`, Hibernate looks for a lowercase table name.

To fix this, add in your `application.yaml`:

```yaml
jpa:
  show-sql: true # to show SQL in console
  hibernate:
    naming:
      physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

---

### Store UUID as String in DB

In your entity:

```java
@JdbcTypeCode(SqlTypes.CHAR)
@Column(name = "id", columnDefinition = "CHAR(36)")
private UUID id;
```

---

### Get Field Value in DTO

```java
@Mapping(target = "totalPrice", expression = "java(itemCart.getTotalPrice())")
ItemCartDto toDto(ItemCart itemCart);
```

> `java(itemCart.getTotalPrice())` → calls the method and sets result to `totalPrice`.

Example with nested DTO:

```java
@Mapping(target = "product", expression = "java(itemCart.getProduct() != null ? toDto(itemCart.getProduct()) : null)")
ItemCartDto toDto(ItemCart itemCart);
CartProductDto toDto(Product product);
```

If a DTO includes another DTO, tell MapStruct how to map it.

---

### Different Field Names

If field names differ:

```java
@Mapping(target = "productId", source = "id")
```

> `target` = returned object
> `source` = original object

---

### Calculate Total Price

In your `Cart` class:

```java
public BigDecimal getTotalPrice() {
    return itemCart.stream()
        .map(ItemCart::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
}
```

---

### Optimize Queries with `@EntityGraph`

In `CartRepository`:

```java
@EntityGraph(attributePaths = "itemCart.product")
@Query("select c from Cart c where c.id = :cartId")
Optional<Cart> getCartWithItemsAndProducts(@Param("cartId") UUID cartId);
```

> This ensures fetching all items and products in one query.

---

## Information Expert Principle

If you repeat the same logic in many methods,
move it to a **service** or **expert class** (e.g., `Cart` for `ItemCart`).

Example:

```java
public ItemCart getItemCart(Long productId) {
    return itemCart.stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst()
        .orElse(null);
}
```

---

## Anemic Domain

Classes that **only have data** —
the logic should live in services related to that entity.

---

## Rich Domain

Classes that **have data and behavior**:

* **Data:** state/properties (`id`, `items`, `status`, `total`, etc.)
* **Behavior:** business logic/methods (`addItem()`, `calculateTotal()`, etc.)

Controller → only request/response
Logic → move to service

---

## Clean Controller with Exception Handling

Before:

```java
@PostMapping("/{id}/item")
public ResponseEntity<ItemCartDto> addToCart(
        @PathVariable UUID id,
        @RequestBody AddItemToCartReqeustDto data
) {
    var cart = cartRepository.findById(id).orElse(null);
    var product = productRepository.findById(productId).orElse(null);
    if (cart == null)
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    if (product == null)
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found"));
    var itemCart = cart.addItemCart(product);
    cartRepository.save(cart);
    var cartItemCartDto = cartMapper.toDto(itemCart);
    return ResponseEntity.status(HttpStatus.CREATED).body(cartItemCartDto);
}
```

After refactoring:

```java
@PostMapping("/{id}/item")
public ResponseEntity<ItemCartDto> addToCart(
        @PathVariable UUID id,
        @RequestBody AddItemToCartReqeustDto data
) {
    var cartItemCartDto = cartService.addToCart(id, data.getProductId());
    return ResponseEntity.status(HttpStatus.CREATED).body(cartItemCartDto);
}
```

Service:

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

Exceptions:

```java
public class CartNotFoundException extends RuntimeException {}
public class ProductNotFoundException extends RuntimeException {}
```

Controller exception handling:

```java
@ExceptionHandler(CartNotFoundException.class)
public ResponseEntity<Map<String,String>> handleCartException() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "Cart not found"));
}

@ExceptionHandler(ProductNotFoundException.class)
public ResponseEntity<Map<String,String>> handleProductException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "Product not found"));
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
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
                var lineItem = createLineItem(item);
                builder.addLineItem(lineItem);
            });
           var session =  Session.create(builder.build());
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

    private  SessionCreateParams.LineItem.PriceData.ProductData createProductData(Order_items item) {
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

