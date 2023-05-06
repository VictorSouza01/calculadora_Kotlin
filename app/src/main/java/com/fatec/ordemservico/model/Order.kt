package com.fatec.ordemservico.model

class Order(id : String, user_email : String, title: String, description: String, status:String, price : String) {
    var id : String
    var user_email : String
    var title : String
    var description : String
    var status : String
    var price : String
    init {
        this.id = id
        this.user_email = user_email
        this.title = title
        this.description = description
        this.status = status
        this.price = price
    }
}