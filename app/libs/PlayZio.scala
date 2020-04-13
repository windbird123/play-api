package libs

import play.api.mvc.{Action, ActionBuilder, BodyParser, Result}

object PlayZio {
  implicit class ActionBuilderOps[+R[_], B](actionBuilder: ActionBuilder[R, B]) {
    def z(zioAction: R[B] => zio.Task[Result]): Action[B] = actionBuilder.async { request =>
      val task = zioAction(request)
      zio.Runtime.default.unsafeRunToFuture(task)
    }

    def z[A](bp: BodyParser[A])(zioAction: R[A] => zio.Task[Result]): Action[A] = actionBuilder(bp).async { request =>
      val task = zioAction(request)
      zio.Runtime.default.unsafeRunToFuture(task)
    }
  }
}
