package com.example.tugas_pertemuan13_firebase_prasetyo_edip

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_pertemuan13_firebase_prasetyo_edip.databinding.ActivityNoteBinding
import com.google.firebase.firestore.FirebaseFirestore

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    private lateinit var itemAdapter: NoteAdapter
    private val listViewData = ArrayList<Note>()
    private val firestore = FirebaseFirestore.getInstance()
    private val noteCollectionRef = firestore.collection("notes")
    private var updateId = ""
    private val noteListLiveData: MutableLiveData<List<Note>> by lazy {
        MutableLiveData<List<Note>>()
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        configureButtonClickHandlers()
        updateNoteListOnTracking()
        getAllNotes()
    }

    private fun setupRecyclerView() {
        itemAdapter = NoteAdapter(this, listViewData) { item ->
            updateId = item.id
            val intentToForm = Intent(this, UpdateActivity::class.java)
                .apply {
                    putExtra(EXTRA_NAME, item.name)
                    putExtra(EXTRA_TITLE, item.title)
                    putExtra(EXTRA_DETAIL, item.details)
                    putExtra(EXTRA_ID, item.id)
                }
            startActivity(intentToForm)
        }

        binding.rvNote.apply {
            layoutManager = LinearLayoutManager(this@NoteActivity)
            adapter = itemAdapter
        }
    }

    private fun configureButtonClickHandlers() {
        binding.btnAdd.setOnClickListener {
            val intentToForm = Intent(this@NoteActivity, MainActivity::class.java)
            startActivity(intentToForm)
        }
    }

    private fun getAllNotes() {
        trackNoteChanges()
    }

    private fun trackNoteChanges() {
        noteCollectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d("NoteActivity", "An error occurred while changing the note: ", error)
                return@addSnapshotListener
            }
            val notes = snapshot?.toObjects(Note::class.java)
            if (notes != null) {
                noteListLiveData.postValue(notes)
            }
        }
    }

    private fun updateNoteListOnTracking() {
        noteListLiveData.observe(this) { notes ->
            listViewData.clear()
            listViewData.addAll(notes)
            itemAdapter.notifyDataSetChanged()

            Log.d("NoteActivity", "Number of notes: ${notes.size}")
        }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
}
