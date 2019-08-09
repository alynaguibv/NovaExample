package com.aly.nova.example

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aly.nova.callback.DataCallback
import com.aly.nova.example.model.ExampleResponse
import com.aly.nova.example.util.ExampleUtil
import com.aly.nova.main.Nova

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dataCallback = object : DataCallback<ExampleResponse> {
            override fun onDataReceived(t: ExampleResponse) {
                Log.e("TAG", t.toString())
            }

            override fun onError(throwable: Throwable) {
                Toast.makeText(this@MainActivity, "An error occurred, please try again", Toast.LENGTH_LONG).show()
            }
        }

        Nova.from("http://pastebin.com/raw/wgkJgazE", ExampleUtil.pastePinConverter, dataCallback)
    }
}
