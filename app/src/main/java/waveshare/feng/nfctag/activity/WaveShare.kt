package waveshare.feng.nfctag.activity

import android.graphics.Bitmap
import android.nfc.tech.NfcA

// https://www.waveshare.net/wiki/Android_SDK_for_NFC-Powered_e-Paper

class WaveShare : a() {
    val progress: Int = c

    public override fun a() {
        super.a()
    }

//    public override fun a(bitmap: Bitmap?): Bitmap {
//        return super.a(bitmap)
//    }

    public override fun a(nfc: NfcA?, p1: Int, p2: Bitmap?): Int {
        return super.a(nfc, p1, p2)
    }

    fun setup() {
        a()
    }

    fun sendBitmap(nfc: NfcA?, sizeFlag: Int, bitmap: Bitmap?): Boolean {
        return a(nfc, sizeFlag, bitmap) == 1
    }
}

enum class EInkModelType(val raw: Int) {
    eInk2_13inch(1),
    eInk2_9inch(2),
    eInk4_2inch(3),
    eInk7_5inch(4),
    eInk7_5inch_HD(5),
    eInk2_7inch(6),
    eInk2_9inch_B(7)
}