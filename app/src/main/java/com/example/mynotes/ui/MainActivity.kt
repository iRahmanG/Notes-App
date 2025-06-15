package com.example.mynotes.ui

import android.content.Intent
import android.os.Bundle
// import android.os.PersistableBundle // Generally not needed for typical Activity state
// import android.view.View // Not directly used in this snippet
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
// import androidx.recyclerview.widget.LinearLayoutManager // You are using GridLayoutManager
// import androidx.core.view.ViewCompat // Not directly used in this snippet
// import androidx.core.view.WindowInsetsCompat // Not directly used in this snippet
// import com.example.mynotes.R // Not directly used in this snippet
import com.example.mynotes.data.Note
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.ui.adapter.NoteAdapter
import com.example.mynotes.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar // Import for Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    // register for result from AddEditNoteActivity
    private val addEditNoteLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val note = data?.getSerializableExtra("note") as? Note
            val isEdit = data?.getBooleanExtra("isEdit", false) ?: false

            note?.let {
                if (isEdit)
                    noteViewModel.update(it)
                else
                    noteViewModel.insert(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Init ViewModel
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        //setup RecyclerView
        noteAdapter = NoteAdapter { note ->
            val intent = Intent(this, AddEditNoteActivity::class.java)
            intent.putExtra("note", note)
            addEditNoteLauncher.launch(intent)
        }

        binding.notesRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = noteAdapter
        }

        //observe LiveData
        noteViewModel.allNotes.observe(this) { notes ->
            noteAdapter.setNotes(notes) // This is correct for your current adapter
        }

        // floating Action Button click
        binding.addNoteBtn.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            addEditNoteLauncher.launch(intent)
        }

        // item touch helper for swipe to delete
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(
                0, // No drag functionality
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Allow swipe left or right
            ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // Not implementing drag and drop
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) { // Check for valid position
                    val note = noteAdapter.getNoteAt(position)
                    note?.let { noteToDelete -> // Renamed for clarity
                        noteViewModel.delete(noteToDelete)
                        // Optional: Show Snackbar with Undo action
                        Snackbar.make(binding.notesRecyclerView, "Note deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                noteViewModel.insert(noteToDelete) // Re-insert if UNDO is clicked
                            }
                            .show()
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.notesRecyclerView) // Corrected: Newline or semicolon needed
    }
}