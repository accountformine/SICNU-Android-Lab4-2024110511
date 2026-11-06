package cn.edu.sicnu.guoxiangyu.cardgame

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.guoxiangyu.cardgame.model.CardMatchingGame

class CardAdapter(val game: CardMatchingGame) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardButton: Button = itemView.findViewById(R.id.button)
    }

    private var mListener: ((Int) -> Unit)? = null

    fun setOnCardClickListener(l: (Int) -> Unit) {
        mListener = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return game.cards.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = game.cardAtIndex(position)
        holder.cardButton.isEnabled = !card.isMatched
        if (card.isChosen) {
            holder.cardButton.text = card.toString()
            holder.cardButton.setBackgroundColor(Color.WHITE)
        } else {
            holder.cardButton.text = holder.itemView.context.getString(R.string.empty_text)
            holder.cardButton.setBackgroundResource(R.drawable.sicnu)
        }
        holder.cardButton.setOnClickListener {
            mListener?.invoke(holder.bindingAdapterPosition)
        }
    }
}
