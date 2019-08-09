package com.aly.nova.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class DefaultConverter {

    class BitmapConverter : NovaConverter<Bitmap> {
        override fun from(byteArray: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size);
        }
    }

}