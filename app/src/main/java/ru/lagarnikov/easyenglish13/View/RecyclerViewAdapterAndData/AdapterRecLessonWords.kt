package ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.lagarnikov.easyenglish13.MyViewModel
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.Room.DataSql
import ru.lagarnikov.easyenglish13.databinding.ItemLessonWordcBinding

class AdapterRecLessonWords(val list:ArrayList<DataSql>, val mModel: MyViewModel, var openTranslate:Boolean):
    RecyclerView.Adapter<AdapterRecLessonWords.ViewHolder>() {

    lateinit var binding:ItemLessonWordcBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRecLessonWords.ViewHolder {

        binding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_lesson_wordc,
            parent, false)
        val view= binding.root
        return AdapterRecLessonWords.ViewHolder(view,binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=list.get(position)
        val n:String= (position+1).toString()
        holder.number.setText(n)
        holder.english.setText(data.en)
        holder.russian.setText(data.ru)
        holder.card.setOnClickListener(View.OnClickListener {
            mModel.setNextWordSpeach(data.en)
        })

        holder.frame.setOnClickListener(View.OnClickListener {
            if (View.GONE==holder.russian.visibility){
                holder.russian.visibility=View.VISIBLE
                holder.minimizeCard.rotation=180F
            }else{
                holder.russian.visibility=View.GONE
                holder.minimizeCard.rotation=0F
            }
        })

        if (openTranslate){
            holder.russian.visibility=View.VISIBLE
            holder.minimizeCard.rotation=180F
        }else{
            holder.russian.visibility=View.GONE
            holder.minimizeCard.rotation=0F
        }
    }





    class ViewHolder(itemView: View, binding: ItemLessonWordcBinding):RecyclerView.ViewHolder(itemView){
        val number=binding.textViewNumber
        val english=binding.textViewEnglish
        val russian=binding.textViewRussn
        val minimizeCard: ImageView =binding.imageView17
        val card: CardView =binding.cardWord
        val frame:FrameLayout=binding.frameLesson
    }
}