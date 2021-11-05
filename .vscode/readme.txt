For vscode java debuging, 

1. run in terminal at root folder level :
./mvnw spring-boot:run -D"spring-boot.run.jvmArguments"="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

2. lauch debuger with lauch.json 