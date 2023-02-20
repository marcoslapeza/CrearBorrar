package www.iesmurgi.firebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Cambios : AppCompatActivity() {
    private lateinit var user:FirebaseUser
    private lateinit var auth:FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambios)
        val mail: String? = intent.getStringExtra("MAIL")

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        findViewById<Button>(R.id.edNombre).setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cambiar el nombre")

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, which ->
                val documento = Firebase.firestore.collection("user").document(user.email.toString())
                documento.update("nombre",input.text.toString())
            }
            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()
            dialog.show()
        }
        findViewById<Button>(R.id.edEdad).setOnClickListener(){

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cambiar el edad")

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, which ->
                val documento = Firebase.firestore.collection("user").document(user.email.toString())
                documento.update("edad",input.text.toString())
            }
            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()
            dialog.show()
        }

        findViewById<Button>(R.id.edDireccion).setOnClickListener(){

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cambiar el direccion")

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            builder.setView(input)

            builder.setPositiveButton("OK") { dialog, which ->
                val documento = Firebase.firestore.collection("user").document(user.email.toString())
                documento.update("direccion",input.text.toString())
            }
            builder.setNegativeButton("direccion") { dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()
            dialog.show()
        }

        findViewById<Button>(R.id.borrarUser).setOnClickListener() {


            }

        findViewById<Button>(R.id.volverPerfil).setOnClickListener(){
            startActivity(Intent(this,Perfil::class.java))
        }
    }
}