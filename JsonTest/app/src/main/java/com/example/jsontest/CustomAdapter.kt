package com.example.jsontest

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import org.w3c.dom.Text

class CustomAdapter(context: Context,arrayListDetails:ArrayList<Model>) : BaseAdapter(){

    private val layoutInflater: LayoutInflater
    private val arrayListDetails:ArrayList<Model>

    init {
        this.layoutInflater = LayoutInflater.from(context)
        this.arrayListDetails=arrayListDetails
    }

    override fun getCount(): Int {
        return arrayListDetails.size
    }

    override fun getItem(position: Int): Any {
        return arrayListDetails.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val listRowHolder: ListRowHolder
        if (convertView == null) {
            view = this.layoutInflater.inflate(R.layout.adapter_layout, parent, false)
            listRowHolder = ListRowHolder(view)
            view.tag = listRowHolder

        } else {
            view = convertView
            listRowHolder = view.tag as ListRowHolder
        }

        listRowHolder.tvWriteDate.text = arrayListDetails.get(position).writeDate
        listRowHolder.tvTalkID.text = arrayListDetails.get(position).talkID
        listRowHolder.tvPrice.text = arrayListDetails.get(position).price
        listRowHolder.tvSellDate.text = arrayListDetails.get(position).sellDate
        listRowHolder.tvType.text = arrayListDetails.get(position).type

        var color_sell = "#BF360C"
        var color_unsell = "#5E35B1"
        if(listRowHolder.tvWriteDate.text == "판매완료"){
            listRowHolder.tvType.setTextColor(Color.RED)
            listRowHolder.firstV.visibility = View.GONE
            listRowHolder.secondV.visibility = View.GONE
            listRowHolder.thirdV.visibility = View.GONE
            listRowHolder.fourthV.visibility = View.GONE
            listRowHolder.now_Sell.visibility = View.VISIBLE
            listRowHolder.now_Sell.text = "판매가 완료된 통학권 입니다."

        }
        else{

            listRowHolder.tvWriteDate.setTextColor(Color.parseColor(color_unsell))
            listRowHolder.tvSellDate.setTextColor(Color.parseColor(color_unsell))
            listRowHolder.tvPrice.setTextColor(Color.parseColor(color_unsell))
            listRowHolder.tvTalkID.setTextColor(Color.parseColor(color_unsell))
            listRowHolder.tvType.setTextColor(Color.parseColor(color_unsell))

        }



        return view
    }
}

private class ListRowHolder(row: View?) {
    public val tvType: TextView
    public val tvSellDate: TextView
    public val tvPrice: TextView
    public val tvTalkID: TextView
    public val tvWriteDate: TextView
    public val linearLayout: LinearLayout
    public val firstV: LinearLayout
    public val secondV : LinearLayout
    public val thirdV : LinearLayout
    public val fourthV : LinearLayout
    public val now_Sell : TextView


    init {
        this.tvType = row?.findViewById<TextView>(R.id.tvType) as TextView
        this.tvSellDate = row?.findViewById<TextView>(R.id.tvSellDate) as TextView
        this.tvPrice = row?.findViewById<TextView>(R.id.tvPrice) as TextView
        this.tvTalkID = row?.findViewById<TextView>(R.id.tvTalkID) as TextView
        this.tvWriteDate = row?.findViewById<TextView>(R.id.tvWriteDate) as TextView
        this.linearLayout = row?.findViewById<LinearLayout>(R.id.linearLayout_dynamicArea) as LinearLayout
        this.firstV = row?.findViewById(R.id.firstV) as LinearLayout
        this.secondV = row?.findViewById(R.id.secondV) as LinearLayout
        this.thirdV = row?.findViewById(R.id.thirdV) as LinearLayout
        this.fourthV = row?.findViewById(R.id.forthV) as LinearLayout
        this.now_Sell = row?.findViewById(R.id.textView9) as TextView
    }
}