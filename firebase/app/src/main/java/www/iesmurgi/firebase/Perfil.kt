package www.iesmurgi.firebase

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class Perfil : AppCompatActivity() {
    lateinit var mostrar:TextView
    lateinit var nombre:TextView
    lateinit var edad:TextView
    lateinit var direccion:TextView
    lateinit var mEmail:TextView

    var auth:FirebaseAuth=FirebaseAuth.getInstance()
    var user:FirebaseUser? = auth.currentUser

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        val mail:String = user?.email.toString()

        mostrar = findViewById<TextView>(R.id.mostrarnom)
        nombre  =findViewById<TextView>(R.id.nombre)
        edad  =findViewById<TextView>(R.id.edad)
        direccion  =findViewById<TextView>(R.id.direccion)
        mEmail  =findViewById<TextView>(R.id.memail)
        mEmail.text = user?.email.toString()
        var heEntrado:Boolean = false

        if (user != null){
            setup(user?.email.toString())
        }

        if (mail != null) {
            heEntrado=true
            extraerDatos(mail)
        }

        if (heEntrado==false){
            val text = findViewById<TextView>(R.id.memail).text.toString()
            extraerDatos(text)
        }

        findViewById<Button>(R.id.editar).setOnClickListener(){
            val intent = Intent(Intent(this, Cambios::class.java))
            startActivity(intent)
        }
    }

    fun setup(email: String){
        var correo = findViewById<TextView>(R.id.memail)
        correo.text = email

        val cerrar = findViewById<Button>(R.id.cerrarSesion)

        cerrar.setOnClickListener(){
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    fun extraerDatos(email:String){
        val firestore = FirebaseFirestore.getInstance()
        val docRef = firestore.document("user/"+email)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                val data = documentSnapshot.data
                if(data != null) {
                    mEmail.text = email
                    nombre.text = data.get("nombre") as CharSequence?
                    edad.text = data.get("edad") as CharSequence?
                    direccion.text = data.get("direccion") as CharSequence?
                }
            } else {
                Log.d(TAG, "No existe el documento")
            }
        }.addOnFailureListener { e -> Log.w(TAG, "Error obteniendo documento", e) }
    }
}