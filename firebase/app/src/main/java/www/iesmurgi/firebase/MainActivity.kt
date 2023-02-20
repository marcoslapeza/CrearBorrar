package www.iesmurgi.firebase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.rpc.context.AttributeContext.Auth

class MainActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth=FirebaseAuth.getInstance()

        findViewById<Button>(R.id.inicioSesion).setOnClickListener(){
            var mail=findViewById<EditText>(R.id.Imail).text.toString()
            var contra= findViewById<EditText>(R.id.Icontra).text.toString()
            if(mail.length>0 && contra.length>0){
                iniciarSesion(mail,contra)
            }else{
                Toast.makeText(this,"Rellene los campos de usuario y contraseña", Toast.LENGTH_SHORT).show()
            }

        }

        findViewById<Button>(R.id.crear).setOnClickListener(){
            val intent = Intent(this, CrearUsuario::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInButton = findViewById<ImageView>(R.id.sign_in_button)

        signInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Companion.RC_SIGN_IN){
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK){
                val user =FirebaseAuth.getInstance().currentUser

                startActivity(Intent(this,Perfil::class.java))
                finish()
            }
        }
    }

    fun iniciarSesionGoogle(){
        val providerGoogle = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providerGoogle).build(),
            Companion.RC_SIGN_IN
        )
    }

    companion object {
        private const val RC_SIGN_IN=423
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            startActivity(Intent(this, Perfil::class.java))
            finish()
        }
    }

    fun iniciarSesion(email:String, contrasena:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email,
            contrasena
        ).addOnCompleteListener{
            if (it.isSuccessful){
                val intent = Intent(this, Perfil::class.java)
                intent.putExtra("MAIL",email)
                startActivity(intent)
            }else {
                Toast.makeText(this,"error al iniciar sesion", Toast.LENGTH_SHORT).show()
            }
        }
    }
}