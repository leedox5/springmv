# Java 개발환경 SETUP 가이드

Windows 로컬과 WSL 환경에서 Java, Maven, VS Code를 설정하는 기본 절차를 정리한 문서입니다.

---

## 1. Windows 로컬 Java 확인

먼저 PowerShell 또는 CMD에서 Java 설치 여부를 확인합니다.

```powershell
java -version
javac -version
```

둘 다 버전이 출력되어야 JDK가 정상 설치된 상태입니다.

예시:

```text
java version "17..."
javac 17...
```

또는:

```text
openjdk version "21..."
javac 21...
```

`java`는 나오는데 `javac`가 나오지 않는다면 JRE만 설치되었거나 JDK 경로가 제대로 잡히지 않은 상태일 수 있습니다.

---

## 2. Windows 로컬 Maven 설치

### 2.1 Maven 다운로드

Maven 공식 사이트에서 **Binary zip archive**를 다운로드합니다.

파일명 예시:

```text
apache-maven-3.9.x-bin.zip
```

### 2.2 압축 해제 위치

개발용 폴더 아래에 압축을 푸는 것을 추천합니다.

```text
C:\dev\apache-maven-3.9.x
```

예상 구조:

```text
C:\dev\apache-maven-3.9.x
 ├─ bin
 │   ├─ mvn.cmd
 │   └─ mvnDebug.cmd
 ├─ conf
 │   └─ settings.xml
 ├─ lib
 └─ ...
```

`Program Files` 아래에 설치해도 되지만, 권한 문제를 피하려면 `C:\dev` 같은 개발 전용 폴더가 편합니다.

---

## 3. Windows Maven 환경변수 설정

### 3.1 GUI 방식

Windows 검색에서 다음 메뉴를 엽니다.

```text
시스템 환경 변수 편집
```

이후 다음 경로로 이동합니다.

```text
환경 변수 → 시스템 변수
```

새 시스템 변수를 추가합니다.

```text
변수 이름: MAVEN_HOME
변수 값: C:\dev\apache-maven-3.9.x
```

그리고 `Path`에 아래 항목을 추가합니다.

```text
%MAVEN_HOME%\bin
```

요즘 Maven 환경에서는 `M2_HOME`은 필수는 아닙니다. 일반적으로 `MAVEN_HOME`과 `PATH`만 잡아도 충분합니다.

### 3.2 PowerShell 방식

관리자 PowerShell에서 실행합니다.

```powershell
[Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\dev\apache-maven-3.9.x", "Machine")
```

```powershell
$oldPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
$newPath = $oldPath + ";%MAVEN_HOME%\bin"
[Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
```

환경변수를 변경한 뒤에는 PowerShell 또는 CMD 창을 새로 열어야 반영됩니다.

---

## 4. Maven 설치 확인

새 PowerShell 또는 CMD를 열고 실행합니다.

```powershell
mvn -version
```

정상 출력 예시:

```text
Apache Maven 3.9.x
Maven home: C:\dev\apache-maven-3.9.x
Java version: 17...
Java home: C:\Program Files\Java\jdk-17
OS name: "windows..."
```

여기서 중요한 부분은 다음 두 가지입니다.

```text
Maven home
Java home
```

`Java home`이 원하는 JDK 경로를 가리키는지 확인합니다.

---

## 5. Maven 테스트 프로젝트 생성

Maven이 정상 동작하는지 간단한 프로젝트를 생성해 봅니다.

```powershell
cd C:\dev
```

```powershell
mvn archetype:generate `
  -DgroupId=com.example `
  -DartifactId=maven-demo `
  -DarchetypeArtifactId=maven-archetype-quickstart `
  -DinteractiveMode=false
```

생성 후 테스트합니다.

```powershell
cd maven-demo
mvn test
```

정상이라면 Maven이 필요한 라이브러리를 다운로드하고 테스트를 수행합니다.

---

## 6. VS Code에서 특정 JDK 설정

VS Code에서는 다음 세 가지 JDK 관점이 다를 수 있습니다.

1. VS Code Java 확장이 사용하는 JDK
2. 프로젝트 컴파일/실행용 JDK
3. VS Code 터미널에서 사용하는 `java`, `mvn`의 JDK

---

## 7. VS Code Java Runtime 확인

VS Code에서 명령 팔레트를 엽니다.

```text
Ctrl + Shift + P
```

다음 명령을 실행합니다.

```text
Java: Configure Java Runtime
```

여기서 VS Code Java 확장이 인식한 JDK 목록과 프로젝트별 런타임을 확인할 수 있습니다.

---

## 8. VS Code settings.json에 JDK 직접 지정

