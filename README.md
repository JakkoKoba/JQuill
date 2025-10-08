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
## Installation

### A) GitHub Packages (Recommended)

1. Add the repository to the pom.xml file:
```xml
<repositories>
  <repository>
    <id>github</id>
    <name>GitHub Packages</name>
    <url>https://maven.pkg.github.com/JakkoKoba/jquill</url>
  </repository>
</repositories>
```

2. Add the dependency:
```xml
<dependency>
  <groupId>io.github.jakkokoba</groupId>
  <artifactId>jquill</artifactId>
  <version>1.0.0</version>
</dependency>
```