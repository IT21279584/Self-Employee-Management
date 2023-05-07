package com.example.selfemployeesmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EmployeeSelfProfile : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etContactNo: EditText
    private lateinit var etTalentDescription: EditText

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employeeselfprofile)
        supportActionBar?.hide()

        etFirstName = findViewById(R.id.selfEtFirstname)
        etLastName = findViewById(R.id.selfEtLastname)
        etEmail = findViewById(R.id.selfEtEmail)
        etPassword = findViewById(R.id.selfEtPassword)
        etContactNo = findViewById(R.id.selfEtPhoneNo)
        etTalentDescription = findViewById(R.id.selfEtTalentDescription)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Employees").child(FirebaseAuth.getInstance().currentUser!!.uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val firstName = dataSnapshot.child("fname").getValue(String::class.java)
                    val lastName = dataSnapshot.child("lname").getValue(String::class.java)
                    val email = dataSnapshot.child("uemail").getValue(String::class.java)
                    val password = dataSnapshot.child("upassword").getValue(String::class.java)
                    val phone = dataSnapshot.child("uphoneNo").getValue(String::class.java)
                    val talent = dataSnapshot.child("udescription").getValue(String::class.java)

                    etFirstName.setText(firstName)
                    etLastName.setText(lastName)
                    etEmail.setText(email)
                    etPassword.setText(password)
                    etContactNo.setText(phone)
                    etTalentDescription.setText(talent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //err
            }
        })

        val updateButton = findViewById<Button>(R.id.selfBtnEditProfile)
        updateButton.setOnClickListener {
            val updatedFirstName = etFirstName.text.toString().trim()
            val updatedLastName = etLastName.text.toString().trim()
            val updatedEmail = etEmail.text.toString().trim()
            val updatedPassword = etPassword.text.toString().trim()
            val updatedContactNo = etContactNo.text.toString().trim()
            val updatedTalentDescription = etTalentDescription.text.toString().trim()

            val userUpdates = hashMapOf<String, Any>(
                "fname" to updatedFirstName,
                "lname" to updatedLastName,
                "uemail" to updatedEmail,
                "upassword" to updatedPassword,
                "uphoneNo" to updatedContactNo,
                "udescription" to updatedTalentDescription
            )

            databaseReference.updateChildren(userUpdates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
        }

        val deleteButton = findViewById<Button>(R.id.selfBtnDeleteProfile)
        deleteButton.setOnClickListener {
            FirebaseAuth.getInstance().currentUser?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        databaseReference.removeValue()
                        Toast.makeText(this, "Profile deleted successfully", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
        }
    }
}
