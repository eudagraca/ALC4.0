package mz.co.alc40phase2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_admin.*
import java.util.*




class AdminActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance()

        selectImage.setOnClickListener {
            launchGallery()
        }

        saveData.setOnClickListener {
            uploadImage()

        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        when (requestCode){
            PICK_IMAGE_REQUEST -> {
                filePath = data!!.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                image.setImageBitmap(bitmap)
            }
        }
    }

    private fun addUploadRecordToDb(uri: String){

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("deals").push()

        val dataRercod = Deals(name.editText!!.text.toString(),
            price.editText!!.text.toString(),
            local.editText!!.text.toString(),
            uri)


        myRef.setValue(dataRercod).addOnSuccessListener {
            Toast.makeText(baseContext, "Success Added", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(baseContext, "Failed Added", Toast.LENGTH_SHORT).show()
        }

    }

    private fun uploadImage() {
        val data = FirebaseStorage.getInstance()
        val ref = data.reference.child("uploads/" + UUID.randomUUID().toString())
        val uploadTask = ref.putFile(filePath!!)

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                addUploadRecordToDb(downloadUri.toString())


            } else {
                // Handle failures
                // ...
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.admin_item -> {
                true
            }
            R.id.user_item ->{
                startActivity(Intent(this@AdminActivity, UserActivity::class.java))
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