사용자 전체 설정을 열려면:

```text
Ctrl + Shift + P
Preferences: Open User Settings (JSON)
```

프로젝트별로만 적용하려면 프로젝트 루트에 다음 파일을 만듭니다.

```text
.vscode/settings.json
```

### 8.1 JDK 17만 등록하는 예시

```json
{
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-17",
      "path": "C:\\Program Files\\Java\\jdk-17"
    }
  ]
}
```

### 8.2 JDK 8, 17, 21을 같이 등록하는 예시

```json
{
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-1.8",
      "path": "C:\\Program Files\\Java\\jdk1.8.0_202"
    },
    {
      "name": "JavaSE-17",
      "path": "C:\\Program Files\\Java\\jdk-17"
    },
    {
      "name": "JavaSE-21",
      "path": "C:\\Program Files\\Java\\jdk-21",
      "default": true
    }
  ]
}
```

`"default": true`가 붙은 JDK가 기본 런타임으로 사용됩니다.

---

## 9. VS Code Java 확장 실행용 JDK 지정

프로젝트 컴파일 JDK와 VS Code Java 확장 자체를 실행하는 JDK는 다를 수 있습니다.

예를 들어 Java 확장은 JDK 21로 실행하고, 프로젝트는 JDK 17로 컴파일할 수 있습니다.

```json
{
  "java.jdt.ls.java.home": "C:\\Program Files\\Java\\jdk-21",

  "java.configuration.runtimes": [
    {
      "name": "JavaSE-17",
      "path": "C:\\Program Files\\Java\\jdk-17",
      "default": true
    },
    {
      "name": "JavaSE-21",
      "path": "C:\\Program Files\\Java\\jdk-21"
    }
  ]
}
```

정리하면 다음과 같습니다.

```text
java.jdt.ls.java.home
→ VS Code Java 확장 실행용 JDK

java.configuration.runtimes
→ Java 프로젝트에서 선택 가능한 JDK 목록
```

---

## 10. Maven 프로젝트의 pom.xml JDK 설정

VS Code에서 JDK를 설정해도 Maven 컴파일 버전은 `pom.xml` 설정의 영향을 받습니다.

### 10.1 Java 17 프로젝트

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
```

또는:

```xml
<properties>
    <maven.compiler.release>17</maven.compiler.release>
</properties>
```

### 10.2 Java 8 프로젝트

```xml
<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>
```

또는:

```xml
<properties>
    <maven.compiler.release>8</maven.compiler.release>
