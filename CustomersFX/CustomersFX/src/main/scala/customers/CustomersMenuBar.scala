package customers

import customers.actions.{CreateAction, DeleteAction, ExitAction}
import scalafx.scene.control.{Menu, MenuBar, MenuItem}

object CustomersMenuBar extends MenuBar {
  menus = Seq(
    new Menu("File") {
      items.addAll(
        ExitAction.menuItem
      )
    },
    new Menu("Customers") {
      items.addAll(
        CreateAction.menuItem,
        DeleteAction.menuItem
      )
    }
  )

}
