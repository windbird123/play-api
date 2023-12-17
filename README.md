## 소개

아래 2개의 repository 를 참고해 playframework rest api 를 작성함

* https://github.com/gaeljw/tapir-play-sample
* https://github.com/playframework/play-samples/tree/3.0.x/play-scala-rest-api-example

## 실행

* Require jdk-17
* run server
  ```bash
  sbt run
  ```

* OpenAPI docs
  ```bash  
  http://localhost:9000/docs/
  ```

## 특징

* OpenAPI docs: http://localhost:9000/docs
* [tapir](https://tapir.softwaremill.com/en/latest/index.html) 사용
* [Marker context logging](https://www.playframework.com/documentation/3.0.x/ScalaLogging#Using-Markers-and-Marker-Contexts)
* [adds blocking threads pool](https://www.playframework.com/documentation/3.0.x/ThreadPools)
* [cache](https://www.playframework.com/documentation/3.0.x/ScalaCache)

## 참고

* streaming 이 필요할 경우 [여기](https://github.com/gaeljw/tapir-play-sample/blob/master/app/routers/BookController.scala#L21)
  처럼 pekko-stream 을 이용할 수 있다.
