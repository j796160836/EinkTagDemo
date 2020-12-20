package com.johnny.e_inktagdemo

import android.content.Intent
import android.graphics.Bitmap
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_send_bitmap.*
import waveshare.feng.nfctag.activity.WaveShare
import java.io.IOException


class SendBitmapActivity : AppCompatActivity() {
    companion object {
        private val TAG = SendBitmapActivity::class.java.name

        // https://www.waveshare.net/wiki/Android_SDK_for_NFC-Powered_e-Paper
        const val IMAGE_WIDTH = 400   // TODO: Hardcode image width
        const val IMAGE_HEIGHT = 300  // TODO: Hardcode image height
        const val SIZE_FLAG = 3   // TODO: Hardcode size flag (4.2inch E-ink screen)
    }

    private var nfcAdapter: NfcAdapter? = null

    private var bitmap: Bitmap = ImageUtils.generateBitmap(IMAGE_WIDTH, IMAGE_HEIGHT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_bitmap)

        image_preview.setImageBitmap(bitmap)
        label_loading.text = "Ready to upload"
        progress_bar.visibility = View.GONE
    }

    var totalProgress = 0

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) { //识别到NFC

            val detectedTag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val tech: Array<String> = detectedTag?.techList ?: emptyArray() //得到描述符
            if (tech.isNotEmpty() && tech[0] == "android.nfc.tech.NfcA") { //如果描述符正确
                val t: Thread = object : Thread() {
                    //创建进程 保证主程序不阻塞
                    override fun run() {
                        var success = false
                        val a = WaveShare() //创建发送的实例。
                        a.setup() //初始化发送函数
                        val thread = Thread {
                            totalProgress = 0
                            while (totalProgress != -1) {
                                totalProgress = a.progress //读取进度
                                if (totalProgress == -1) {
                                    break
                                }
                                runOnUiThread {
                                    label_loading.text = "Uploading ... $totalProgress%"
                                }
                                if (totalProgress == 100) {
                                    break
                                }
                                SystemClock.sleep(10) //防止过度占用CPU
                            }
                        }
                        thread.start() //开启线程
                        val tnTag: NfcA = NfcA.get(detectedTag) //NFC接口 //获取给定标签的实例
                        try {
                            val whetherSucceed = a.sendBitmap(tnTag, SIZE_FLAG, bitmap) //发送函数
                            if (whetherSucceed) {
                                success = true
                                runOnUiThread {
                                    label_loading.text = "Upload success."
                                }
                            } else {
                                runOnUiThread {
                                    label_loading.text = "Upload failed."
                                }
                            }
                        } finally {
                            try {
                                tnTag.close()
                            } catch (e: IOException) { //发送异常处理  NFC I/O异常
                                e.printStackTrace()
                            }
                        }
                    }
                }
                t.start() //开启线程
            }
        }
    }
}