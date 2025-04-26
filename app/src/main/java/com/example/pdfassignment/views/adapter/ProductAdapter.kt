package com.example.pdfassignment.views.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfassignment.R
import com.example.pdfassignment.model.localDB.entity.ProductEntity
import com.example.pdfassignment.viewModel.ProductViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductAdapter(
    private val onUpdateClick: (ProductEntity) -> Unit,
    private val onDeleteClick: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, ProductAdapter.ProductViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTxt = itemView.findViewById<TextView>(R.id.productName)
        private val dataTxt = itemView.findViewById<TextView>(R.id.productData)
        val updateBtn = itemView.findViewById<Button>(R.id.btnUpdate)
        val deleteBtn = itemView.findViewById<Button>(R.id.btnDelete)

        fun bind(product: ProductEntity) {
            nameTxt.text = product.name
//            dataTxt.text = product.data
            val json = product.data
            if (!json.isNullOrEmpty()) {
                try {
                    // Parse the JSON string into a Map
                    val map: Map<String, Any> = Gson().fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
                    dataTxt.text = map.entries.joinToString("\n \n") { "${it.key}: ${it.value}" }

                } catch (e: Exception) {
                    e.printStackTrace()
                    dataTxt.text = "Data not found"
                }
            } else {
                dataTxt.text = "No product details available"
            }


            updateBtn.setOnClickListener { onUpdateClick(product) }
            deleteBtn.setOnClickListener { onDeleteClick(product) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity) = oldItem == newItem
    }



}
