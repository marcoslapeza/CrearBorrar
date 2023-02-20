package www.iesmurgi.proyectosqlite.actividades

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mejorasqlite.R
import com.example.mejorasqlite.databinding.CrearModificarBinding
import www.iesmurgi.proyectosqlite.adapter.JuegosAdapter
import www.iesmurgi.proyectosqlite.bbdd.BaseDatosJuegos
import www.iesmurgi.proyectosqlite.bbdd.Juegos
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class Crear_Modificar:AppCompatActivity() {
    var titulo = ""
    var descripcion = ""
    var categoria = ""
    var estrellas = 0.0f
    var img = null
    var id: Int? = null
    var visto = ""
    var editar = false

    lateinit var binding: CrearModificarBinding
    lateinit var conexion: BaseDatosJuegos
    lateinit var miAdapter: JuegosAdapter
    var lista = mutableListOf<Juegos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CrearModificarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        conexion = BaseDatosJuegos(this)
        var button= binding.button2
        button.setOnClickListener{
            abrirFoto()
        }


        binding.imageView.setImageResource(R.drawable.nophoto)
        datosNoVacios()
        // cargarLista()
        cogerDatos()
        setListeners()
    }
    private fun abrirFoto() {

     var options = listOf("Tomar foto", "Elegir de la galería")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Elige una opción")
        builder.setItems(options.toTypedArray()){ _, item ->
            when {
                options[item] == "Tomar foto" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, PICK_IMAGE_REQUEST_CAMERA)
                }
                options[item] == "Elegir de la galería" -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, PICK_IMAGE_REQUEST_GALERIA)
                }
            }
        }
        builder.show()
    }


    private fun setListeners() {
        binding.imageView.setOnClickListener {

            openGallery()
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }
        binding.btnCrear.setOnClickListener {
            crearRegistro()
        }
    }


    fun datosNoVacios() {

        binding.etTitulo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false

                if (input.isNotEmpty()) {

                    binding.Titulo.error = null

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etDescripcion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                var found = false

                if (input.isNotEmpty()) {

                    binding.Descripcion.error = null

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }


    private fun cogerDatos() {
        val datos = intent.extras
        if (datos != null) {
            editar = true
            binding.btnCrear.text = "EDITAR"
            val anime = datos.getSerializable("ANIMES") as Juegos
            id = anime.id
            binding.etTitulo.setText(anime.titulo)
            //binding.etCategoria.setText(anime.categoria)
            binding.etDescripcion.setText(anime.descripcion)

            binding.autocompleteCategoria.setText(anime.consola)

            binding.autocompleteVisto.setText(anime.jugado)


            binding.etStar.rating = anime.estrellas

            val byteArray = anime.imagen // tu array de bytes
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            binding.imageView.setImageBitmap(bitmap)

        }
    }

    private fun crearRegistro() {

        titulo = binding.etTitulo.text.toString().trim()
        descripcion = binding.etDescripcion.text.toString().trim()
        categoria = binding.autocompleteCategoria.text.toString().trim()
        estrellas = binding.etStar.rating

        visto = binding.autocompleteVisto.text.toString().trim()


        val imageView: ImageView = binding.imageView
        if (imageView.drawable != null) {
            val drawable = imageView.getDrawable()
            val bitmap = (drawable as BitmapDrawable).bitmap

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            var listaCategoria = listOf<String>("XBOX/X", "PS5", "SWITCH")
            var listaVisto = listOf<String>("Jugado", "No Jugado", "Jugándolo")

            var txt = binding.autocompleteCategoria.text
            var foundCategoria = false
            for (item in listaCategoria) {
                if (item.equals(txt)) {
                    foundCategoria = true
                    break
                }

            }

            var txt2 = binding.autocompleteVisto.text
            var foundVisto = false
            for (item in listaVisto) {
                if (item.equals(txt2)) {
                    foundVisto = true
                    break
                }
            }

            if (binding.etTitulo.text!!.isEmpty()) {

                binding.Titulo.error = "El titulo no puede estar vacio"


            } else if (binding.etDescripcion.text!!.isEmpty()) {

                binding.Descripcion.error = "La descripcion no puede estar vacia"

            } else if (binding.autocompleteVisto.text.isEmpty()) {
                    //categoria, byteArray, titulo, descripcion, estrellas, visto

                binding.Visto.error = "Este campo no puede estar vacio"

            } else if (binding.autocompleteCategoria.text.isEmpty()) {
                binding.Visto.error = "Este campo no puede estar vacio."
            } else  {
                if (!editar) {
                    val anime =
                        Juegos(1, categoria, byteArray, titulo, descripcion, estrellas, visto)
                    if (conexion.create(anime) > -1) {
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "No se pudo guardar el registro!!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                } else {
                    val anime =
                        Juegos(1, categoria, byteArray, titulo, descripcion, estrellas, visto)
                    if (conexion.update(anime) > -1) {
                        finish()
                    } else {
                        Toast.makeText(this, "No se pudo editar el registro!!!", Toast.LENGTH_SHORT)
                            .show()
                    }

                }


            }

        } else {
            println("eror")
        }


    }


    companion object {

        const val REQUEST_CODE_GALLERY = 1
        const val CAMERA_REQUEST_CODE = 31
        const val PICK_IMAGE_REQUEST_GALERIA=20
        const val PICK_IMAGE_REQUEST_CAMERA=21
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            binding.imageView.setImageURI(imageUri)

        } else if ( requestCode == PICK_IMAGE_REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            val imagen = data?.extras?.get("data") as Bitmap
            binding.imageView.setImageBitmap(imagen)
        }
    }
}