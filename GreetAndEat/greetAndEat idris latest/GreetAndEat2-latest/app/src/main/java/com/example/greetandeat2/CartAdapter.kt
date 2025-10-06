package com.example.greetandeat2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val items: List<MenuItem>,
    private val onRemoveClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvCartItemName)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        private val tvQuantity: TextView = itemView.findViewById(R.id.tvCartItemQuantity)
        private val btnRemove: Button = itemView.findViewById(R.id.btnRemove)

        fun bind(item: MenuItem) {
            tvName.text = item.name
            tvQuantity.text = "Qty: ${item.quantity}"
            val subtotal = item.price * item.quantity
            tvPrice.text = "R$subtotal"

            btnRemove.setOnClickListener {
                onRemoveClick(item)
            }
        }
    }
}
