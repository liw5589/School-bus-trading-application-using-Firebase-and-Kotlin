package com.example.jsontest

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.pagewrite.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class WritePage : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val current = LocalDateTime.now()
        val formatter_spinner = DateTimeFormatter.ofPattern("yyMMdd")
        val formatted_spinner= current.format(formatter_spinner)
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일 HH:mm:ss")
        val formatter_title : DateTimeFormatter = DateTimeFormatter.ofPattern("오늘은 yy년 MM월 dd일")
        val formatter_title2 = DateTimeFormatter.ofPattern("현재 시각 HH시 mm분 ss초 입니다.")
        val formatted_title2= current.format(formatter_title2)
        val formatted_title = current.format(formatter_title)
        val formatted = current.format(formatter)

        var database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("board/board")
        val numRef : DatabaseReference = database.getReference("NUM")
        var num = 1;
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pagewrite)
        textView12.text = formatted_title
        textView22.text = formatted_title2
        if(formatted_spinner.substring(0,2) == "19"){
            spinner.setSelection(0)
        }
        else{
            spinner.setSelection(1)
        }
            spinner2.setSelection(formatted_spinner.substring(2,4).toInt() - 1)
            spinner3.setSelection(formatted_spinner.substring(4,6).toInt() - 1)


        numRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                //값이 변경된게 있으면 database의 값이 갱신되면 자동 호출된다.
                val value = p0?.value
                num = value.toString().toInt()+1

            }
        })
        button.setOnClickListener{

            var str_sellDateNtype = spinner.selectedItem.toString() + "년 " +
                    spinner2.selectedItem.toString() + "월 " + spinner3.selectedItem.toString() +"일"
            var str_price = edit_content2.text.toString()+"원"
            var str_talkID = edit_content.text.toString()
            var str_currentTime : String = formatted
            var str_type = spinner4.selectedItem.toString()

            myRef.child(num.toString()).child("price").setValue(str_price)
            myRef.child(num.toString()).child("sellDate").setValue(str_sellDateNtype)
            myRef.child(num.toString()).child("talkID").setValue(str_talkID)
            myRef.child(num.toString()).child("writeDate").setValue(str_currentTime)
            myRef.child(num.toString()).child("type").setValue(str_type)
            myRef.child(num.toString()).child("num").setValue(num.toString())

            numRef.setValue(num.toString())
            finish()
        }
        button2.setOnClickListener{

            finish()
        }
    }
}