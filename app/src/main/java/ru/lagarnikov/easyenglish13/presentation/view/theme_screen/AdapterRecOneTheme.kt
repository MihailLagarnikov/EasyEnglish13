package ru.lagarnikov.easyenglish13.presentation.view.theme_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.lagarnikov.easyenglish13.R
import ru.lagarnikov.easyenglish13.databinding.ItemThemeWordsRecBinding
import ru.lagarnikov.easyenglish13.domain.UiPresenter
import ru.lagarnikov.easyenglish13.data.model.DataOneTheme

class AdapterRecOneTheme(val listDataOneTheme:ArrayList<DataOneTheme>, val mModel: UiPresenter.ViewModelTestFragments)
    :RecyclerView.Adapter<AdapterRecOneTheme.ViewHolder>() {
    lateinit var binding:ItemThemeWordsRecBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding= DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_theme_words_rec,
            parent, false)
        val view= binding.root
        return ViewHolder(view, binding)
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
            mModel.setNextWordSpeech(data.english)
        })

    }






    class ViewHolder(itemView: View, binding: ItemThemeWordsRecBinding):RecyclerView.ViewHolder(itemView){
        val number=binding.textViewNumber
        val english=binding.textViewEnglish
        val russian=binding.textViewRussn
        val playSound: ImageView =binding.imageView14
    }
}