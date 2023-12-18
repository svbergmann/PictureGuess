package com.github.svbergmann.pictureguess

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TableRow
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.svbergmann.pictureguess.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val changeImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                binding.selectedImage.setImageURI(imgUri)
                binding.tableLayout.removeAllViews()
                binding.addBoxes.setOnClickListener {
                    createBoxes()
                }
            }
        }

    private fun createBoxes() {
        val imageHeight = binding.selectedImage.height
        val imageWidth = binding.selectedImage.width
        val numberOfBoxesH = 5
        val numberOfBoxesV = 5
        val boxHeight = imageHeight / numberOfBoxesV
        val boxWidth = imageWidth / numberOfBoxesH
        for (row in 0..numberOfBoxesV) {
            val tableRow = TableRow(this)
            for (column in 0..numberOfBoxesH) {
                Button(this).apply {
                    this.setBackgroundColor(Color.BLACK)
                    this.setOnClickListener { this.setBackgroundColor(Color.TRANSPARENT) }
                    tableRow.addView(this, boxWidth, boxHeight)
                }
            }
            binding.tableLayout.addView(tableRow, row)
        }
        binding.tableLayout.bringToFront()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pickImage.setOnClickListener {
            val pickImg =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }
    }
}