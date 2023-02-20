package www.iesmurgi.juegotablero

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import www.iesmurgi.juegotablero.databinding.ActivityGameFieldBinding
import java.util.*
import kotlin.random.Random.Default.nextInt

class GameField : AppCompatActivity() {

    private lateinit var bind: ActivityGameFieldBinding
    private var colores = intArrayOf(R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4, R.drawable.color5)
    private var numeros = intArrayOf(R.drawable.uno, R.drawable.dos, R.drawable.tres, R.drawable.cuatro, R.drawable.cinco )
    private var dibujos = intArrayOf()
    private var topTileX = 0
    private var topTileY = 0
    private var topElement = 0
    private var sonido = false
    private var vibracion = false
    private var ids = Array(topTileX) { IntArray(topTileY) }
    private var values = Array(topTileX) { IntArray(topTileY) }
    private var contador = 0
    private lateinit var mp: MediaPlayer
    private lateinit var vibratorService: Vibrator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityGameFieldBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val bundle = intent.extras
        if (bundle != null) {
            topTileX = bundle.getInt("COLUMNA")
            topTileY = bundle.getInt("FILA")
            topElement = bundle.getInt("TRAMAS")
            sonido = bundle.getBoolean("SONIDO")
            vibracion = bundle.getBoolean("VIBRACION")
            var modo = bundle.getInt("modo")


            if (modo == 0){
                dibujos = colores.copyOf()
            }else{
                dibujos = numeros.copyOf()
            }
        }

        bind.chronometer.start()

        mp = MediaPlayer.create(this, R.raw.sonido)

        vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        bind.tablero.removeAllViews()


        bind.tablero.post{
            val width = bind.tablero.width
            val params = bind.tablero.layoutParams
            params.height = width
            bind.tablero.layoutParams = params

            ids = Array(topTileX) { IntArray(topTileY) }
            values = Array(topTileX) { IntArray(topTileY) }

            var ident = 0
            for(i in 0 until topTileY){
                var l2 = LinearLayout(this)
                l2.orientation = LinearLayout.HORIZONTAL
                for(j in 0 until topTileX){
                    var trama = miRandom(topElement)
                    values[j][i] = trama
                    var celda = CeldaView(this,j,i,topElement,trama,dibujos[trama])
                    ident++
                    celda.id = ident
                    ids[j][i] = ident
                    celda.layoutParams = LinearLayout.LayoutParams(0,(width-topTileY*10)/topTileY,1.0f)
                    celda.layoutParams = (celda.layoutParams as ViewGroup.MarginLayoutParams).apply {
                        setMargins(5,5,5,5)
                    }
                    celda.setOnClickListener{
                        hasClick(celda.x, celda.y)
                    }
                    l2.addView(celda)
                }
                bind.tablero.addView(l2)
            }
        }
    }

    fun miRandom(max: Int): Int {
        val random = Random()
        return random.nextInt(max)
    }

    fun hasClick(x:Int,y:Int){
        if (sonido){
            mp.start()
        }
        if(vibracion){
            vibratorService.vibrate(100L)
        }
        if(x==0 && y==0){
            changeView(0,1)
            changeView(1,0)
            changeView(1,1)
        }else if(x == 0 && y == topTileY -1){
            changeView(0,topTileY-2)
            changeView(1,topTileY-2)
            changeView(1,topTileY-1)
        }else if(x == topTileX-1 && y==0){
            changeView(topTileX-2,0)
            changeView(topTileX-1,1)
            changeView(topTileX-2,1)
        }else if(x == topTileX - 1 && y == topTileY-1){
            changeView(topTileX-1, topTileY-2)
            changeView(topTileX-2,topTileY-2)
            changeView(topTileX-2,topTileY-1)
        }else if(x == 0){
            changeView(x,y-1)
            changeView(x,y+1)
            changeView(x+1,y)
        }else if(y == 0){
            changeView(x+1,y)
            changeView(x-1,y)
            changeView(x,y+1)
        }else if(x == topTileX-1){
            changeView(x,y-1)
            changeView(x,y+1)
            changeView(x-1,y)
        }else if(y == topTileY-1){
            changeView(x+1,y)
            changeView(x-1,y)
            changeView(x,y-1)
        }else{
            changeView(x-1,y)
            changeView(x+1,y)
            changeView(x,y+1)
            changeView(x,y-1)
        }
        contador++;
        bind.pulsacion.text =  "Pulsaciones: " + contador
        checkIfFinished()
    }

    fun changeView(x:Int,y:Int){
        var celda = findViewById<Button>(ids[x][y])
        var newIndex = (celda as CeldaView).getNewIndex()
        values[x][y] = newIndex
        celda.setBackgroundResource(dibujos[newIndex])
        celda.invalidate()
    }

    fun checkIfFinished(){
        var finish = true
        var valor = values[0][0]
        for(col in values){
            for(celda in col){
                if (valor != celda){
                    finish = false
                }
            }
        }
        if(finish){
            bind.chronometer.stop()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("BIENNNNNNNNNNNNN!")
            val message = "Has ganado en TAN SOLO $contador movimientos, en el tiempo ${bind.chronometer.text}"
            builder.setMessage(message)
            builder.setPositiveButton("OK") { _, _ ->
                finish()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }




}