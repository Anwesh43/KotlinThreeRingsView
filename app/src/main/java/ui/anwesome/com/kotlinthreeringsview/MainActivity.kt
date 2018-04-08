package ui.anwesome.com.kotlinthreeringsview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.threeringsview.ThreeRingsView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThreeRingsView.create(this)
    }
}
