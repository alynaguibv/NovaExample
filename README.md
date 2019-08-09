```kotlin

val dataCallback = object : DataCallback<ExampleResponse> {
            override fun onDataReceived(t: ExampleResponse) {
                Log.e("TAG", t.toString())
            }

            override fun onError(throwable: Throwable) {
                Toast.makeText(this@MainActivity, "An error occurred, please try again", Toast.LENGTH_LONG).show()
            }
        }

        Nova.from("http://pastebin.com/raw/wgkJgazE", ExampleUtil.pastePinConverter, dataCallback)

```