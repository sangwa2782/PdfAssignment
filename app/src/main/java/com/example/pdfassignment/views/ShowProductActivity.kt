package com.example.pdfassignment.views

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfassignment.R
import com.example.pdfassignment.databinding.ActivityShowProductBinding
import com.example.pdfassignment.model.localDB.entity.ProductEntity
import com.example.pdfassignment.viewModel.ProductViewModel
import com.example.pdfassignment.views.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowProductActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShowProductBinding

    private lateinit var adapter: ProductAdapter
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ProductAdapter(
//            onUpdateClick = { product -> updateProduct(product) },
            onUpdateClick = { product -> showUpdateDialog(this, product, productViewModel) },
            onDeleteClick = { product -> productViewModel.deleteProduct(product) }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        productViewModel.products.observe(this) { productList ->
            adapter.submitList(productList)
        }

        productViewModel.fetchProducts() // Initial API call

    }

    private fun updateProduct(product: ProductEntity) {
        val updated = product.copy(name = "${product.name} (Updated)")
        productViewModel.updateProduct(updated)
        Toast.makeText(this, "Updated ${product.name}", Toast.LENGTH_SHORT).show()
    }

    private fun showUpdateDialog(context: Context, product: ProductEntity, viewModel: ProductViewModel) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_product, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.editProductName)
        val dataEditText = dialogView.findViewById<EditText>(R.id.editProductData)
        val updateButton = dialogView.findViewById<Button>(R.id.updateButton)

        nameEditText.setText(product.name)
        dataEditText.setText(product.data)

        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        updateButton.setOnClickListener {
            val updatedName = nameEditText.text.toString().trim()
            val updatedData = dataEditText.text.toString().trim()

            if (updatedName.isNotEmpty()) {
                val updatedProduct = product.copy(
                    name = updatedName,
                    data = if (updatedData.isNotEmpty()) updatedData else null  // Save updated data properly
                )
                viewModel.updateProduct(updatedProduct)
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Product name can't be empty", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }



}