</properties>
```

새 프로젝트라면 `maven.compiler.release` 방식을 추천합니다.

---

## 11. Windows 환경변수에서 기본 JDK 고정

Windows 전체에서 특정 JDK를 기본으로 쓰려면 `JAVA_HOME`을 설정합니다.

예: JDK 17

```text
JAVA_HOME = C:\Program Files\Java\jdk-17
```

그리고 `Path`에 아래 항목을 추가합니다.

```text
%JAVA_HOME%\bin
```

주의할 점은 `Path` 안에 예전 JDK 경로가 위쪽에 있으면 그쪽이 먼저 잡힐 수 있다는 것입니다.

예:

```text
C:\Program Files\Java\jdk1.8.0_202\bin
%JAVA_HOME%\bin
```

이 경우 JDK 8이 먼저 잡힐 수 있습니다. 가능하면 직접 JDK 경로는 제거하고 `%JAVA_HOME%\bin`만 두는 것이 깔끔합니다.

확인 명령:

```powershell
java -version
javac -version
mvn -version
```

---

## 12. WSL 환경에서 Java 설치

WSL에서는 Windows 쪽 JDK와 별개로 Ubuntu 내부에 Java를 따로 설치한다고 보면 됩니다.

먼저 WSL Ubuntu 버전을 확인합니다.

```bash
lsb_release -a
```

또는:

```bash
cat /etc/os-release
```

---

## 13. WSL 패키지 목록 업데이트

```bash
sudo apt update
```

---

## 14. WSL JDK 설치

### 14.1 Java 21 설치

새 프로젝트나 최신 Spring Boot 기준이면 Java 21이 좋습니다.

```bash
sudo apt install openjdk-21-jdk -y
```

### 14.2 Java 17 설치

실무 호환성, 레거시 프로젝트, Spring Boot 2.x/3.x 혼재 환경이면 Java 17이 무난합니다.

```bash
sudo apt install openjdk-17-jdk -y
```

---

## 15. WSL Java 설치 확인

```bash
java -version
javac -version
```

예시:

```text
openjdk version "21..."
javac 21...
```

또는:

```text
openjdk version "17..."
javac 17...
```

`java`만 나오고 `javac`가 나오지 않으면 JDK가 아니라 JRE만 설치된 상태일 수 있습니다.

---

## 16. WSL JAVA_HOME 확인

설치된 Java 경로는 보통 아래에 있습니다.

```bash
ls /usr/lib/jvm
```

실제 `java` 경로를 확인합니다.

```bash
readlink -f $(which java)
```

예시:

```text
/usr/lib/jvm/java-21-openjdk-amd64/bin/java
```

이 경우 `JAVA_HOME`은 다음 경로입니다.

```text
/usr/lib/jvm/java-21-openjdk-amd64
```

---

## 17. WSL JAVA_HOME 설정

`~/.bashrc`를 엽니다.

```bash
nano ~/.bashrc
```

맨 아래에 추가합니다.

### Java 21인 경우

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

### Java 17인 경우

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

저장 후 적용합니다.

```bash
source ~/.bashrc
```

확인합니다.

```bash
echo $JAVA_HOME
java -version
javac -version
```

---

## 18. WSL에서 여러 JDK 선택

JDK 17과 21을 둘 다 설치했다면 아래 명령으로 기본 Java를 선택할 수 있습니다.

```bash
sudo update-alternatives --config java
```

`javac`도 맞춰주는 것이 좋습니다.

```bash
sudo update-alternatives --config javac
```

선택 후 다시 확인합니다.

```bash
java -version
javac -version
```

---

## 19. WSL Maven 설치

WSL에서 Java 개발을 할 경우 Maven도 Ubuntu 내부에 설치합니다.

```bash
sudo apt install maven -y
```

확인:

```bash
mvn -version
```

중요 확인 항목:

```text
Java version
Java home
```

예시:

```text
Apache Maven ...
Java version: 21...
Java home: /usr/lib/jvm/java-21-openjdk-amd64
```

Maven이 원하는 JDK를 사용하고 있는지 반드시 확인합니다.

---

## 20. VS Code에서 WSL Java 사용

Windows VS Code에서 WSL 프로젝트를 열 때는 **Remote - WSL** 방식으로 여는 것이 좋습니다.

WSL 터미널에서 프로젝트 폴더로 이동한 뒤 실행합니다.

```bash
code .
```

이렇게 열면 VS Code가 Windows JDK가 아니라 WSL 내부의 Java, JDK, Maven을 기준으로 동작합니다.

VS Code 터미널에서 확인합니다.

```bash
which java
java -version
which mvn
mvn -version
```

정상이라면 경로가 Windows의 `C:\Program Files\Java...`가 아니라 Linux 경로로 나옵니다.

예시:

```text
/usr/bin/java
/usr/bin/mvn
```

---

## 21. 추천 설치 조합

### 21.1 Windows 로컬 개발

```text
JDK 17 또는 JDK 21
Maven 3.9.x
VS Code Java Extension Pack
```

### 21.2 WSL 새 실습용

```bash
sudo apt update
sudo apt install openjdk-21-jdk maven -y
```

확인:

```bash
java -version
javac -version
mvn -version
```

### 21.3 WSL 실무 호환성 우선

```bash
sudo apt update
sudo apt install openjdk-17-jdk maven -y
```

확인:

```bash
java -version
javac -version
mvn -version
```

개인 실습이나 최신 Spring Boot 학습은 JDK 21, 회사 레거시나 호환성 테스트는 JDK 17을 추천합니다.

---

## 22. 최종 점검 명령 모음

### Windows

```powershell
java -version
javac -version
mvn -version
```

### WSL

```bash
which java
java -version
javac -version
echo $JAVA_HOME
which mvn
mvn -version
```

### Maven 테스트

```bash
mvn -version
```

```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=maven-demo \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

```bash
cd maven-demo
mvn test
```

---

## 23. 자주 보는 문제

### 문제 1. `java`는 되는데 `javac`가 안 됨

JRE만 설치했거나 JDK의 `bin` 경로가 PATH에 잡히지 않은 상태입니다.

해결:

```text
JDK 설치 확인
JAVA_HOME 확인
PATH에 %JAVA_HOME%\bin 또는 $JAVA_HOME/bin 추가
```

### 문제 2. Maven이 다른 JDK를 사용함

`mvn -version`에서 `Java home`을 확인합니다.

Windows에서는 `JAVA_HOME`과 `Path` 순서를 확인합니다.

WSL에서는 다음을 확인합니다.

