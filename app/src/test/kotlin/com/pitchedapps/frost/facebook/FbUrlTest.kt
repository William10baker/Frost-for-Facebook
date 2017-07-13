package com.pitchedapps.frost.facebook

import org.junit.Test
import kotlin.test.assertEquals


/**
 * Created by Allan Wang on 2017-07-07.
 */
class FbUrlTest {

    @Test
    fun base() {
        val url = "https://touch.facebook.com/relative/?asdf=1234&hjkl=7890"
        assertFbFormat(url, url)
    }

    @Test
    fun relative() {
        val url = "/relative/?asdf=1234&hjkl=7890"
        assertFbFormat("$FB_URL_BASE${url.substring(1)}", url)
    }

    @Test
    fun redirect() {
        assertFbFormat("$FB_URL_BASE/relative/?asdf=1234&hjkl=7890", "https://touch.facebook.com/l.php?u=$FB_URL_BASE/relative/&asdf=1234&hjkl=7890")
    }

    @Test fun discard() {
        val prefix = "$FB_URL_BASE/?test=1234"
        val suffix = "&apple=notorange"
        assertFbFormat("$prefix$suffix", "$prefix&ref=hello$suffix")
    }

    fun assertFbFormat(expected: String, url: String) {
        val fbUrl = FbUrlFormatter(url)
        assertEquals(expected, fbUrl.toString(), "FbUrl Mismatch:\n${fbUrl.toLogList().joinToString("\n\t")}")
    }
}