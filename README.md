# JQuill - Java Console Utility Library

**JQuill** is a lightweight Java library providing handy utilities for console interaction and development.
It was originally designed as a companion to **JCMD**, offering a simple way to customize output, manage messages, and enhance readability in terminal-based applications.

---

## Features

* Easy-to-use methods for printing styled or prefixed messages
* Helpful utilities for console interaction and debugging
* Works seamlessly with JCMD and other Java projects
* Lightweight design with zero dependencies

---

## Requirements (tested)

* Java 24.0.2
* Maven 3.9.11

---

## Example Usage

```java
import org.jquill.Debug;
import org.jquill.JQuill;

public class Main {
    public static void main(String[] args) {
        Debug.info("Starting application...");
        Debug.debug("Loading modules...");
        Debug.warn("Deprecated configuration detected.");
        Debug.success("Modules loaded successfully!");
        Debug.error("Failed to load optional plugin!");
    }
}
```

## Handy Methods

| Method                               | Description                         |
| ------------------------------------ | ----------------------------------- |
| `info(String msg)`                   | Prints an informational message     |
| `debug(String msg)`                  | Prints a debug message              |
| `error(String msg)`                  | Prints an error message             |
| `warn(String msg)`                   | Prints a warning message            |
| `success(String msg)`                | Prints a success message            |

*(Method availability may vary by version.)*

---

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/JakkoKoba/jquill.git
```

2. Navigate to the project directory:

```bash
cd jquill
```

3. Build the library with Maven:

```bash
mvn clean package
```

4. Install locally (optional):

```bash
mvn install
```

5. Add dependency to your project:

```xml
<dependency>
  <groupId>org.jquill</groupId>
  <artifactId>jquill</artifactId>
  <version>0.1.0</version>
</dependency>
```