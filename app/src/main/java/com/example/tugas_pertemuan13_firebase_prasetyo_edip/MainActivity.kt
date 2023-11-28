package com.example.tugas_pertemuan13_firebase_prasetyo_edip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tugas_pertemuan13_firebase_prasetyo_edip.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val noteCollectionRef = firestore.collection("notes")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnAdd.setOnClickListener {
                val nama = txtName.text.toString()
                val titles = txtTitle.text.toString()
                val detail = txtDetail.text.toString()
                val newNote = Note(
                    name = nama,
                    title = titles,
                    details = detail,
                )
                addDataNote(newNote)
                startActivity(Intent(this@MainActivity, NoteActivity::class.java))
            }
            btnBack.setOnClickListener {
                startActivity(Intent(this@MainActivity, NoteActivity::class.java))
            }
        }
    }

    private fun getAllNotes() {
        trackNoteChanges()
    }

    private fun trackNoteChanges() {
        noteCollectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.d("MainActivity", "An error occurred while changing the note: ", error)
                return@addSnapshotListener
            }
            val notes = snapshot?.toObjects(Note::class.java)
            if (notes != null) {
                Log.d("MainActivity", "Notes: $notes")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

    private fun addDataNote(note: Note) {
        noteCollectionRef.add(note)
            .addOnSuccessListener { docRef ->
                val createNoteId = docRef.id
                note.id = createNoteId
                docRef.set(note)
                    .addOnFailureListener{
                        Log.d("MainActivity", "An error occurred while updating a note id", it)
                    }
                resetForm()
            }
            .addOnFailureListener{
                Log.d("MainActivity", "An error occurred while adding a note", it)
            }
    }

    private fun resetForm() {
        with(binding) {
            txtName.setText("")
            txtTitle.setText("")
            txtDetail.setText("")
        }
    }
}
