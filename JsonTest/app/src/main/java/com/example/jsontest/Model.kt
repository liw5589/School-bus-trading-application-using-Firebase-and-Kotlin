package com.example.jsontest

public class Model{
    lateinit var type:String
    lateinit var sellDate:String
    lateinit var num:String
    lateinit var price:String
    lateinit var talkID:String
    lateinit var writeDate:String

    constructor(sellDate: String,type:String,price:String,talkID:String,writeDate:String,num:String) {
        this.sellDate = sellDate
        this.num = num
        this.price = price
        this.talkID= talkID
        this.writeDate = writeDate
        this.type = type
    }

    constructor()
}