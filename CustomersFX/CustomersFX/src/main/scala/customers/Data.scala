package customers

import scalafx.collections.ObservableBuffer

object Data {

  val data = new ObservableBuffer[Customer]()

  addCustomer(Customer("Tom", "Praha"))

  def addCustomer(customer : Customer): Unit = {
    data +=customer
  }

}
