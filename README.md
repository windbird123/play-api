# play-api
master, rest 두개의 branch 가 있다.

### master branch  
* 기능: Grpc + Rest
* 실행  
  * 참고: https://developer.lightbend.com/guides/play-scala-grpc-example/locally.html
  * dev mode (IDE 에서)
    ```bash
    sbt run
    ```
    dev mode 에서는 application.conf 에 지정된 http.port 값을 인식을 못하는 듯 해서 build.sbt 의 PlayKeys.devSettings 에 설정
    https://stackoverflow.com/a/40119695
    
  * prod mode (linux 장비에서)
    ```bash
    sbt runProd
    ```
* 확인
  * http api
    ```bash
    curl -XGET http://localhost:9000/; echo
    ```
  * https api
    ```bash
    curl --insecure https://localhost:9443; echo 
    ```
  * non-secure grpc  
    > localhost:9000 로 greeterServiceClient.sayHello(request) 요청
  * secure grpc  
    > localhost:9443 로 SSL/TLS 설정 & greeterServiceClient.sayHello(request) 요청
* 정리  

  | 구분 |  port | 인증 필요 |
  |------|-------|----------|
  | http | 9000 | X | 
  | grpc | 9000 | X | 
  | https| 9433 | O |
  | grpc | 9443 | O |
  
  * http://localhost:9000/ 의 경우 내부적으로 conf/selfsigned.keystore 를 이용해 grpc 접근해 결과를 받아 전달하는 구조

 
### rest branch  
* 기능: Rest only 
* 실행
  * IDE 에서 쉽게 실행 됨