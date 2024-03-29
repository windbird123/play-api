package bases

import org.apache.pekko.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext

import javax.inject.{Inject, Singleton}

@Singleton
class BlockingExecutionContext @Inject() (actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, "blocking.dispatcher")
