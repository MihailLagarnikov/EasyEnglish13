package ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.lagarnikov.easyenglish13.MyViewModel
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.databinding.ItemThemeRecBinding
import ru.lagarnikov.easyenglish13.databinding.ItemThemeWordsRecBinding

class AdapterRecOneTheme(val listDataOneTheme:ArrayList<DataOneTheme>, val mModel: MyViewModel)
    :RecyclerView.Adapter<AdapterRecOneTheme.ViewHolder>() {
    lateinit var binding:ItemThemeWordsRecBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRecOneTheme.ViewHolder {

        binding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_theme_words_rec,
            parent, false)
        val view= binding.root
        return AdapterRecOneTheme.ViewHolder(view, binding)
    }

    override fun getItemCount(): Int {
        return listDataOneTheme.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=listDataOneTheme.get(position)
        val n:String= (position+1).toString()
        holder.number.setText(n)
        holder.english.setText(data.english)
        holder.russian.setText(data.rus)
        holder.playSound.setOnClickListener(View.OnClickListener {
            mModel.setNextWordSpeach(data.english)
        })
        holder.deletWord.setOnClickListener(View.OnClickListener {
            //Логика удаления слова из списка изучаемых

        })
    }






    class ViewHolder(itemView: View, binding: ItemThemeWordsRecBinding):RecyclerView.ViewHolder(itemView){
        val number=binding.textViewNumber
        val english=binding.textViewEnglish
        val russian=binding.textViewRussn
        val deletWord: TextView =binding.textViewDelet
        val playSound: ImageView =binding.imageView14
        val card: CardView =binding.cardWord
    }
}