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

public class Main {
    public static void main(String[] args) {
        Debug.info("Starting application...");
        Debug.log("Loading modules...");
        Debug.warn("Deprecated configuration detected.");
        Debug.success("Modules loaded successfully!");
        Debug.error("Failed to load optional plugin!");
    }
}
```

## Handy Methods

| Method                                 | Description                                                                             |
|----------------------------------------|-----------------------------------------------------------------------------------------|
| `info(String msg)`                     | Prints an informational message. Honors `showType` and `showTime` flags.                |
| `log(String msg)`                      | Prints a debug/log message. Honors `showType` and `showTime` flags.                     |
| `warn(String msg)`                     | Prints a warning message. Honors `showType` and `showTime` flags.                       |
| `error(String msg)`                    | Prints an error message. Honors `showType` and `showTime` flags.                        |
| `success(String msg)`                  | Prints a success message. Honors `showType` and `showTime` flags.                       |
| `print(String msg, Style... styles)`   | Prints a message without newline. Supports custom text styling using `Style` instances. |
| `println(String msg, Style... styles)` | Prints a message with newline. Supports custom text styling using `Style` instances.        |

*(Method availability and formatting behavior may vary by version. Prefixes and timestamps depend on `showType`, `showTime`, and `useRunTime` settings.)*

## Available Styles

| Style Instance |
|----------------|
| `RESET`        |
| `BOLD`         |
| `DIM`          |
| `ITALICS`      |
| `UNDERLINE`    |
| `GRAY`         |
| `CYAN`         |
| `AMBER`        |
| `RED`          |
| `GREEN`        |


---
## Installation

### A) GitHub Packages (Recommended)

#### 1. Set up Maven authentication
1. Create a GitHub personal access token with `read:packages` scope.
2. Add it to your Maven `settings.xml` (usually in `~/.m2/settings.xml`):
```xml
<servers>
  <server>
    <id>github</id>
    <username>YOUR_GITHUB_USERNAME</username>
    <password>YOUR_PERSONAL_ACCESS_TOKEN</password>
  </server>
</servers>
```

> ⚠️ Make sure <id> matches the repository <id> below (github).

2. Add the repository to the pom.xml file:
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