package com.example.jsontest

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.DialogTitle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kakao.kakaolink.KakaoLink
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adapter_layout.*
import kotlinx.android.synthetic.main.custom_dialog.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(){
    private val RECORD_REQUEST_CODE = 1000
    private lateinit var mRunnable:Runnable
    lateinit var progress:ProgressBar
    lateinit var listView_details: ListView
    var arrayList_details:ArrayList<Model> = ArrayList();
    //OkHttpClient creates connection pool between client and server
    val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        Toast.makeText(this,"KIT_BUS", Toast.LENGTH_SHORT).show()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        listView_details = findViewById(R.id.listView) as ListView
        run("https://myapplication-bf0b8.firebaseio.com/board.json")
        listView_details.onItemClickListener = AdapterView.OnItemClickListener{parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position) as Model

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)

            val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogType)
            dialogTitle.text = selectItem.type

            val textPRICE = dialogView.findViewById<TextView>(R.id.tvPRICE)
            textPRICE.text = selectItem.price

            val tv15 = dialogView.findViewById<TextView>(R.id.textView15)

            val tv16 = dialogView.findViewById<TextView>(R.id.textView16)

            val tv19 = dialogView.findViewById<TextView>(R.id.textView19)

            val tv20 = dialogView.findViewById<TextView>(R.id.textView20)

            val tvSELLDATE = dialogView.findViewById<TextView>(R.id.tvSELLDATE)
            tvSELLDATE.text = selectItem.sellDate

            val tvTALKID = dialogView.findViewById<TextView>(R.id.tvTALKID)
            tvTALKID.text = selectItem.talkID

            val tvWRITEDATE = dialogView.findViewById<TextView>(R.id.tvWRITEDATE)
            tvWRITEDATE.text = selectItem.writeDate

            if(selectItem.price != "판매완료"){
                dialogTitle.setTextColor(Color.parseColor("#5E35B1"))
                builder.setNegativeButton("판매자에게 SMS 전송하기", DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
                    val smsManager = SmsManager.getDefault() as SmsManager
                    val st_phone_num = tvTALKID.text.toString()

                    val str_sms = "[KIT-BUS] \n" +
                            "판매날짜 : " + tvSELLDATE.text.toString() + "\n종류 : "+ tvType.text.toString() +  " \n가격 : " + textPRICE.text.toString()+ "\n구매 하고 싶어요! 연락 주세요!"
                    val smsPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)
                    if(smsPermission == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this,"PERMISSION_DENIED", Toast.LENGTH_SHORT).show()
                        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.SEND_SMS),RECORD_REQUEST_CODE)
                        smsManager.sendTextMessage(st_phone_num,null,str_sms,null,null)
                        Toast.makeText(this,"SMS 전송 완료", Toast.LENGTH_SHORT).show()
                    }
                    else if(smsPermission == PackageManager.PERMISSION_GRANTED){

                        smsManager.sendTextMessage(st_phone_num,null,str_sms,null,null)
                        Toast.makeText(this,"SMS 전송 완료", Toast.LENGTH_SHORT).show()
                        var item_num = selectItem.num
                        var database : FirebaseDatabase = FirebaseDatabase.getInstance()
                        var path = "board/board/" + item_num
                        val myRef : DatabaseReference = database.getReference(path)
                        myRef.child("price").setValue("판매완료")
                        myRef.child("sellDate").setValue("판매완료")
                        myRef.child("talkID").setValue("판매완료")
                        myRef.child("writeDate").setValue("판매완료")
                        myRef.child("type").setValue("판매\n완료")
                        myRef.child("num").setValue(item_num)

                    }



                })

                builder.setPositiveButton("닫기", DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->

                })
                builder.setView(dialogView).show()

            }

            }





        imageButton.setOnClickListener{
            val nextIntent = Intent(this,WritePage::class.java)
            startActivity(nextIntent)
        }
        swipe.setOnRefreshListener {
            run("https://myapplication-bf0b8.firebaseio.com/board.json")
            swipe.isRefreshing = false
        }

    }


    fun run(url: String) {


        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

                var str_response = response.body()!!.string()
                //creating json object
                val json_contact:JSONObject = JSONObject(str_response)
                //creating json array
                var jsonarray_info:JSONArray= json_contact.getJSONArray("board")
                var i:Int = 0
                var size:Int = jsonarray_info.length()
                arrayList_details= ArrayList();
                for (i in size-1 downTo 1 ) {
                    var json_objectdetail:JSONObject=jsonarray_info.getJSONObject(i)
                    var model:Model= Model();
                    model.num = json_objectdetail.getString("num")
                    model.sellDate=json_objectdetail.getString("sellDate")
                    model.type=json_objectdetail.getString("type")
                    model.price=json_objectdetail.getString("price")
                    model.talkID=json_objectdetail.getString("talkID")
                    model.writeDate=json_objectdetail.getString("writeDate")
                    arrayList_details.add(model)
                }
                runOnUiThread {
                    //stuff that updates ui
                    val obj_adapter : CustomAdapter
                    obj_adapter = CustomAdapter(applicationContext,arrayList_details)
                    listView_details.adapter=obj_adapter
                }


            }
        })

    }
}