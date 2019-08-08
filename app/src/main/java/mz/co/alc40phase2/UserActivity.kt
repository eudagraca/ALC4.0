package mz.co.alc40phase2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        setSupportActionBar(toolbaru);

            with(rv){
                layoutManager = LinearLayoutManager(this@UserActivity, RecyclerView.VERTICAL, false)
                setHasFixedSize(true)

                val dealsList = ArrayList<Deals>()

                val ref = FirebaseDatabase.getInstance().getReference("deals")
                ref.addListenerForSingleValueEvent(object:ValueEventListener {
                    override fun onDataChange(dataSnapshot:DataSnapshot) {

                        for (snapshot in dataSnapshot.children) {
                            if (dataSnapshot.exists()) {
                                val deals = Deals(
                                    snapshot.child("name").value.toString(),
                                    snapshot.child("price").value.toString(),
                                    snapshot.child("local").value.toString(),
                                    snapshot.child("image").value.toString()
                                )

                                dealsList.add(deals)
                            }
                        }

                        adapter = DealsAdapter(baseContext,dealsList)
                    }
                    override fun onCancelled(databaseError:DatabaseError) {
                        throw databaseError.toException()
                    }
                })


        }

    }

}
