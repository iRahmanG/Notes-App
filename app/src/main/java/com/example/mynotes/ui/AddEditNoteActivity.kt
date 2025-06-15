package com.example.mynotes.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynotes.data.Note
import com.example.mynotes.databinding.ActivityAddEditNoteBinding
import com.example.mynotes.databinding.ActivityMainBinding
import kotlinx.serialization.Serializable

class AddEditNoteActivity : AppCompatActivity(){

    private lateinit var binding :ActivityAddEditNoteBinding
    private var currentNote: Note? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // CHECK if editing an existing note
        currentNote = intent.getSerializableExtra("note") as? Note
        currentNote?.let {
            binding.editTitle.setText(it.title)
            binding.editDescription.setText(it.description)
        }

        // save button click
        binding.saveNoteBtn.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val description = binding.editDescription.text.toString()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please enter title and description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newNote =Note(
                id = currentNote?.id ?:0,
                title = title,
                description = description,
                timeStamp = System.currentTimeMillis()
            )

            val resultIntent = Intent()
            resultIntent.putExtra("note",newNote )
            resultIntent.putExtra("isEdit",currentNote!=null)
            setResult(RESULT_OK,resultIntent)
            finish()
        }
    }

}
