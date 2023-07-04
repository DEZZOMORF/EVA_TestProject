package test.project.eva.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes str:Int) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}