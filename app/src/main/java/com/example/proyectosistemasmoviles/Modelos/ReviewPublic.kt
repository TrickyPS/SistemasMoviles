package com.example.proyectosistemasmoviles.Modelos

data class ReviewPublic(
    var titulo:String? =null,
    var subtitulo:String? =null,
    var contenido:String?=null,
    var nombre:String? =null,
    var apellido:String? =null,
    var email:String?=null,
    var image:String?=null,
    var created_at:String?=null,
    var votos:Int? = null,
    var isVoted:Int? = null
)
