package www.iesmurgi.formulario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.SeekBar
import www.iesmurgi.formulario.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.seekBar.setOnSeekBarChangeListener(this);


        binding.nombreeeee.inputType = InputType.TYPE_CLASS_TEXT
        binding.codPostal.inputType = InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
        binding.direccion.inputType = InputType.TYPE_CLASS_TEXT
        binding.localidad.inputType = InputType.TYPE_CLASS_TEXT
        binding.nombrePadre.inputType = InputType.TYPE_CLASS_TEXT
        binding.nombreMadre.inputType = InputType.TYPE_CLASS_TEXT
        binding.tlfPersonal.inputType = InputType.TYPE_CLASS_PHONE
        binding.tlfemergencias.inputType = InputType.TYPE_CLASS_PHONE
        binding.web.inputType =InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_URI
        binding.email.inputType = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        binding.nacimiento.inputType = InputType.TYPE_CLASS_DATETIME
        binding.altura.inputType = InputType.TYPE_CLASS_NUMBER+ InputType.TYPE_NUMBER_FLAG_DECIMAL

        binding.localidad.requestFocus()
        //binding.nombre.error= "obligatorio"


    }


    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        binding.progeso.text = p1.toString()
        binding.estado.text = "arrastrando"
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        binding.estado.text="toque"
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        binding.estado.text = "terminado"
    }
}


