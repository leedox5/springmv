# springmv

`springmv` is a Spring Boot web application that combines several flows in one codebase:

- a wordbook area for creating and browsing vocabulary entries
- a book area for word-focused study views and member-specific lists
- a club area for meetings, players, matches, and simple stats
- login, signup, and remember-me authentication backed by Spring Security

The project is implemented as a server-rendered MVC app with JPA persistence, Thymeleaf views, some legacy JSP assets, and a small amount of static JavaScript.

## Stack

| Area | Details |
| --- | --- |
| Runtime | Java 17, Spring Boot 2.6 |
| Web | Spring MVC, Thymeleaf, JSP (`tomcat-jasper`, `jstl`) |
| Data | Spring Data JPA, MySQL connector |
| Security | Spring Security with JDBC remember-me tokens |
| Build | Maven |
| Local dependency | `lib\leedox-1.0-SNAPSHOT.jar` |

## Main application areas

| Area | Entry points | Notes |
| --- | --- | --- |
| Landing and account flow | `/`, `/intro`, `/login`, `/signup` | Shared entry pages and basic member signup |
| Wordbook | `/wordbook/**` | Personal wordbook views, lists, detail pages, and add flow |
| Book | `/book/**` | Member-facing study and browsing flows |
| Club | `/club/**` | Meeting, player, ranking, and match management |
| Payments | `/club/orders/**` | PayPal-backed order creation and capture |

## Repository layout

| Path | Purpose |
| --- | --- |
| `src\main\java\kr\leedox` | Application code, controllers, services, entities, repositories |
| `src\main\resources\templates\thymeleaf` | Server-rendered Thymeleaf templates |
| `src\main\webapp\pages` | Legacy JSP pages |
| `src\main\resources\static` | Static HTML, JS, CSS, and images |
| `src\test\java` | JUnit and Spring Boot tests |
| `lib` | Checked-in local JAR dependency |

## Local development

Start with the setup guide:

- [Local setup guide](docs/local-setup.md)

Typical commands after local configuration is in place:

```powershell
mvn test
mvn spring-boot:run
```

## Important repository notes

- `src/main/resources/application.properties` is gitignored and is not checked in, so each developer needs a local copy.
- The Maven wrapper scripts are present, but the `.mvn` wrapper directory is not checked in. Use a locally installed `mvn` unless wrapper files are restored.
- PayPal settings are bound from `paypal.*` properties and must be supplied locally if you want payment flows to work.

## Testing

The repository includes tests under `src\test\java`, including Spring Boot context, controller, service, repository, and entity coverage. Run them with:

```powershell
mvn test
```
