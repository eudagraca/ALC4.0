package mz.co.alc40phase2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class DealsAdapter(val context: Context,
    private val dealsList: List<Deals>
):
        RecyclerView.Adapter<DealsAdapter.BooksViewHolder>(){


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BooksViewHolder {
        val inflatedView = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_deals, viewGroup, false)
        return BooksViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = dealsList.size

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val itemBook: Deals = dealsList[position]
        holder.bind(itemBook)
    }

    inner class BooksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var name: TextView = itemView.findViewById(R.id.nameL)
        private var location: TextView = itemView.findViewById(R.id.locationL)
        private var price: TextView = itemView.findViewById(R.id.priceL)

        private var image: ImageView = itemView.findViewById(R.id.imgL)


        fun bind(deals: Deals) {
            name.text = deals.name
            location.text = deals.local
            price.text   = deals.price


            Glide
                .with(context)
                .load(deals.image)
                .centerCrop()
                .into(image);
        }
    }

}