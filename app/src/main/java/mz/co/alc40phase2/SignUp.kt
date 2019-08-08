package mz.co.alc40phase2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUp : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        save.setOnClickListener {

            mAuth!!.createUserWithEmailAndPassword(
                email.editText!!.text.toString(),
                password.editText!!.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("user")
                        val user = mAuth!!.currentUser

                        myRef.child(user!!.uid).setValue(name.editText!!.text.toString())
                    } else {

                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()

        val user = mAuth!!.currentUser
        if (user!=null){
            startActivity(Intent(baseContext, AdminActivity::class.java))
        }
    }
}
