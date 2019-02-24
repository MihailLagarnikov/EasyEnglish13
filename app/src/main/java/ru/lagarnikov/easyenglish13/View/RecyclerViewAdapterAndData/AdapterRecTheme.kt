package ru.lagarnikov.easyenglish13.View.RecyclerViewAdapterAndData

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.lagarnikov.easyenglish13.*
import ru.lagarnikov.easyenglish13.View.FragmentThemeWord
import ru.lagarnikov.easyenglish13.databinding.ItemThemeRecBinding

class AdapterRecTheme(val listDataAllTheme:ArrayList<DataAllTheme>, val mModel:MyViewModel, val resources: Resources):RecyclerView.Adapter<AdapterRecTheme.ViewHolder>() {
    lateinit var binding:ItemThemeRecBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding= DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_theme_rec,
            parent, false)
        val view= binding.root
        return ViewHolder(view,binding)
    }

    override fun getItemCount(): Int {
        return listDataAllTheme?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=listDataAllTheme.get(position)
        holder.numberWords.setText(data.numberWords)
        holder.nameTheme.setText(resources.getString(data.themeName).toUpperCase())
        holder.image.setImageResource(data.imag)
        if (data.progress==""){
            holder.progress.visibility=View.GONE
        }else{
            holder.progress.setText(data.progress)
        }
        holder.card.setOnClickListener(View.OnClickListener {
            InnerData.saveText(THEME_CURENT, holder.nameTheme.text.toString())
            mModel.setNextFragmentName(FragmentThemeWord())

        })
    }

    class ViewHolder(itemView: View,binding: ItemThemeRecBinding):RecyclerView.ViewHolder(itemView){
        val numberWords=binding.textViewThemeWords
        val nameTheme=binding.textViewThemeName
        val progress=binding.textViewProgressTheme
        val image:ImageView=binding.imageViewTheme
        val card:CardView=binding.cardView
    }
}