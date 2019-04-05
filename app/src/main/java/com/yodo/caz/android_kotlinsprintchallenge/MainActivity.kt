package com.yodo.caz.android_kotlinsprintchallenge

import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

// Charles Godoy
// Kotlin Sprint

class MainActivity : AppCompatActivity() {

    val url = "http://hubblesite.org/api/v3/video/1"
    private var playbackPosition = 0
    val job = Job()
    val dataScope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var mediaController: android.widget.MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mediaController = MediaController(this)

        videoView.setOnPreparedListener {
            mediaController.setAnchorView(videoContainer)
            videoView.setMediaController(mediaController)
            videoView.seekTo(playbackPosition)
            videoView.start()
        }

        //turn off progress bar
        videoView.setOnInfoListener { player, what, extra ->
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                progressBar.visibility = View.INVISIBLE
            true
        }
    }

    override fun onStart() {
        super.onStart()
        dataScope.async {
            val (success, result) = NetworkAdapter.httpRequest(
                url, NetworkAdapter.GET, null
            )
            if (success == true) {
                val videoData = Json.nonstrict.parse(VideoStream.serializer(), result)
                val uri = Uri.parse(videoData.getUrl())

                // first get uri
                withContext(Dispatchers.Main) {
                    // call videoview and set uri
                    videoView.setVideoURI(uri)
                }
            }

            videoSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        videoView.seekTo(progress.times(videoSeekBar.max))
                    }
                    Toast.makeText(this@MainActivity, "Seek Bar progress change", Toast.LENGTH_SHORT).show()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    Toast.makeText(this@MainActivity, "onStartTrackingTouch - Seek Bar", Toast.LENGTH_SHORT).show()
                }
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    Toast.makeText(this@MainActivity, "onStopTrackingTouch - Seek Bar", Toast.LENGTH_SHORT).show()
                }
            })

            button.setOnClickListener{
                if (videoView.isPlaying) {
                    videoView.pause()
                    Toast.makeText(this@MainActivity, "Pause button test", Toast.LENGTH_SHORT).show()
                } else {
                    videoView.start()
                    Toast.makeText(this@MainActivity, "Start button test", Toast.LENGTH_SHORT).show()
                }
            }
        }

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
