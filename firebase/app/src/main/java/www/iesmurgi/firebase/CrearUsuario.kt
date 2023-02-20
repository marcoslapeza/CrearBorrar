package www.iesmurgi.firebase


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CrearUsuario : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_usuario)

        findViewById<Button>(R.id.creaUser).setOnClickListener(){
            var mail:String=findViewById<EditText>(R.id.Cmail).text.toString()
            var contra:String = findViewById<EditText>(R.id.Ccontra).text.toString()
            var contra2 = findViewById<EditText>(R.id.Ccontra2).text.toString()
            if (mail!=null && contra!=null && contra2!=null){
                if (contra.equals(contra2)){
                    crearUsuario(mail,contra)
                }
            }
        }

    }

    fun crearUsuario(email: String, contrasena: String){

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email,
                contrasena
            ).addOnCompleteListener{
                if (it.isSuccessful){
                    val db = FirebaseFirestore.getInstance()

                    val data = hashMapOf(
                        "direccion" to "pula",
                        "edad" to "999",
                        "nombre" to "paco"
                    )
                    db.collection("user").document(email).set(data)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot successfully written!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error writing document", e)
                        }
                    val intent = Intent(this, Perfil::class.java)
                    intent.putExtra("MAIL",email)
                    startActivity(intent)
                }else {
                    Toast.makeText(this,"error", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

