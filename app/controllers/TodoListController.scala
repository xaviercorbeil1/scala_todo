package controllers

import com.google.inject.Inject
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import play.api.libs.json._

import javax.inject.Singleton
import java.sql.{Connection, DriverManager, Statement}
import scala.collection.mutable

@Singleton
class TodoListController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController {

  val connection: Connection = DriverManager.getConnection("jdbc:sqlite::../../database/todo.db")

  case class todo(id: Int, text: String, done: Boolean)

  implicit val todoListJson = Json.format[todo]

  def home: Action[AnyContent] = Action {
    NoContent
  }

  def getAll: Action[AnyContent] = Action {
    val stmt: Statement = connection.createStatement()
    val sql = "SELECT * FROM Todos;"
    val todoList = new mutable.ListBuffer[todo]()
    val set = stmt.executeQuery(sql)

    while (set.next()) {
      val id = set.getInt("id")
      val text = set.getString("text")
      val done = set.getBoolean("done")
      todoList += todo(id, text, done)
    }
    set.close()
    Ok(Json.toJson(todoList))
  }

  def save: Action[AnyContent] = Action { request: Request[AnyContent] =>
    val content = request.body
    val jsonValue = content.asJson.get
    val str = (jsonValue \ "text").as[String]

    val stmt: Statement = connection.createStatement()
    val sql = f"INSERT INTO Todos (text) VALUES ('$str');"

    stmt.executeUpdate(sql)

    Created
  }

  def done(id: Int): Action[AnyContent] = Action {
    val stmt: Statement = connection.createStatement()
    val sql = f"UPDATE todos SET done = 1 WHERE id = $id"
    stmt.executeUpdate(sql)
    Ok
  }
}
