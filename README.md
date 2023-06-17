## 소개
아래 2개의 repository 를 참고해 playframework rest api 를 작성함

* https://github.com/gaeljw/tapir-play-sample
* https://github.com/playframework/play-samples/tree/2.8.x/play-scala-rest-api-example

## 특징
* [tapir](https://tapir.softwaremill.com/en/latest/index.html) 사용
* Marker context id logging (MDC)
* adds blocking threads pool

## 참고
* streaming 이 필요할 경우 [여기](https://github.com/gaeljw/tapir-play-sample/blob/master/app/routers/BookController.scala#L21) 처럼 akka-stream 을 이용할 수 있다.
