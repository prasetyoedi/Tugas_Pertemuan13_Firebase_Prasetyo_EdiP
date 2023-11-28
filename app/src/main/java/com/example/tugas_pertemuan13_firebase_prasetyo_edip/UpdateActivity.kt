package com.example.tugas_pertemuan13_firebase_prasetyo_edip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tugas_pertemuan13_firebase_prasetyo_edip.databinding.ActivityUpdateBinding
import com.google.firebase.firestore.FirebaseFirestore

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val noteCollectionRef = firestore.collection("notes")
    private var updateId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val id = intent.getStringExtra(NoteActivity.EXTRA_ID)
            if (id.isNullOrEmpty()) {
                Log.d("UpdateActivity", "Invalid ID: $id")
                finish()
            }

            val name = intent.getStringExtra(NoteActivity.EXTRA_NAME)
            val title = intent.getStringExtra(NoteActivity.EXTRA_TITLE)
            val detail = intent.getStringExtra(NoteActivity.EXTRA_DETAIL)

            txtName.setText(name)
            txtTitle.setText(title)
            txtDetail.setText(detail)

            btnUpdate.setOnClickListener {
                val updatedData = Note(
                    id = id.toString(),
                    name = txtName.text.toString(),
                    title = txtTitle.text.toString(),
                    details = txtDetail.text.toString()
                )
                updateData(updatedData)
            }

            btnDelete.setOnClickListener {
                val noteToDelete = Note(id = id.toString(), "", "", "")
                deleteData(noteToDelete)
            }

            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun updateData(note: Note) {
        noteCollectionRef.document(note.id).set(note)
            .addOnSuccessListener {
                Log.d("UpdateActivity", "The data has been updated successfully")
                finish()
            }
            .addOnFailureListener {
                Log.d("UpdateActivity", "An error occurred while updating a note", it)
            }
    }

    private fun deleteData(note: Note) {
        if (note.id.isEmpty()) {
            Log.d("UpdateActivity", "An error occurred while deleting empty ID data")
            return
        }

        noteCollectionRef.document(note.id).delete()
            .addOnSuccessListener {
                Log.d("UpdateActivity", "The data has been deleted successfully")
                finish()
            }
            .addOnFailureListener {
                Log.d("UpdateActivity", "An error occurred while deleting note data", it)
            }
    }
}
