package com.example.proyectosistemasmoviles.Modelos

data class Review(
    var id: Int? = null,
    var titulo:String? =null,
    var subtitulo:String? =null,
    var contenido:String?=null,
    var id_user:Int?=null,
    var created_at:String?=null,
    var updated_at:String?=null
    )