package www.iesmurgi.juegotablero

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var color:RadioButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val columna = findViewById<SeekBar>(R.id.columna)
        val tColumna = findViewById<TextView>(R.id.colum)
        val fila = findViewById<SeekBar>(R.id.fila)
        val tfila = findViewById<TextView>(R.id.filass)
        var trama = findViewById<SeekBar>(R.id.trama)
        var tTrama = findViewById<TextView>(R.id.tramas)
        var sonido = findViewById<CheckBox>(R.id.sonido)
        var vibracion = findViewById<CheckBox>(R.id.vibracion)
        color = findViewById<RadioButton>(R.id.colores)
        var nColumna:Int=3
        var nFilas:Int=3
        var nTramas:Int=2

        columna.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                nColumna = progress+3
                tColumna.text = "Nº de Columnas: $nColumna"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        fila.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                nFilas = progress+3
                tfila.text = "Nº de Filas: $nFilas"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        trama.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                nTramas = progress+2
                tTrama.text = "Nº de Tramas: $nTramas"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        findViewById<ImageView>(R.id.acercade).setOnClickListener(){
            val intent = Intent(this, acercaDe::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.info).setOnClickListener(){
            val intent = Intent(this, info::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.jugar).setOnClickListener(){
            starPlay(nColumna,nFilas,nTramas,vibracion,sonido)
        }
    }

    fun starPlay(nColumna:Int, nFilas:Int,nTramas:Int,vibracion:CheckBox,sonido:CheckBox){
        val intent = Intent(this, GameField::class.java)
        intent.putExtra("COLUMNA",nColumna)
        intent.putExtra("FILA", nFilas)
        if (sonido.isChecked){
            intent.putExtra("SONIDO", true)
        }else{
            intent.putExtra("SONIDO", false)
        }
        if (vibracion.isChecked){
            intent.putExtra("VIBRACION", true)
        }else{
            intent.putExtra("VIBRACION", false)
        }
        if (color.isChecked){
            intent.putExtra("modo",0)
        }else {
            intent.putExtra("modo",1)
        }
        intent.putExtra("TRAMAS", nTramas)
        startActivity(intent)
    }
}