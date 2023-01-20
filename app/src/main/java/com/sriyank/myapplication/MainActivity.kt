package com.sriyank.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val CITY:String = "dhaka,bd"
        val API :String ="06c921750b9a82d8f5d1294e1586276f"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }

    inner class weatherTask():AsyncTask<String , Void , String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility=View.GONE
            findViewById<TextView>(R.id.errorText).visibility=View.GONE
        }

        override fun doInBackground(vararg params: String?): String?{
            var response:String?
            try{
                response=URL("https://api.openweathermap.org/data/2.5/weather?q=dhaka,bd&units=metric&appid=06c921750b9a82d8f5d1294e1586276f").readText(Charsets.UTF_8)
            }
            catch(e: Exception){
                response = null
            }
            return response



        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try{
                val jsonOBJ = JSONObject(result)
                val main = jsonOBJ.getJSONObject("main")
                val sys = jsonOBJ.getJSONObject("sys")
                val wind = jsonOBJ.getJSONObject("wind")
                val weather= jsonOBJ.getJSONArray("weather").getJSONObject(0)
                val updateat:Long = jsonOBJ.getLong("dt")
                val Updateattext = "Updated at : "+SimpleDateFormat("dd/MM/yyyy hh:mm a" , Locale.ENGLISH ).format(
                    Date(updateat*1000)
                )
                val temp = main.getString("temp")+"°C"
                val tempmin= " Min Temp: "+main.getString("temp_min")+"°C"
                val tempmax= " Max Temp: "+main.getString("temp_max")+"°C"

                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonOBJ.getString("name")+", "+sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = Updateattext
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempmin
                findViewById<TextView>(R.id.temp_max).text = tempmax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a" , Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a" , Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility=View.VISIBLE


            }
            catch (e: Exception){
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility=View.VISIBLE
            }

        }
    }

}