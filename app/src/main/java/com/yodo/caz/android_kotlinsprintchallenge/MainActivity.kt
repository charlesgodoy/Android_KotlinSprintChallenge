package com.yodo.caz.android_kotlinsprintchallenge

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var playbackPosition = 0
    private val testVideo = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    private lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaController = MediaController(this)
        videoView.setOnPreparedListener {
            videoView.seekTo(playbackPosition)
            videoView.start()
        }
    }

    override fun onStart() {
        super.onStart()

        // first get uri
        val uri = Uri.parse(testVideo)

        // call videoview and set uri
        videoView.setVideoURI(uri)

        progressBar.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()

        videoView.pause()
        playbackPosition = videoView.currentPosition
    }

    override fun onStop() {
        videoView.stopPlayback()
        super.onStop()

    }
}