```bash
echo $JAVA_HOME
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

### 문제 3. VS Code에서는 다른 JDK로 보임

VS Code 설정을 확인합니다.

```text
Java: Configure Java Runtime
```

또는 `.vscode/settings.json`의 다음 설정을 확인합니다.

```json
{
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-17",
      "path": "C:\\Program Files\\Java\\jdk-17",
      "default": true
    }
  ]
}
```

### 문제 4. WSL에서 Windows Java가 잡히는 것처럼 보임

WSL 프로젝트는 WSL 터미널에서 다음 명령으로 VS Code를 여는 것이 좋습니다.

```bash
code .
```

그리고 VS Code 왼쪽 아래에 `WSL: Ubuntu` 형태로 표시되는지 확인합니다.

---

## 24. 기본 결론

처음 Java/Maven 개발환경을 잡는다면 다음 흐름이 가장 안정적입니다.

```text
1. JDK 설치
2. java -version, javac -version 확인
3. Maven 설치
4. mvn -version 확인
5. VS Code에서 Java Runtime 확인
6. Maven 테스트 프로젝트 생성
7. mvn test 실행
```

Windows와 WSL은 서로 독립된 개발환경으로 보고 각각 JDK와 Maven을 확인하는 것이 좋습니다.

---

## 25. WSL에 MySQL 설치 후 springmv 프로젝트 연결

`springmv` 프로젝트는 `pom.xml`에서 `mysql-connector-java 8.0.27`을 사용하므로, MySQL 서버도 8.0.x 계열을 설치하는 것을 권장합니다.

### 25.1 MySQL 서버 설치

```bash
sudo apt update
sudo apt install mysql-server -y
```

### 25.2 MySQL 서비스 시작 및 상태 확인

WSL은 기본적으로 systemd가 아닌 경우가 많으므로 `service` 명령을 사용합니다.

```bash
sudo service mysql start
sudo service mysql status
```

버전 확인:

```bash
mysql --version
```

### 25.3 root로 접속

Ubuntu의 MySQL은 기본적으로 `auth_socket` 인증을 사용하므로 `sudo` 없이 접속하면 비밀번호 없이도 막힐 수 있습니다.

```bash
sudo mysql
```

### 25.4 프로젝트용 데이터베이스와 사용자 생성

`src/main/resources/application.properties`는 gitignore 대상이라 저장소에는 없습니다. 로컬에서 사용할 DB 이름, 사용자명, 비밀번호를 정하고 아래 값을 자신의 값으로 바꿔서 실행합니다.

```sql
CREATE DATABASE <DB_NAME> CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER '<DB_USER>'@'localhost' IDENTIFIED BY '<DB_PASSWORD>';
GRANT ALL PRIVILEGES ON <DB_NAME>.* TO '<DB_USER>'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 25.5 접속 테스트

```bash
mysql -u <DB_USER> -p
```

실행하면 다음처럼 비밀번호 입력 프롬프트가 뜹니다. 여기에 25.4에서 설정한 `<DB_PASSWORD>`를 입력합니다.

```text
Enter password:
```

정상적으로 접속되어 `mysql>` 프롬프트가 뜨면 준비가 된 것입니다.

### 25.6 application.properties에 datasource 설정

`src/main/resources/application.properties`가 없다면 새로 만들고, 위에서 만든 DB 이름/사용자/비밀번호를 아래 형태로 반영합니다.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/<DB_NAME>?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username=<DB_USER>
spring.datasource.password=<DB_PASSWORD>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

`ddl-auto=update`로 두면 Spring Data JPA가 엔티티 기준으로 테이블을 자동 생성/갱신하므로, 스키마를 직접 만들 필요는 없습니다. (Spring Security remember-me용 토큰 테이블도 이 과정에서 함께 생성됩니다.)

### 25.7 애플리케이션 실행 및 연결 확인

```bash
mvn spring-boot:run
```

콘솔에 `HikariPool` 관련 로그가 에러 없이 뜨고, 브라우저에서 아래 주소가 열리면 DB 연결이 정상입니다.

```text
http://localhost:8080/
```

### 25.8 자주 보는 문제

**`Access denied for user`**
사용자명/비밀번호가 `application.properties`와 실제 MySQL 계정이 일치하는지 확인합니다.

**`Unknown database`**
25.4에서 만든 `<DB_NAME>`과 `application.properties`의 URL에 있는 DB 이름이 같은지 확인합니다.

**`Public Key Retrieval is not allowed`**
JDBC URL에 `allowPublicKeyRetrieval=true`가 포함되어 있는지 확인합니다.

**MySQL 서비스가 재부팅 후 꺼져 있음**
WSL은 Windows 재부팅 시 자동으로 서비스가 시작되지 않으므로, 매번 아래 명령으로 수동 시작이 필요합니다.

```bash
sudo service mysql start
```
