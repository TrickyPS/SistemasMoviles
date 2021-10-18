package com.example.proyectosistemasmoviles.Modelos

data class Comentarios(
    var comment:String?=null,
    var id_review:Int?=null,
    var id_user:Int?=null,
    var nombre:String? =null,
    var apellido:String? =null,
    var email:String?=null,
    var image:String?=null,
    var created_at:String?=null
)
