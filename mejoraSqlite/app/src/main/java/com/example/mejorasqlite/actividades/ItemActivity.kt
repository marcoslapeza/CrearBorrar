package www.iesmurgi.proyectosqlite.actividades

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mejorasqlite.R
import com.example.mejorasqlite.databinding.DescripcionBinding

class ItemActivity:AppCompatActivity() {
    lateinit var binding: DescripcionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DescripcionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cogerDatos()
    }

    fun cogerDatos() {
        val datos = intent.extras
        if (datos != null) {

            binding.etTitulo.text = datos.getString("TITULO")
            binding.etCategoria.text = datos.getString("CONSOLA")
            binding.etVisto.text = datos.getString("JUGADO")

            binding.etEstrellas.rating = datos.getFloat("ESTRELLAS")

            binding.etDescripcion.text = datos.getString("DESCRIPCION")

            val byteArray = datos.getByteArray("IMAGEN") // tu array de bytes
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size ?: 0)
            binding.etImg.setImageBitmap(bitmap)


        }


    }
}